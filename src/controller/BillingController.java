package controller;

import exception.InsufficientStockException;
import exception.ValidationException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.Appointment;
import model.BoardingRoom;
import model.Invoice;
import model.InvoiceItem;
import model.Payment;
import model.Pet;
import model.ServiceItem;
import model.Treatment;
import model.TreatmentMedicine;
import service.AppointmentService;
import service.BoardingBookingService;
import service.BoardingRoomService;
import service.CustomerService;
import service.InvoiceService;
import service.MedicineService;
import service.PetService;
import service.PrescriptionService;
import service.ReportService;
import service.ServiceService;
import service.TreatmentService;
import util.ComboItem;
import util.Validator;
import view.BillingForm;

// Billing screen controller
public class BillingController extends BaseController<BillingForm>
{
    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");

    private final CustomerService customerService = new CustomerService();
    private final PetService petService = new PetService();
    private final ServiceService serviceService = new ServiceService();
    private final MedicineService medicineService = new MedicineService();
    private final BoardingBookingService boardingService = new BoardingBookingService();
    private final BoardingRoomService roomService = new BoardingRoomService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final TreatmentService treatmentService = new TreatmentService();
    private final PrescriptionService prescriptionService = new PrescriptionService();
    private final InvoiceService invoiceService = new InvoiceService();

    private List<Pet> allPets = new ArrayList<>();
    private List<ServiceItem> allServices = new ArrayList<>();
    private final Map<Integer, BigDecimal> itemPrices = new HashMap<>();
    private final Map<Integer, Integer> medicineStock = new HashMap<>();
    private final List<InvoiceItem> cart = new ArrayList<>();
    private List<Invoice> invoices = new ArrayList<>();
    private boolean loading;

    public BillingController(BillingForm view)
    {
        super(view);
        attachSidebar("BILLING");
        wire();
        guard(() ->
        {
            loadLookups();
            loadInvoices();
        });
    }

    // Factory entry
    public static BillingForm open()
    {
        BillingForm v = new BillingForm();
        new BillingController(v);
        return v;
    }

    // Register listeners
    private void wire()
    {
        view.getBtnAddCharges().addActionListener(e -> guard(this::addAppointmentCharges));
        view.getBtnAddItem().addActionListener(e -> guard(this::addItem));
        view.getBtnRemoveItem().addActionListener(e -> removeItem());
        view.getBtnSave().addActionListener(e -> guard(this::saveInvoice));
        view.getBtnClear().addActionListener(e -> clearForm());
        view.getBtnPay().addActionListener(e -> guard(this::recordPayment));
        view.getBtnPrint().addActionListener(e -> guard(this::printInvoice));
        view.getBtnCancel().addActionListener(e -> guard(this::cancelInvoice));
        view.getBtnRefresh().addActionListener(e -> guard(this::loadInvoices));

        view.getCmbCustomer().addActionListener(e ->
        {
            if (!loading)
            {
                guard(this::onCustomerChanged);
            }
        });
        view.getCmbType().addActionListener(e ->
        {
            if (!loading)
            {
                guard(this::onTypeChanged);
            }
        });
        view.getCmbItem().addActionListener(e ->
        {
            if (!loading)
            {
                onItemChanged();
            }
        });

        // Live totals
        DocumentListener recalc = new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                updateTotals();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                updateTotals();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                updateTotals();
            }
        };
        view.getTxtDiscount().getDocument().addDocumentListener(recalc);
        view.getTxtTaxRate().getDocument().addDocumentListener(recalc);

        // Filters
        DocumentListener filter = new DocumentListener()
        {
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                applyFilter();
            }
        };
        view.getTxtSearch().getDocument().addDocumentListener(filter);
        view.getCmbFilter().addActionListener(e -> applyFilter());

        // Enter chain
        focusNext(view.getTxtQty(), view.getTxtPrice());
        view.getTxtPrice().addActionListener(e -> guard(this::addItem));
        focusNext(view.getTxtDiscount(), view.getTxtTaxRate());
        focusNext(view.getTxtTaxRate(), view.getTxtPaidNow());
        focusNext(view.getTxtPaidNow(), view.getTxtNotes());
        view.getTxtNotes().addActionListener(e -> guard(this::saveInvoice));
    }

    // ------------------------------------------------------------ Loading

    private void loadLookups()
    {
        loading = true;
        try
        {
            allPets = petService.getAllPets();
            allServices = serviceService.getAllServicesForManagement();

            List<ComboItem> customers = new ArrayList<>();
            customerService.getAllCustomers().stream()
                    .filter(c -> "Active".equalsIgnoreCase(c.getStatus()))
                    .forEach(c -> customers.add(new ComboItem(c.getCustomerId(), c.getFullName())));
            view.setCombo(view.getCmbCustomer(), customers);
        }
        finally
        {
            loading = false;
        }
        onCustomerChanged();
        onTypeChanged();
    }

    private void onCustomerChanged()
    {
        int customerId = view.selectedId(view.getCmbCustomer());

        // Pets of customer
        List<ComboItem> pets = new ArrayList<>();
        pets.add(new ComboItem(0, "(No pet)"));
        allPets.stream().filter(p -> p.getCustomerId() == customerId)
                .forEach(p -> pets.add(new ComboItem(p.getPetId(), p.getPetName())));
        view.setCombo(view.getCmbPet(), pets);

        // Appointments of customer
        List<ComboItem> appts = new ArrayList<>();
        appts.add(new ComboItem(0, "(None)"));
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Map<Integer, String> serviceNames = allServices.stream()
                .collect(Collectors.toMap(ServiceItem::getServiceId, ServiceItem::getServiceName, (a, b) -> a));
        appointmentService.getAllAppointments().stream()
                .filter(a -> a.getCustomerId() == customerId && !"Cancelled".equalsIgnoreCase(a.getStatus()))
                .forEach(a -> appts.add(new ComboItem(a.getAppointmentId(),
                        "#" + a.getAppointmentId() + " - " + serviceNames.getOrDefault(a.getServiceId(), "Service")
                        + " - " + (a.getAppointmentDatetime() == null ? "" : f.format(a.getAppointmentDatetime())))));
        view.setCombo(view.getCmbAppointment(), appts);

        if ("Boarding Stay".equals(view.getCmbType().getSelectedItem()))
        {
            onTypeChanged();
        }
    }

    private void onTypeChanged()
    {
        String type = String.valueOf(view.getCmbType().getSelectedItem());
        List<ComboItem> items = new ArrayList<>();
        itemPrices.clear();
        medicineStock.clear();

        switch (type)
        {
            case "Service" -> allServices.stream()
                    .filter(s -> "Active".equalsIgnoreCase(s.getStatus()))
                    .forEach(s ->
                    {
                        itemPrices.put(s.getServiceId(), s.getPrice());
                        items.add(new ComboItem(s.getServiceId(), s.getServiceName()));
                    });
            case "Medicine" -> medicineService.getActiveMedicines().stream()
                    .filter(m -> m.getStockQuantity() > 0)
                    .forEach(m ->
                    {
                        itemPrices.put(m.getMedicineId(), m.getUnitPrice());
                        medicineStock.put(m.getMedicineId(), m.getStockQuantity());
                        items.add(new ComboItem(m.getMedicineId(), m.toString()));
                    });
            case "Boarding Stay" ->
            {
                int customerId = view.selectedId(view.getCmbCustomer());
                Map<Integer, String> rooms = roomService.getAllRooms().stream()
                        .collect(Collectors.toMap(BoardingRoom::getRoomId, BoardingRoom::getRoomNumber, (a, b) -> a));
                boardingService.getAllBookings().stream()
                        .filter(b -> b.getCustomerId() == customerId && !"Cancelled".equalsIgnoreCase(b.getStatus()))
                        .forEach(b ->
                        {
                            itemPrices.put(b.getBoardingId(), b.getTotalAmount());
                            items.add(new ComboItem(b.getBoardingId(), "Room " + rooms.getOrDefault(b.getRoomId(), "?")
                                    + ": " + b.getCheckIn() + " to " + b.getCheckOut() + " (" + b.getNumberOfDays() + " days)"));
                        });
            }
            default ->
            {
                // Free text item
            }
        }

        loading = true;
        try
        {
            view.setCombo(view.getCmbItem(), items);
            view.getCmbItem().setEditable("Other".equals(type));
        }
        finally
        {
            loading = false;
        }
        view.getTxtQty().setText("1");
        view.getTxtQty().setEnabled(!"Boarding Stay".equals(type));
        onItemChanged();
    }

    private void onItemChanged()
    {
        String type = String.valueOf(view.getCmbType().getSelectedItem());
        view.getTxtPrice().setEditable("Other".equals(type) || "Service".equals(type));
        BigDecimal price = itemPrices.get(view.selectedId(view.getCmbItem()));
        view.getTxtPrice().setText(price == null ? "" : price.setScale(2).toPlainString());
    }

    private void loadInvoices()
    {
        invoices = invoiceService.getAllInvoices();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Object[]> rows = new ArrayList<>();
        for (Invoice inv : invoices)
        {
            rows.add(new Object[]{
                inv.getInvoiceId(),
                inv.getInvoiceDate() == null ? "" : f.format(inv.getInvoiceDate()),
                inv.getCustomerName(),
                inv.getPetName() == null ? "" : inv.getPetName(),
                MONEY.format(inv.getTotalAmount()),
                MONEY.format(inv.getPaidAmount()),
                MONEY.format(inv.getBalance()),
                inv.getStatus()
            });
        }
        view.setInvoiceRows(rows);

        // Stat cards
        BigDecimal revenue = invoices.stream()
                .filter(i -> !Invoice.CANCELLED.equals(i.getStatus()))
                .map(Invoice::getPaidAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal outstanding = invoices.stream()
                .filter(i -> !Invoice.CANCELLED.equals(i.getStatus()))
                .map(Invoice::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        long unpaid = invoices.stream()
                .filter(i -> Invoice.UNPAID.equals(i.getStatus()) || Invoice.PARTIAL.equals(i.getStatus())).count();
        view.setStats(String.valueOf(invoices.size()), MONEY.format(revenue), MONEY.format(outstanding), String.valueOf(unpaid));
    }

    private void applyFilter()
    {
        view.filterInvoices(view.getTxtSearch().getText().trim(), String.valueOf(view.getCmbFilter().getSelectedItem()));
    }

    // ------------------------------------------------------------ Cart

    private void addItem()
    {
        String type = String.valueOf(view.getCmbType().getSelectedItem());
        int qty = Validator.parsePositiveInt(view.getTxtQty().getText(), "Quantity");
        BigDecimal price = Validator.parseMoney(view.getTxtPrice().getText(), "Unit price");

        Object selected = view.getCmbItem().getSelectedItem();
        if (selected == null || selected.toString().trim().isEmpty())
        {
            throw new ValidationException("Item", "Please choose or type an item.");
        }

        InvoiceItem item = new InvoiceItem();
        item.setQuantity(qty);
        item.setUnitPrice(price);
        switch (type)
        {
            case "Service" ->
            {
                item.setItemType(InvoiceItem.SERVICE);
                item.setItemId(((ComboItem) selected).getId());
                item.setDescription(selected.toString());
            }
            case "Medicine" ->
            {
                int id = ((ComboItem) selected).getId();
                int inCart = cart.stream()
                        .filter(c -> InvoiceItem.MEDICINE.equals(c.getItemType()) && c.getItemId() != null && c.getItemId() == id)
                        .mapToInt(InvoiceItem::getQuantity).sum();
                if (inCart + qty > medicineStock.getOrDefault(id, 0))
                {
                    throw new InsufficientStockException("Only " + medicineStock.getOrDefault(id, 0) + " in stock.");
                }
                item.setItemType(InvoiceItem.MEDICINE);
                item.setItemId(id);
                item.setDescription(((ComboItem) selected).toString().replaceAll(" \\(stock \\d+\\)$", ""));
            }
            case "Boarding Stay" ->
            {
                item.setItemType(InvoiceItem.BOARDING);
                item.setItemId(((ComboItem) selected).getId());
                item.setDescription("Boarding - " + selected);
                item.setQuantity(1);
            }
            default ->
            {
                item.setItemType(InvoiceItem.OTHER);
                item.setDescription(Validator.maxLength(selected.toString().trim(), 150, "Description"));
            }
        }
        cart.add(item);
        refreshCart();
        view.getTxtQty().setText("1");
        view.setMessage(" ");
    }

    private void removeItem()
    {
        int row = view.getSelectedCartRow();
        if (row < 0)
        {
            warn("Select an item to remove.");
            return;
        }
        cart.remove(row);
        refreshCart();
    }

    private void refreshCart()
    {
        List<Object[]> rows = new ArrayList<>();
        for (InvoiceItem it : cart)
        {
            rows.add(new Object[]{it.getItemType(), it.getDescription(), it.getQuantity(),
                MONEY.format(it.getUnitPrice()), MONEY.format(it.getLineTotal())});
        }
        view.setCart(rows);
        updateTotals();
    }

    // Tolerant parse
    private BigDecimal lenient(String text)
    {
        try
        {
            return new BigDecimal(text.trim().replace(",", ""));
        }
        catch (NumberFormatException e)
        {
            return BigDecimal.ZERO;
        }
    }

    private void updateTotals()
    {
        BigDecimal sub = InvoiceService.subtotal(cart);
        BigDecimal discount = lenient(view.getTxtDiscount().getText());
        BigDecimal tax = InvoiceService.taxAmount(sub, discount, lenient(view.getTxtTaxRate().getText()));
        view.setTotals(MONEY.format(sub), MONEY.format(sub.subtract(discount).max(BigDecimal.ZERO).add(tax)));
    }

    // Import appointment charges
    private void addAppointmentCharges()
    {
        int appointmentId = view.selectedId(view.getCmbAppointment());
        if (appointmentId <= 0)
        {
            throw new ValidationException("Appointment", "Select an appointment first.");
        }

        Appointment appt = appointmentService.getAllAppointments().stream()
                .filter(a -> a.getAppointmentId() == appointmentId).findFirst()
                .orElseThrow(() -> new ValidationException("Appointment not found."));

        int added = 0;
        boolean hasService = cart.stream().anyMatch(c -> InvoiceItem.SERVICE.equals(c.getItemType())
                && c.getItemId() != null && c.getItemId() == appt.getServiceId());
        if (!hasService)
        {
            ServiceItem svc = allServices.stream().filter(s -> s.getServiceId() == appt.getServiceId()).findFirst().orElse(null);
            if (svc != null)
            {
                cart.add(new InvoiceItem(InvoiceItem.SERVICE, svc.getServiceId(), svc.getServiceName(), 1, svc.getPrice()));
                added++;
            }
        }

        // Prescribed medicines
        List<Treatment> treatments = treatmentService.getAllTreatments().stream()
                .filter(t -> t.getAppointmentId() == appointmentId).toList();
        for (Treatment t : treatments)
        {
            for (TreatmentMedicine tm : prescriptionService.getByTreatment(t.getTreatmentId()))
            {
                boolean exists = cart.stream().anyMatch(c -> InvoiceItem.PRESCRIPTION.equals(c.getItemType())
                        && c.getItemId() != null && c.getItemId() == tm.getId());
                if (!exists)
                {
                    cart.add(new InvoiceItem(InvoiceItem.PRESCRIPTION, tm.getId(),
                            "Medicine - " + tm.getMedicineName(), tm.getQuantity(), tm.getUnitPrice()));
                    added++;
                }
            }
        }

        // Link pet
        view.selectId(view.getCmbPet(), appt.getPetId());
        refreshCart();
        view.setMessage(added == 0 ? "No new charges found for this appointment." : added + " charge(s) added.");
    }

    // ------------------------------------------------------------ Invoices

    // Validate and save the complete invoice.
    private void saveInvoice()
    {
        int customerId = Validator.requireSelected(
                view.selectedId(view.getCmbCustomer()), "Customer");

        if (cart.isEmpty())
        {
            throw new ValidationException("Items", "Add at least one item or service to the invoice.");
        }

        int petId = view.selectedId(view.getCmbPet());
        int apptId = view.selectedId(view.getCmbAppointment());

        BigDecimal subtotal = InvoiceService.subtotal(cart);

        String discountText = view.getTxtDiscount().getText().trim();
        BigDecimal discount = discountText.isEmpty()
                ? BigDecimal.ZERO
                : Validator.parseMoney(discountText, "Discount");

        if (discount.compareTo(subtotal) > 0)
        {
            throw new ValidationException("Discount",
                    "Discount cannot be greater than the invoice subtotal.");
        }

        String rateText = view.getTxtTaxRate().getText().trim();
        BigDecimal rate = rateText.isEmpty()
                ? BigDecimal.ZERO
                : Validator.parseMoney(rateText, "Tax rate");

        if (rate.compareTo(new BigDecimal("100")) > 0)
        {
            throw new ValidationException("Tax rate", "Tax rate cannot exceed 100%.");
        }

        BigDecimal total = subtotal
                .subtract(discount)
                .max(BigDecimal.ZERO)
                .add(InvoiceService.taxAmount(subtotal, discount, rate));

        String paidText = view.getTxtPaidNow().getText().trim();
        BigDecimal paidNow = paidText.isEmpty()
                ? BigDecimal.ZERO
                : Validator.parseMoney(paidText, "Paid amount");

        if (paidNow.compareTo(total) > 0)
        {
            throw new ValidationException("Paid amount",
                    "Paid amount cannot be greater than the invoice total.");
        }

        String notes = Validator.maxLength(
                view.getTxtNotes().getText().trim(), 255, "Notes");

        Invoice inv = Invoice.builder()
                .customer(customerId)
                .pet(petId > 0 ? petId : null)
                .appointment(apptId > 0 ? apptId : null)
                .notes(notes)
                .discount(discount)
                .items(new ArrayList<>(cart))
                .build();

        Payment first = null;
        if (paidNow.signum() > 0)
        {
            String method = Validator.requireText(
                    String.valueOf(view.getCmbMethod().getSelectedItem()),
                    "Payment method");

            if (!method.equals("Cash") && !method.equals("Card") && !method.equals("Online"))
            {
                throw new ValidationException("Payment method", "Please select a valid payment method.");
            }

            first = new Payment();
            first.setAmount(paidNow);
            first.setPaymentMethod(method);
            first.setReferenceNo("");
        }

        int id = invoiceService.createInvoice(inv, rate, first);

        clearForm();
        loadInvoices();
        loadLookups();

        int print = JOptionPane.showConfirmDialog(
                view,
                "Invoice #" + id + " saved. Print it now?",
                "Success",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);

        if (print == JOptionPane.YES_OPTION)
        {
            ReportService.showInvoice(id);
        }
    }

    private Invoice selectedInvoice()
    {
        int id = view.getSelectedInvoiceId();
        if (id <= 0)
        {
            throw new ValidationException("Invoice", "Select an invoice from the list first.");
        }
        return invoices.stream().filter(i -> i.getInvoiceId() == id).findFirst()
                .orElseThrow(() -> new ValidationException("Invoice not found."));
    }

    private void recordPayment()
    {
        Invoice inv = selectedInvoice();
        if (Invoice.CANCELLED.equals(inv.getStatus()) || Invoice.PAID.equals(inv.getStatus()))
        {
            throw new ValidationException("Invoice", "This invoice is already " + inv.getStatus().toLowerCase() + ".");
        }

        String head = "Invoice #" + inv.getInvoiceId() + " - " + inv.getCustomerName()
                + " (balance LKR " + MONEY.format(inv.getBalance()) + ")";
        Object amount = JOptionPane.showInputDialog(view, head + "\nAmount (LKR)", "Record Payment",
                JOptionPane.PLAIN_MESSAGE, null, null, inv.getBalance().setScale(2).toPlainString());
        if (amount == null)
        {
            return;
        }
        Object method = JOptionPane.showInputDialog(view, "Method", "Record Payment",
                JOptionPane.PLAIN_MESSAGE, null, new String[]{"Cash", "Card", "Online"}, "Cash");
        if (method == null)
        {
            return;
        }
        Object ref = JOptionPane.showInputDialog(view, "Reference (optional)", "Record Payment",
                JOptionPane.PLAIN_MESSAGE, null, null, "");
        if (ref == null)
        {
            return;
        }
        BigDecimal paymentAmount = Validator.parseMoney(amount.toString(), "Amount");
        if (paymentAmount.compareTo(inv.getBalance()) > 0)
        {
            throw new ValidationException("Amount", "Payment cannot be greater than the invoice balance.");
        }
        String reference = Validator.maxLength(ref.toString().trim(), 100, "Reference");

        invoiceService.recordPayment(inv.getInvoiceId(), paymentAmount,
                method.toString(), reference);
        loadInvoices();
        JOptionPane.showMessageDialog(view, "Payment recorded.", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void printInvoice()
    {
        ReportService.showInvoice(selectedInvoice().getInvoiceId());
    }

    private void cancelInvoice()
    {
        Invoice inv = selectedInvoice();
        int confirm = JOptionPane.showConfirmDialog(view, "Cancel invoice #" + inv.getInvoiceId() + "?",
                "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION)
        {
            return;
        }
        invoiceService.cancelInvoice(inv.getInvoiceId());
        loadInvoices();
        loadLookups();
        view.setMessage("Invoice #" + inv.getInvoiceId() + " cancelled.");
    }

    private void clearForm()
    {
        cart.clear();
        refreshCart();
        view.clearForm();
    }
}

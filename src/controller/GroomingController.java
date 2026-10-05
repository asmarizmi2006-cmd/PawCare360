package controller;

import exception.AppointmentConflictException;
import exception.DatabaseException;
import exception.ValidationException;
import model.Appointment;
import model.Customer;
import model.Pet;
import model.ServiceItem;
import model.Staff;
import service.AppointmentService;
import service.CustomerService;
import service.PetService;
import service.ServiceService;
import service.StaffService;
import view.GroomingForm;
import util.Validator;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// Grooming screen logic
public class GroomingController extends BaseController<GroomingForm>
{
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter SHOW_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final CustomerService customerService = new CustomerService();
    private final PetService petService = new PetService();
    private final StaffService staffService = new StaffService();
    private final ServiceService serviceService = new ServiceService();
    private final AppointmentService appointmentService = new AppointmentService();

    private List<Customer> customerList = new ArrayList<>();
    private List<Pet> petList = new ArrayList<>();
    private List<Pet> filteredPetList = new ArrayList<>();
    private List<Staff> staffList = new ArrayList<>();
    private List<ServiceItem> serviceList = new ArrayList<>();
    private List<Appointment> shownList = new ArrayList<>();
    private Map<Integer, String> customerNames;
    private Map<Integer, String> petNames;
    private Map<Integer, String> staffNames;
    private Map<Integer, String> serviceNames;

    public GroomingController(GroomingForm view)
    {
        super(view);
        attachSidebar("GROOMING");
        wireEvents();
        loadDropdowns();
        refreshAll();
    }

    public static GroomingForm open()
    {
        GroomingForm v = new GroomingForm();
        new GroomingController(v);
        return v;
    }

    private void wireEvents()
    {
        view.getCmbCustomer().addItemListener(e ->
        {
            if (e.getStateChange() == ItemEvent.SELECTED)
            {
                int index = view.getCmbCustomer().getSelectedIndex();
                if (index >= 0 && index < customerList.size())
                {
                    filterPets(customerList.get(index).getCustomerId());
                }
            }
        });
        view.getTable().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                fillFormFromTable(); // Row click
            }
        });
        view.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        view.getBtnBook().addActionListener(e -> book());
        view.getBtnUpdate().addActionListener(e -> updateStatus());
        view.getBtnCancelAppointment().addActionListener(e -> cancel());
        view.getBtnClear().addActionListener(e -> clear());

        // Enter chain
        focusNext(view.getTxtDate(), view.getTxtTime());
        focusNext(view.getTxtTime(), view.getTxtReason());
        focusNext(view.getTxtReason(), view.getTxtNotes());
        view.getTxtNotes().addActionListener(e -> view.getBtnBook().doClick()); // Last field
    }

    // Shared error handling
    private void handleError(Exception ex)
    {
        if (ex instanceof ValidationException || ex instanceof AppointmentConflictException)
        {
            view.showMessage(ex.getMessage(), false);
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }
        else if (ex instanceof DatabaseException)
        {
            LOG.log(java.util.logging.Level.SEVERE, null, ex);
            view.showMessage(ex.getMessage(), false);
            JOptionPane.showMessageDialog(view, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        else
        {
            LOG.log(java.util.logging.Level.SEVERE, null, ex);
            view.showMessage("Something went wrong. Please try again.", false);
            JOptionPane.showMessageDialog(view, "Something went wrong. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDropdowns()
    {
        try
        {
            customerList = customerService.getAllCustomers();
            customerNames = customerList.stream().collect(Collectors.toMap(Customer::getCustomerId, Customer::getFullName, (a, b) -> a));
            view.setItems(view.getCmbCustomer(),
                    customerList.stream().map(Customer::getFullName).collect(Collectors.toList()));

            petList = petService.getAllPets();
            petNames = petList.stream().collect(Collectors.toMap(Pet::getPetId, Pet::getPetName, (a, b) -> a));

            List<Staff> allStaff = staffService.getAllStaff().stream()
                    .filter(s -> !"Inactive".equalsIgnoreCase(s.getStatus()))
                    .collect(Collectors.toList());
            List<Staff> groomers = allStaff.stream()
                    .filter(s -> "Groomer".equalsIgnoreCase(s.getRole()))
                    .collect(Collectors.toList());
            staffList = groomers.isEmpty() ? allStaff : groomers;
            staffNames = staffService.getAllStaff().stream().collect(Collectors.toMap(Staff::getStaffId, Staff::getFullName, (a, b) -> a));
            view.setItems(view.getCmbStaff(),
                    staffList.stream().map(Staff::getFullName).collect(Collectors.toList()));

            serviceList = serviceService.getAllServices().stream()
                    .filter(s -> "Grooming".equalsIgnoreCase(s.getCategory()))
                    .collect(Collectors.toList());
            serviceNames = serviceService.getAllServicesForManagement().stream()
                    .collect(Collectors.toMap(ServiceItem::getServiceId, ServiceItem::getServiceName, (a, b) -> a));
            view.setItems(view.getCmbService(),
                    serviceList.stream().map(ServiceItem::getServiceName).collect(Collectors.toList()));

            if (!customerList.isEmpty())
            {
                filterPets(customerList.get(0).getCustomerId());
            }
            if (serviceList.isEmpty())
            {
                view.showMessage("Add a Grooming service in the Services screen first.", false);
            }
        }
        catch (Exception ex)
        {
            handleError(ex);
        }
    }

    private void filterPets(int customerId)
    {
        filteredPetList = petList.stream().filter(p -> p.getCustomerId() == customerId).collect(Collectors.toList());
        List<String> names = filteredPetList.stream().map(Pet::getPetName).collect(Collectors.toList());
        if (names.isEmpty())
        {
            names.add("No pets for this customer");
        }
        view.setItems(view.getCmbPet(), names);
    }

    private void refreshAll()
    {
        try
        {
            List<Integer> groomingIds = serviceService.getAllServicesForManagement().stream()
                    .filter(s -> "Grooming".equalsIgnoreCase(s.getCategory()))
                    .map(ServiceItem::getServiceId).collect(Collectors.toList());
            shownList = appointmentService.getAllAppointments().stream()
                    .filter(a -> groomingIds.contains(a.getServiceId()))
                    .collect(Collectors.toList());
            loadTable();
            loadCounts();
        }
        catch (Exception ex)
        {
            handleError(ex);
        }
    }

    private void loadTable()
    {
        List<Object[]> rows = new ArrayList<>();
        shownList.forEach(a -> rows.add(new Object[]{
            a.getAppointmentId(),
            customerNames.getOrDefault(a.getCustomerId(), "Unknown"),
            petNames.getOrDefault(a.getPetId(), "Unknown"),
            a.getStaffId() != null ? staffNames.getOrDefault(a.getStaffId(), "Unknown") : "Unassigned",
            serviceNames.getOrDefault(a.getServiceId(), "Unknown"),
            a.getAppointmentDatetime() != null ? a.getAppointmentDatetime().toLocalDateTime().format(SHOW_FMT) : "",
            a.getStatus()
        }));
        view.setRows(rows);
    }

    private void loadCounts()
    {
        LocalDate today = LocalDate.now();
        int todayCount = (int) shownList.stream()
                .filter(a -> a.getAppointmentDatetime() != null
                        && a.getAppointmentDatetime().toLocalDateTime().toLocalDate().equals(today))
                .count();
        Map<String, Long> byStatus = shownList.stream()
                .filter(a -> a.getStatus() != null)
                .collect(Collectors.groupingBy(a -> a.getStatus().toLowerCase(), Collectors.counting()));
        Function<String, Integer> count = s -> byStatus.getOrDefault(s, 0L).intValue();
        view.setStats(todayCount, count.apply("scheduled"), count.apply("completed"), count.apply("cancelled"));
    }

    // Validate and create a grooming appointment.
    private void book()
    {
        try
        {
            if (serviceList.isEmpty())
            {
                throw new ValidationException("Service", "Add a Grooming service in the Services screen first.");
            }

            int ci = view.getCmbCustomer().getSelectedIndex();
            int si = view.getCmbStaff().getSelectedIndex();
            int vi = view.getCmbService().getSelectedIndex();
            int pi = view.getCmbPet().getSelectedIndex();

            if (ci < 0) throw new ValidationException("Customer", "Please select a customer.");
            if (si < 0) throw new ValidationException("Staff", "Please select a staff member.");
            if (vi < 0) throw new ValidationException("Service", "Please select a grooming service.");
            if (filteredPetList.isEmpty() || pi < 0 || pi >= filteredPetList.size())
                throw new ValidationException("Pet", "This customer has no registered pet selected.");

            LocalDateTime when = parseDateTime();
            if (when == null)
            {
                return;
            }
            if (when.isBefore(LocalDateTime.now()))
            {
                throw new ValidationException("Appointment date/time", "Appointment date and time cannot be in the past.");
            }

            Appointment a = new Appointment();
            a.setCustomerId(customerList.get(ci).getCustomerId());
            a.setPetId(filteredPetList.get(pi).getPetId());
            a.setStaffId(staffList.get(si).getStaffId());
            a.setServiceId(serviceList.get(vi).getServiceId());
            a.setAppointmentDatetime(Timestamp.valueOf(when));
            a.setReason(Validator.maxLength(view.getTxtReason().getText().trim(), 255, "Reason"));
            a.setNotes(Validator.maxLength(view.getTxtNotes().getText().trim(), 500, "Notes"));

            appointmentService.bookAppointment(a);
            clear();
            refreshAll();
            view.showMessage("Grooming booked successfully.", true);
        }
        catch (Exception ex)
        {
            handleError(ex);
        }
    }

    // Parse date, time
    private LocalDateTime parseDateTime()
    {
        String d = view.getTxtDate().getText().trim();
        String t = view.getTxtTime().getText().trim();
        if (d.isEmpty() || t.isEmpty())
        {
            throw new ValidationException("Appointment date/time", "Date and time are required.");
        }
        try
        {
            return LocalDateTime.of(LocalDate.parse(d, DATE_FMT), LocalTime.parse(t, TIME_FMT));
        }
        catch (DateTimeParseException ex)
        {
            throw new ValidationException("Appointment date/time",
                    "Invalid date/time. Use YYYY-MM-DD and HH:MM (24hr).");
        }
    }

    private Integer selectedAppointmentId()
    {
        int row = view.getTable().getSelectedRow();
        if (row == -1)
        {
            JOptionPane.showMessageDialog(view, "Select a grooming booking from the table first.");
            return null;
        }
        return (Integer) view.getTable().getValueAt(row, 0);
    }

    private void updateStatus()
    {
        try
        {
            Integer id = selectedAppointmentId();
            if (id == null)
            {
                return;
            }
            String[] options = {"Scheduled", "Completed", "Cancelled"};
            String chosen = (String) JOptionPane.showInputDialog(view, "Set status to:",
                    "Update Status", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (chosen != null)
            {
                appointmentService.updateStatus(id, chosen);
                refreshAll();
                view.showMessage("Status updated to " + chosen + ".", true);
            }
        }
        catch (Exception ex)
        {
            handleError(ex);
        }
    }

    private void cancel()
    {
        try
        {
            Integer id = selectedAppointmentId();
            if (id == null)
            {
                return;
            }
            if (JOptionPane.showConfirmDialog(view, "Cancel this grooming booking?",
                    "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            {
                appointmentService.updateStatus(id, "Cancelled");
                refreshAll();
                view.showMessage("Booking cancelled.", true);
            }
        }
        catch (Exception ex)
        {
            handleError(ex);
        }
    }

    private void clear()
    {
        view.clearForm();
        if (!customerList.isEmpty())
        {
            filterPets(customerList.get(0).getCustomerId());
        }
    }

    // Row to form
    private void fillFormFromTable()
    {
        int row = view.getTable().getSelectedRow();
        if (row < 0 || row >= shownList.size())
        {
            return;
        }
        Appointment a = shownList.get(row);
        for (int i = 0; i < customerList.size(); i++)
        {
            if (customerList.get(i).getCustomerId() == a.getCustomerId())
            {
                view.getCmbCustomer().setSelectedIndex(i);
                filterPets(a.getCustomerId());
                break;
            }
        }
        for (int i = 0; i < filteredPetList.size(); i++)
        {
            if (filteredPetList.get(i).getPetId() == a.getPetId())
            {
                view.getCmbPet().setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < staffList.size(); i++)
        {
            if (a.getStaffId() != null && staffList.get(i).getStaffId() == a.getStaffId())
            {
                view.getCmbStaff().setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < serviceList.size(); i++)
        {
            if (serviceList.get(i).getServiceId() == a.getServiceId())
            {
                view.getCmbService().setSelectedIndex(i);
                break;
            }
        }
        String date = "";
        String time = "";
        if (a.getAppointmentDatetime() != null)
        {
            LocalDateTime dt = a.getAppointmentDatetime().toLocalDateTime();
            date = dt.toLocalDate().format(DATE_FMT);
            time = dt.toLocalTime().format(TIME_FMT);
        }
        view.fillForm(date, time,
                a.getReason() == null ? "" : a.getReason(),
                a.getNotes() == null ? "" : a.getNotes());
    }
}

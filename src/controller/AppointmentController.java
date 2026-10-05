package controller;

import exception.AppointmentConflictException;
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
import view.AppointmentForm;
import util.Validator;

import javax.swing.JOptionPane;
import java.awt.event.ItemEvent;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Appointment screen logic
public class AppointmentController extends BaseController<AppointmentForm>
{
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(AppointmentController.class.getName());
    private final CustomerService customerService = new CustomerService();
    private final PetService petService = new PetService();
    private final StaffService staffService = new StaffService();
    private final ServiceService serviceService = new ServiceService();
    private final AppointmentService appointmentService = new AppointmentService();

    private final Map<Integer, String> customerNames = new HashMap<>();
    private final Map<Integer, String> petNames = new HashMap<>();
    private final Map<Integer, String> staffNames = new HashMap<>();
    private final Map<Integer, String> serviceNames = new HashMap<>();

    private List<Customer> customerList = new ArrayList<>();
    private List<Pet> petList = new ArrayList<>();
    private List<Pet> filteredPetList = new ArrayList<>();
    private List<Staff> staffList = new ArrayList<>();
    private List<ServiceItem> serviceList = new ArrayList<>();

    public AppointmentController(AppointmentForm view)
    {
        super(view);
        attachSidebar("APPOINTMENTS");

        guard(this::loadDropdowns);
        guard(this::refreshAll);
        wireEvents();

        if (!customerList.isEmpty())
        {
            filterPets(customerList.get(0).getCustomerId());
        }
    }

    public static AppointmentForm open()
    {
        AppointmentForm v = new AppointmentForm();
        new AppointmentController(v);
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
        view.getBtnBook().addActionListener(e -> book());
        view.getBtnUpdate().addActionListener(e -> guard(this::updateStatus));
        view.getBtnCancelAppointment().addActionListener(e -> guard(this::cancel));
        view.getBtnClear().addActionListener(e -> clear());

        // Enter chain
        focusNext(view.getTxtDate(), view.getTxtTime());
        focusNext(view.getTxtTime(), view.getTxtReason());
        focusNext(view.getTxtReason(), view.getTxtNotes());
        view.getTxtNotes().addActionListener(e -> view.getBtnBook().doClick());
    }

    private void loadDropdowns()
    {
        customerList = customerService.getAllCustomers();
        customerList.forEach(c -> customerNames.put(c.getCustomerId(), c.getFullName()));
        view.setItems(view.getCmbCustomer(),
                customerList.stream().map(Customer::getFullName).collect(Collectors.toList()));

        petList = petService.getAllPets();
        petList.forEach(p -> petNames.put(p.getPetId(), p.getPetName()));

        staffList = staffService.getAllStaff();
        staffList.forEach(s -> staffNames.put(s.getStaffId(), s.getFullName()));
        view.setItems(view.getCmbStaff(),
                staffList.stream().map(Staff::getFullName).collect(Collectors.toList()));

        serviceList = serviceService.getAllServices();
        serviceList.forEach(s -> serviceNames.put(s.getServiceId(), s.getServiceName()));
        view.setItems(view.getCmbService(),
                serviceList.stream().map(ServiceItem::getServiceName).collect(Collectors.toList()));
    }

    private void filterPets(int customerId)
    {
        filteredPetList = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for (Pet p : petList)
        {
            if (p.getCustomerId() == customerId)
            {
                filteredPetList.add(p);
                names.add(p.getPetName());
            }
        }
        if (filteredPetList.isEmpty())
        {
            names.add("No pets for this customer");
        }
        view.setItems(view.getCmbPet(), names);
    }

    private void refreshAll()
    {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        loadTable(appointments);
        loadCounts(appointments);
    }

    private void loadTable(List<Appointment> appointments)
    {
        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<Object[]> rows = new ArrayList<>();
        for (Appointment a : appointments)
        {
            rows.add(new Object[]{
                a.getAppointmentId(),
                customerNames.getOrDefault(a.getCustomerId(), "Unknown"),
                petNames.getOrDefault(a.getPetId(), "Unknown"),
                a.getStaffId() != null ? staffNames.getOrDefault(a.getStaffId(), "Unknown") : "Unassigned",
                serviceNames.getOrDefault(a.getServiceId(), "Unknown"),
                a.getAppointmentDatetime() != null ? displayFormat.format(a.getAppointmentDatetime()) : "",
                a.getStatus()
            });
        }
        view.setRows(rows);
    }

    private void loadCounts(List<Appointment> appointments)
    {
        int today = 0, scheduled = 0, completed = 0, cancelled = 0;
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = dateOnly.format(new java.util.Date());

        for (Appointment a : appointments)
        {
            if (a.getAppointmentDatetime() != null && dateOnly.format(a.getAppointmentDatetime()).equals(todayStr))
            {
                today++;
            }
            if ("Scheduled".equalsIgnoreCase(a.getStatus())) scheduled++;
            else if ("Completed".equalsIgnoreCase(a.getStatus())) completed++;
            else if ("Cancelled".equalsIgnoreCase(a.getStatus())) cancelled++;
        }
        view.setStats(today, scheduled, completed, cancelled);
    }

    // Create an appointment only after validating all required selections and text.
    private void book()
    {
        try
        {
            int customerIndex = view.getCmbCustomer().getSelectedIndex();
            int petIndex = view.getCmbPet().getSelectedIndex();
            int staffIndex = view.getCmbStaff().getSelectedIndex();
            int serviceIndex = view.getCmbService().getSelectedIndex();

            if (customerIndex < 0)
                throw new ValidationException("Customer", "Please select a customer.");
            if (staffIndex < 0)
                throw new ValidationException("Staff", "Please select a staff member.");
            if (serviceIndex < 0)
                throw new ValidationException("Service", "Please select a service.");
            if (filteredPetList.isEmpty() || petIndex < 0 || petIndex >= filteredPetList.size())
                throw new ValidationException("Pet", "This customer has no registered pet selected.");

            String dateText = Validator.requireText(view.getTxtDate().getText(), "Appointment date");
            String timeText = Validator.requireText(view.getTxtTime().getText(), "Appointment time");

            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            parser.setLenient(false);
            java.util.Date parsedDate;
            try
            {
                parsedDate = parser.parse(dateText + " " + timeText);
            }
            catch (ParseException ex)
            {
                throw new ValidationException("Appointment date/time",
                        "Use a valid date and time: YYYY-MM-DD and HH:MM (24-hour).");
            }

            if (parsedDate.before(new java.util.Date()))
            {
                throw new ValidationException("Appointment date/time",
                        "Appointment date and time cannot be in the past.");
            }

            String reason = Validator.maxLength(view.getTxtReason().getText().trim(), 255, "Reason");
            String notes = Validator.maxLength(view.getTxtNotes().getText().trim(), 500, "Notes");

            Appointment appointment = new Appointment();
            appointment.setCustomerId(customerList.get(customerIndex).getCustomerId());
            appointment.setPetId(filteredPetList.get(petIndex).getPetId());
            appointment.setStaffId(staffList.get(staffIndex).getStaffId());
            appointment.setServiceId(serviceList.get(serviceIndex).getServiceId());
            appointment.setAppointmentDatetime(new Timestamp(parsedDate.getTime()));
            appointment.setReason(reason);
            appointment.setNotes(notes);

            appointmentService.bookAppointment(appointment);

            clear();
            view.showMessage("Appointment booked successfully.", true);
            refreshAll();
        }
        catch (AppointmentConflictException | IllegalArgumentException ex)
        {
            view.showMessage(ex.getMessage(), false);
        }
        catch (Exception ex)
        {
            LOG.log(java.util.logging.Level.SEVERE, null, ex);
            view.showMessage("Something went wrong. Check your database connection.", false);
        }
    }

    private Integer selectedId()
    {
        int row = view.getTable().getSelectedRow();
        if (row == -1)
        {
            JOptionPane.showMessageDialog(view, "Select an appointment from the table first.");
            return null;
        }
        return (Integer) view.getTable().getValueAt(row, 0);
    }

    private void updateStatus()
    {
        Integer id = selectedId();
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
        }
    }

    private void cancel()
    {
        Integer id = selectedId();
        if (id == null)
        {
            return;
        }
        if (JOptionPane.showConfirmDialog(view, "Cancel this appointment?",
                "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        {
            appointmentService.updateStatus(id, "Cancelled");
            refreshAll();
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
}

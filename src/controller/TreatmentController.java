package controller;

import exception.ValidationException;
import model.Appointment;
import model.Customer;
import model.Pet;
import model.Staff;
import model.Treatment;
import service.AppointmentService;
import service.CustomerService;
import service.PetService;
import service.StaffService;
import service.TreatmentService;
import util.Validator;
import view.TreatmentForm;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// Treatment screen logic
public class TreatmentController extends BaseController<TreatmentForm>
{
    private final TreatmentService treatmentService = new TreatmentService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final CustomerService customerService = new CustomerService();
    private final PetService petService = new PetService();
    private final StaffService staffService = new StaffService();

    private List<Treatment> treatments = new ArrayList<>();
    private List<Appointment> appointmentChoices = new ArrayList<>();
    private List<Staff> staffList = new ArrayList<>();
    private Map<Integer, Appointment> appointmentById = new HashMap<>();
    private Map<Integer, String> customerNames = new HashMap<>();
    private Map<Integer, String> petNames = new HashMap<>();
    private Map<Integer, String> staffNames = new HashMap<>();
    private int selectedTreatmentId = 0;

    // View plus controller
    public static TreatmentForm open()
    {
        TreatmentForm form = new TreatmentForm();
        new TreatmentController(form);
        return form;
    }

    public TreatmentController(TreatmentForm view)
    {
        super(view);
        attachSidebar("TREATMENTS");
        wireEvents();
        guard(() ->
        {
            loadDropdowns();
            refreshAll();
        });
        clearForm();
    }

    // Wire events
    private void wireEvents()
    {
        view.getBtnAddTreatment().addActionListener(e -> save(true));
        view.getBtnUpdateTreatment().addActionListener(e -> save(false));
        view.getBtnDeleteTreatment().addActionListener(e -> delete());
        view.getBtnClearTreatment().addActionListener(e -> clearForm());

        view.getTblTreatments().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int row = view.getTblTreatments().rowAtPoint(e.getPoint());
                if (row < 0 || row >= treatments.size())
                {
                    return;
                }
                fillForm(treatments.get(row));
                if (e.getClickCount() == 2)
                {
                    PrescriptionController.open(view, treatments.get(row).getTreatmentId(),
                            "Medicines for Treatment #" + treatments.get(row).getTreatmentId());
                }
            }
        });

        focusNext(view.getTxtDiagnosis(), view.getTxtTreatmentDate());
        focusNext(view.getTxtTreatmentDate(), view.getTxtTreatmentDetails());
        enterTo(view.getCmbAppointment(), view.getCmbStaff());
        enterTo(view.getCmbStaff(), view.getTxtDiagnosis());
        enterTo(view.getTxtTreatmentDetails(), view.getJTextArea2());
        enterTo(view.getJTextArea2(), view.getBtnAddTreatment());
    }

    // Enter moves focus
    private void enterTo(javax.swing.JComponent from, javax.swing.JComponent to)
    {
        from.addKeyListener(new java.awt.event.KeyAdapter()
        {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e)
            {
                if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER && !e.isShiftDown())
                {
                    e.consume();
                    to.requestFocusInWindow();
                }
            }
        });
    }

    // Load combos
    private void loadDropdowns()
    {
        customerNames = customerService.getAllCustomers().stream()
                .collect(Collectors.toMap(Customer::getCustomerId, Customer::getFullName, (a, b) -> a));
        petNames = petService.getAllPets().stream()
                .collect(Collectors.toMap(Pet::getPetId, Pet::getPetName, (a, b) -> a));
        staffList = staffService.getAllStaff();
        staffNames = staffList.stream()
                .collect(Collectors.toMap(Staff::getStaffId, Staff::getFullName, (a, b) -> a));

        List<Appointment> all = appointmentService.getAllAppointments();
        appointmentById = all.stream()
                .collect(Collectors.toMap(Appointment::getAppointmentId, a -> a, (a, b) -> a));
        appointmentChoices = all.stream()
                .filter(a -> !"Cancelled".equalsIgnoreCase(a.getStatus()))
                .collect(Collectors.toList());

        view.setAppointmentItems(appointmentChoices.stream().map(this::appointmentLabel).collect(Collectors.toList()));
        view.setStaffItems(staffList.stream().map(Staff::getFullName).collect(Collectors.toList()));
    }

    // Appointment text
    private String appointmentLabel(Appointment a)
    {
        String when = a.getAppointmentDatetime() == null ? ""
                : new SimpleDateFormat("yyyy-MM-dd HH:mm").format(a.getAppointmentDatetime());
        return "#" + a.getAppointmentId() + " - " + customerNames.getOrDefault(a.getCustomerId(), "Unknown")
                + " / " + petNames.getOrDefault(a.getPetId(), "Unknown") + " - " + when;
    }

    // Load table
    private void refreshAll()
    {
        treatments = treatmentService.getAllTreatments();
        List<Object[]> rows = new ArrayList<>();
        for (Treatment t : treatments)
        {
            Appointment a = appointmentById.get(t.getAppointmentId());
            rows.add(new Object[]{
                t.getTreatmentId(),
                a == null ? "#" + t.getAppointmentId() : appointmentLabel(a),
                a == null ? "Unknown" : petNames.getOrDefault(a.getPetId(), "Unknown"),
                t.getDiagnosis(),
                t.getTreatmentDetails(),
                t.getTreatmentDate() == null ? "" : t.getTreatmentDate().toString(),
                t.getStaffId() == null ? "Unassigned" : staffNames.getOrDefault(t.getStaffId(), "Unknown"),
                t.getNotes()
            });
        }
        view.setRows(rows);
        loadCounts();
    }

    // Stat cards
    private void loadCounts()
    {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Predicate<Treatment> hasDate = t -> t.getTreatmentDate() != null;
        long todayCount = treatments.stream().filter(hasDate)
                .filter(t -> t.getTreatmentDate().toLocalDate().equals(today)).count();
        long weekCount = treatments.stream().filter(hasDate)
                .filter(t -> !t.getTreatmentDate().toLocalDate().isBefore(weekStart)
                        && !t.getTreatmentDate().toLocalDate().isAfter(weekStart.plusDays(6))).count();
        long monthCount = treatments.stream().filter(hasDate)
                .filter(t -> YearMonth.from(t.getTreatmentDate().toLocalDate()).equals(YearMonth.from(today))).count();
        view.setStats(treatments.size(), todayCount, weekCount, monthCount);
    }

    // Row to form
    private void fillForm(Treatment t)
    {
        selectedTreatmentId = t.getTreatmentId();
        int ai = -1;
        for (int i = 0; i < appointmentChoices.size(); i++)
        {
            if (appointmentChoices.get(i).getAppointmentId() == t.getAppointmentId())
            {
                ai = i;
                break;
            }
        }
        int si = -1;
        for (int i = 0; i < staffList.size(); i++)
        {
            if (t.getStaffId() != null && staffList.get(i).getStaffId() == t.getStaffId())
            {
                si = i;
                break;
            }
        }
        view.fillForm(ai, si,
                t.getDiagnosis() == null ? "" : t.getDiagnosis(),
                t.getTreatmentDate() == null ? "" : t.getTreatmentDate().toString(),
                t.getTreatmentDetails() == null ? "" : t.getTreatmentDetails(),
                t.getNotes() == null ? "" : t.getNotes());
        showHint();
    }

    // Form to object
    private Treatment readForm()
    {
        Treatment t = new Treatment();
        t.setTreatmentId(selectedTreatmentId);
        int ai = view.getAppointmentIndex();
        if (ai < 0 || ai >= appointmentChoices.size())
        {
            throw new ValidationException("Appointment", "Please select an appointment.");
        }
        t.setAppointmentId(appointmentChoices.get(ai).getAppointmentId());

        int si = view.getStaffIndex();
        t.setStaffId(si < 0 ? null : staffList.get(si).getStaffId());

        t.setDiagnosis(Validator.maxLength(
                Validator.requireText(view.getDiagnosisText(), "Diagnosis"), 255, "Diagnosis"));

        t.setTreatmentDate(Validator.parseDate(view.getDateText(), "Treatment date"));

        if (t.getTreatmentDate().toLocalDate().isAfter(LocalDate.now()))
        {
            throw new ValidationException("Treatment date", "Treatment date cannot be in the future.");
        }

        t.setTreatmentDetails(Validator.maxLength(
                Validator.requireText(view.getDetailsText(), "Treatment details"), 1000, "Treatment details"));

        t.setNotes(Validator.maxLength(view.getNotesText().trim(), 500, "Notes"));

        return t;
    }

    // Add or update
    private void save(boolean isNew)
    {
        guard(() ->
        {
            if (!isNew)
            {
                Validator.requireSelected(selectedTreatmentId, "Treatment");
            }
            Treatment t = readForm();
            if (isNew)
            {
                treatmentService.addTreatment(t);
                markCompleted(t.getAppointmentId());
            }
            else
            {
                treatmentService.updateTreatment(t);
            }
            refreshAll();
            clearForm();
            info(isNew ? "Treatment added successfully." : "Treatment updated successfully.");
        });
    }

    // Complete appointment
    private void markCompleted(int appointmentId)
    {
        Appointment a = appointmentById.get(appointmentId);
        if (a != null && !"Completed".equalsIgnoreCase(a.getStatus()))
        {
            appointmentService.updateStatus(appointmentId, "Completed");
            a.setStatus("Completed");
        }
    }

    private void delete()
    {
        if (selectedTreatmentId <= 0)
        {
            warn("Please select a treatment first.");
            return;
        }
        if (!confirm("Delete this treatment?"))
        {
            return;
        }
        guard(() ->
        {
            treatmentService.deleteTreatment(selectedTreatmentId);
            refreshAll();
            clearForm();
        });
    }

    // Reset form
    private void clearForm()
    {
        selectedTreatmentId = 0;
        view.clearForm(LocalDate.now().toString());
        showHint();
    }

    private void showHint()
    {
        view.setMessage("Double-click a row to record medicines");
    }
}

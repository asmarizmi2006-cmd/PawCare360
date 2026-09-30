/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

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
import exception.AppointmentConflictException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HP
 */
public class AppointmentForm extends javax.swing.JFrame 
{

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AppointmentForm.class.getName());

    private final CustomerService customerService;
    private final PetService petService;
    private final StaffService staffService;
    private final ServiceService serviceService;
    private final AppointmentService appointmentService;

    private Map<Integer, String> customerNames;
    private Map<Integer, String> petNames;
    private Map<Integer, String> staffNames;
    private Map<Integer, String> serviceNames;

    private List<Customer> customerList;
    private List<Pet> petList;          // ALL pets
    private List<Pet> filteredPetList;  // pets belonging to the selected customer
    private List<Staff> staffList;
    private List<ServiceItem> serviceList;

    /**
     * Creates new form AppointmentForm
     */
    public AppointmentForm() 
    {
        initComponents();
        setResizable(false);

        customerService = new CustomerService();
        petService = new PetService();
        staffService = new StaffService();
        serviceService = new ServiceService();
        appointmentService = new AppointmentService();

        setLocationRelativeTo(null);

        loadDropdowns();
        refreshAll();
        setupButtonActions();

        cmbCustomer.addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                int index = cmbCustomer.getSelectedIndex();
                if (index != -1) {
                    Customer selected = customerList.get(index);
                    filterPetsForCustomer(selected.getCustomerId());
                }
            }
        });

        if (!customerList.isEmpty()) {
            filterPetsForCustomer(customerList.get(0).getCustomerId());
        }

        sidebarPanel1.setNavigationListener(this::handleSidebarNavigation);
    }

    private void loadDropdowns() 
    {
        customerNames = new HashMap<>();
        petNames = new HashMap<>();
        staffNames = new HashMap<>();
        serviceNames = new HashMap<>();

        customerList = customerService.getAllCustomers();
        cmbCustomer.removeAllItems();
        for (Customer c : customerList) 
        {
            cmbCustomer.addItem(c.getFullName());
            customerNames.put(c.getCustomerId(), c.getFullName());
        }

        petList = petService.getAllPets();
        for (Pet p : petList) 
        {
            petNames.put(p.getPetId(), p.getPetName());
        }
        // cmbPet is populated dynamically per selected customer — see filterPetsForCustomer()

        staffList = staffService.getAllStaff();
        cmbStaff.removeAllItems();
        for (Staff s : staffList) 
        {
            cmbStaff.addItem(s.getFullName());
            staffNames.put(s.getStaffId(), s.getFullName());
        }

        serviceList = serviceService.getAllServices();
        cmbService.removeAllItems();
        for (ServiceItem svc : serviceList) 
        {
            cmbService.addItem(svc.getServiceName());
            serviceNames.put(svc.getServiceId(), svc.getServiceName());
        }
    }

    private void filterPetsForCustomer(int customerId) 
    {
        filteredPetList = new ArrayList<>();
        cmbPet.removeAllItems();

        for (Pet p : petList) 
        {
            if (p.getCustomerId() == customerId) 
            {
                filteredPetList.add(p);
                cmbPet.addItem(p.getPetName());
            }
        }

        if (filteredPetList.isEmpty()) 
        {
            cmbPet.addItem("No pets for this customer");
        }
    }

    private void refreshAll() 
    {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        loadTable(appointments);
        loadCounts(appointments);
    }

    private void loadTable(List<Appointment> appointments) 
    {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Customer", "Pet", "Staff", "Service", "Date & Time", "Status"}, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Appointment a : appointments) 
        {
            model.addRow(new Object[]{
                a.getAppointmentId(),
                customerNames.getOrDefault(a.getCustomerId(), "Unknown"),
                petNames.getOrDefault(a.getPetId(), "Unknown"),
                a.getStaffId() != null ? staffNames.getOrDefault(a.getStaffId(), "Unknown") : "Unassigned",
                serviceNames.getOrDefault(a.getServiceId(), "Unknown"),
                a.getAppointmentDatetime() != null ? displayFormat.format(a.getAppointmentDatetime()) : "",
                a.getStatus()
            });
        }

        tblAppointments.setModel(model);
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

        lblTodayCount.setText(String.valueOf(today));
        lblScheduledCount.setText(String.valueOf(scheduled));
        lblCompletedCount.setText(String.valueOf(completed));
        lblCancelledCount.setText(String.valueOf(cancelled));
    }

    private void setupButtonActions() 
    {
        btnBook.addActionListener(e -> handleBook());
        btnUpdate.addActionListener(e -> handleUpdateStatus());
        btnCancelAppointment.addActionListener(e -> handleCancel());
        btnClear.addActionListener(e -> clearForm());
    }

    private void handleBook() 
    {
        try 
        {
            int customerIndex = cmbCustomer.getSelectedIndex();
            int petIndex = cmbPet.getSelectedIndex();
            int staffIndex = cmbStaff.getSelectedIndex();
            int serviceIndex = cmbService.getSelectedIndex();

            if (customerIndex == -1 || petIndex == -1 || staffIndex == -1 || serviceIndex == -1) 
            {
                lblMessage.setText("Please make sure Customer, Pet, Staff and Service are all selected.");
                return;
            }

            if (filteredPetList.isEmpty()) 
            {
                lblMessage.setText("This customer has no registered pets.");
                return;
            }

            Customer selectedCustomer = customerList.get(customerIndex);
            Pet selectedPet = filteredPetList.get(petIndex);
            Staff selectedStaff = staffList.get(staffIndex);
            ServiceItem selectedService = serviceList.get(serviceIndex);

            String dateText = txtDate.getText().trim();
            String timeText = txtTime.getText().trim();

            SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            parser.setLenient(false);
            java.util.Date parsedDate;
            try 
            {
                parsedDate = parser.parse(dateText + " " + timeText);
            } 
            catch (ParseException ex) 
            {
                lblMessage.setText("Invalid date/time. Use YYYY-MM-DD and HH:MM (24hr).");
                return;
            }

            Appointment appointment = new Appointment();
            appointment.setCustomerId(selectedCustomer.getCustomerId());
            appointment.setPetId(selectedPet.getPetId());
            appointment.setStaffId(selectedStaff.getStaffId());
            appointment.setServiceId(selectedService.getServiceId());
            appointment.setAppointmentDatetime(new Timestamp(parsedDate.getTime()));
            appointment.setReason(txtReason.getText().trim());
            appointment.setNotes(txtNotes.getText().trim());

            appointmentService.bookAppointment(appointment);

            lblMessage.setForeground(new java.awt.Color(60, 140, 90));
            lblMessage.setText("Appointment booked successfully.");
            clearForm();
            refreshAll();
        } 
        catch (AppointmentConflictException ex) 
        {
            lblMessage.setForeground(new java.awt.Color(200, 60, 60));
            lblMessage.setText(ex.getMessage());
        } 
        catch (IllegalArgumentException ex) 
        {
            lblMessage.setForeground(new java.awt.Color(200, 60, 60));
            lblMessage.setText(ex.getMessage());
        } 
        catch (Exception ex) 
        {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
            lblMessage.setForeground(new java.awt.Color(200, 60, 60));
            lblMessage.setText("Something went wrong. Check your database connection.");
        }
    }

    private void handleUpdateStatus() 
    {
        int row = tblAppointments.getSelectedRow();
        if (row == -1) 
        {
            JOptionPane.showMessageDialog(this, "Select an appointment from the table first.");
            return;
        }

        int appointmentId = (int) tblAppointments.getValueAt(row, 0);
        String[] options = {"Scheduled", "Completed", "Cancelled"};
        String chosen = (String) JOptionPane.showInputDialog(this, "Set status to:",
            "Update Status", JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        if (chosen != null) 
        {
            appointmentService.updateStatus(appointmentId, chosen);
            refreshAll();
        }
    }

    private void handleCancel() 
    {
        int row = tblAppointments.getSelectedRow();
        if (row == -1) 
        {
            JOptionPane.showMessageDialog(this, "Select an appointment from the table first.");
            return;
        }

        int appointmentId = (int) tblAppointments.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Cancel this appointment?",
            "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) 
        {
            appointmentService.updateStatus(appointmentId, "Cancelled");
            refreshAll();
        }
    }

    private void clearForm() 
    {
        if (cmbCustomer.getItemCount() > 0) 
        {
            cmbCustomer.setSelectedIndex(0);
            filterPetsForCustomer(customerList.get(0).getCustomerId());
        }
        if (cmbStaff.getItemCount() > 0) cmbStaff.setSelectedIndex(0);
        if (cmbService.getItemCount() > 0) cmbService.setSelectedIndex(0);
        txtDate.setText("");
        txtTime.setText("");
        txtReason.setText("");
        txtNotes.setText("");
    }

    private void handleSidebarNavigation(String destination) 
    {
        switch (destination) 
        {
            case "Dashboard":
                new DashboardForm().setVisible(true);
                dispose();
                break;
            case "Customers":
                new CustomerForm().setVisible(true);
                dispose();
                break;
            case "Pet Patients":
                new PetForm().setVisible(true);
                dispose();
                break;
            case "Appointments":
                new AppointmentForm().setVisible(true);
                dispose();
                break;
            case "Staff":
                JOptionPane.showMessageDialog(this, "Staff module coming soon.", "Not available yet", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Billing":
                JOptionPane.showMessageDialog(this, "Billing module coming soon.", "Not available yet", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Treatments":
                JOptionPane.showMessageDialog(this, "Treatments module coming soon.", "Not available yet", JOptionPane.INFORMATION_MESSAGE);
                break;
            default:
                JOptionPane.showMessageDialog(this,
                    destination + " module coming soon.",
                    "Not available yet",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // KEEP everything below this line exactly as NetBeans generated it —
    // initComponents(), any btnXActionPerformed stubs, main(), and the
    // "Variables declaration" section that already exist further down in your file.


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebarPanel1 = new view.SidebarPanel();
        pnlAppointmentContent = new javax.swing.JPanel();
        pnlCardToday = new javax.swing.JPanel();
        lblTodayTitle = new javax.swing.JLabel();
        lblTodayCount = new javax.swing.JLabel();
        pnlCardScheduled = new javax.swing.JPanel();
        lblScheduledTitle = new javax.swing.JLabel();
        lblScheduledCount = new javax.swing.JLabel();
        pnlCardCompleted = new javax.swing.JPanel();
        lblCompletedCount = new javax.swing.JLabel();
        lblCompletedTitle = new javax.swing.JLabel();
        pnlCardCancelled = new javax.swing.JPanel();
        lblCancelledCount = new javax.swing.JLabel();
        lblCancelledTitle = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblSubtitle = new javax.swing.JLabel();
        pnlAppointmentDetails = new javax.swing.JPanel();
        lblFormSubtitle = new javax.swing.JLabel();
        lblformTitle = new javax.swing.JLabel();
        lblDateLabel = new javax.swing.JLabel();
        cmbCustomer = new javax.swing.JComboBox<>();
        lblPetLabel = new javax.swing.JLabel();
        cmbPet = new javax.swing.JComboBox<>();
        lblStaffLabel = new javax.swing.JLabel();
        cmbStaff = new javax.swing.JComboBox<>();
        lblServiceLabel = new javax.swing.JLabel();
        cmbService = new javax.swing.JComboBox<>();
        lblCustomerLabel = new javax.swing.JLabel();
        txtDate = new javax.swing.JTextField();
        lblTimeTable = new javax.swing.JLabel();
        txtTime = new javax.swing.JTextField();
        lblReasonLabel = new javax.swing.JLabel();
        txtReason = new javax.swing.JTextField();
        txtNotes = new javax.swing.JTextField();
        lblNotes = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        btnBook = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnCancelAppointment = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        lblRecentsAppointments = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAppointments = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1500, 900));
        setSize(new java.awt.Dimension(1500, 900));

        sidebarPanel1.setMinimumSize(new java.awt.Dimension(250, 900));
        getContentPane().add(sidebarPanel1, java.awt.BorderLayout.WEST);

        pnlAppointmentContent.setBackground(new java.awt.Color(250, 246, 240));
        pnlAppointmentContent.setMinimumSize(new java.awt.Dimension(1200, 900));
        pnlAppointmentContent.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlCardToday.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardToday.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlCardToday.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTodayTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTodayTitle.setForeground(new java.awt.Color(110, 125, 135));
        lblTodayTitle.setText("TODAY");
        pnlCardToday.add(lblTodayTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 190, 18));

        lblTodayCount.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTodayCount.setForeground(new java.awt.Color(74, 91, 106));
        lblTodayCount.setText("0");
        pnlCardToday.add(lblTodayCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 40, 190, 36));

        pnlAppointmentContent.add(pnlCardToday, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 280, 90));

        pnlCardScheduled.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardScheduled.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlCardScheduled.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblScheduledTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblScheduledTitle.setForeground(new java.awt.Color(110, 125, 135));
        lblScheduledTitle.setText("SHEDULED");
        pnlCardScheduled.add(lblScheduledTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 190, 18));

        lblScheduledCount.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblScheduledCount.setForeground(new java.awt.Color(74, 91, 106));
        lblScheduledCount.setText("0");
        pnlCardScheduled.add(lblScheduledCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 40, 190, 36));

        pnlAppointmentContent.add(pnlCardScheduled, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 90, 280, 90));

        pnlCardCompleted.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardCompleted.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlCardCompleted.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCompletedCount.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblCompletedCount.setForeground(new java.awt.Color(74, 91, 106));
        lblCompletedCount.setText("0");
        pnlCardCompleted.add(lblCompletedCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 40, 190, 36));

        lblCompletedTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblCompletedTitle.setForeground(new java.awt.Color(110, 125, 135));
        lblCompletedTitle.setText("COMPLETED");
        pnlCardCompleted.add(lblCompletedTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 190, 18));

        pnlAppointmentContent.add(pnlCardCompleted, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 90, 280, 90));

        pnlCardCancelled.setBackground(new java.awt.Color(255, 255, 255));
        pnlCardCancelled.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlCardCancelled.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCancelledCount.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblCancelledCount.setForeground(new java.awt.Color(74, 91, 106));
        lblCancelledCount.setText("0");
        pnlCardCancelled.add(lblCancelledCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 40, 190, 36));

        lblCancelledTitle.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblCancelledTitle.setForeground(new java.awt.Color(110, 125, 135));
        lblCancelledTitle.setText("CANCELLED");
        pnlCardCancelled.add(lblCancelledTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 12, 190, 18));

        pnlAppointmentContent.add(pnlCardCancelled, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 90, 280, 90));

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblTitle.setText("Appointments");
        pnlAppointmentContent.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 500, 35));

        lblSubtitle.setForeground(new java.awt.Color(110, 125, 135));
        lblSubtitle.setText("Book and manage appointments");
        pnlAppointmentContent.add(lblSubtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 500, 20));

        pnlAppointmentDetails.setBackground(new java.awt.Color(255, 255, 255));
        pnlAppointmentDetails.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(217, 217, 217)));
        pnlAppointmentDetails.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblFormSubtitle.setForeground(new java.awt.Color(110, 125, 135));
        lblFormSubtitle.setText("Register and manage appointment bookings");
        pnlAppointmentDetails.add(lblFormSubtitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 42, 500, 20));

        lblformTitle.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        lblformTitle.setForeground(new java.awt.Color(74, 91, 106));
        lblformTitle.setText("APPOINTMENT BOOKING");
        pnlAppointmentDetails.add(lblformTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 15, 500, 25));

        lblDateLabel.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblDateLabel.setForeground(new java.awt.Color(113, 128, 140));
        lblDateLabel.setText("DATE (YYYY-MM-DD)");
        pnlAppointmentDetails.add(lblDateLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 366, 18));

        cmbCustomer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlAppointmentDetails.add(cmbCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 270, 32));

        lblPetLabel.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblPetLabel.setForeground(new java.awt.Color(113, 128, 140));
        lblPetLabel.setText("PET");
        pnlAppointmentDetails.add(lblPetLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 270, 18));

        cmbPet.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlAppointmentDetails.add(cmbPet, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 110, 270, 32));

        lblStaffLabel.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblStaffLabel.setForeground(new java.awt.Color(113, 128, 140));
        lblStaffLabel.setText("STAFF");
        pnlAppointmentDetails.add(lblStaffLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 90, 270, 18));

        cmbStaff.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlAppointmentDetails.add(cmbStaff, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 110, 270, 32));

        lblServiceLabel.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblServiceLabel.setForeground(new java.awt.Color(113, 128, 140));
        lblServiceLabel.setText("SERVICE");
        pnlAppointmentDetails.add(lblServiceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 90, 270, 18));

        cmbService.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        pnlAppointmentDetails.add(cmbService, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 110, 270, 32));

        lblCustomerLabel.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblCustomerLabel.setForeground(new java.awt.Color(113, 128, 140));
        lblCustomerLabel.setText("CUSTOMER");
        pnlAppointmentDetails.add(lblCustomerLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 270, 18));

        txtDate.addActionListener(this::txtDateActionPerformed);
        pnlAppointmentDetails.add(txtDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 366, 32));

        lblTimeTable.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTimeTable.setForeground(new java.awt.Color(113, 128, 140));
        lblTimeTable.setText("TIME (HH:MM)");
        pnlAppointmentDetails.add(lblTimeTable, new org.netbeans.lib.awtextra.AbsoluteConstraints(406, 160, 366, 18));

        txtTime.addActionListener(this::txtTimeActionPerformed);
        pnlAppointmentDetails.add(txtTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(406, 180, 366, 32));

        lblReasonLabel.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblReasonLabel.setForeground(new java.awt.Color(113, 128, 140));
        lblReasonLabel.setText("REASON");
        pnlAppointmentDetails.add(lblReasonLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(792, 160, 366, 18));

        txtReason.addActionListener(this::txtReasonActionPerformed);
        pnlAppointmentDetails.add(txtReason, new org.netbeans.lib.awtextra.AbsoluteConstraints(792, 180, 366, 32));
        pnlAppointmentDetails.add(txtNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 250, 1140, 70));

        lblNotes.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblNotes.setForeground(new java.awt.Color(113, 128, 140));
        lblNotes.setText("NOTES");
        pnlAppointmentDetails.add(lblNotes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 1140, 18));

        lblMessage.setForeground(java.awt.Color.red);
        pnlAppointmentDetails.add(lblMessage, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 600, 20));

        btnBook.setBackground(new java.awt.Color(157, 201, 163));
        btnBook.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnBook.setForeground(new java.awt.Color(74, 91, 106));
        btnBook.setText("Book Appointment");
        btnBook.addActionListener(this::btnBookActionPerformed);
        pnlAppointmentDetails.add(btnBook, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 355, 270, 45));

        btnUpdate.setBackground(new java.awt.Color(190, 210, 230));
        btnUpdate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnUpdate.setForeground(new java.awt.Color(74, 91, 106));
        btnUpdate.setText("Update Status");
        btnUpdate.addActionListener(this::btnUpdateActionPerformed);
        pnlAppointmentDetails.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 355, 270, 45));

        btnCancelAppointment.setBackground(new java.awt.Color(220, 170, 170));
        btnCancelAppointment.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnCancelAppointment.setForeground(new java.awt.Color(120, 40, 40));
        btnCancelAppointment.setText("Cancel");
        btnCancelAppointment.addActionListener(this::btnCancelAppointmentActionPerformed);
        pnlAppointmentDetails.add(btnCancelAppointment, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 355, 270, 45));

        btnClear.setBackground(new java.awt.Color(235, 235, 230));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnClear.setForeground(new java.awt.Color(74, 91, 106));
        btnClear.setText("Clear");
        btnClear.addActionListener(this::btnClearActionPerformed);
        pnlAppointmentDetails.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 355, 270, 45));

        pnlAppointmentContent.add(pnlAppointmentDetails, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 1180, 420));

        lblRecentsAppointments.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblRecentsAppointments.setForeground(new java.awt.Color(113, 128, 140));
        lblRecentsAppointments.setText("RECENT APPOINTMENTS");
        pnlAppointmentContent.add(lblRecentsAppointments, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 630, 400, 25));

        tblAppointments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Customer", "Pet", "Staff", "Service", "Date & Time", "Status"
            }
        ));
        jScrollPane1.setViewportView(tblAppointments);

        pnlAppointmentContent.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 658, 1180, 160));

        getContentPane().add(pnlAppointmentContent, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtReasonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtReasonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtReasonActionPerformed

    private void txtDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDateActionPerformed

    private void txtTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimeActionPerformed

    private void btnBookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnBookActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnCancelAppointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelAppointmentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelAppointmentActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnClearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AppointmentForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBook;
    private javax.swing.JButton btnCancelAppointment;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbCustomer;
    private javax.swing.JComboBox<String> cmbPet;
    private javax.swing.JComboBox<String> cmbService;
    private javax.swing.JComboBox<String> cmbStaff;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCancelledCount;
    private javax.swing.JLabel lblCancelledTitle;
    private javax.swing.JLabel lblCompletedCount;
    private javax.swing.JLabel lblCompletedTitle;
    private javax.swing.JLabel lblCustomerLabel;
    private javax.swing.JLabel lblDateLabel;
    private javax.swing.JLabel lblFormSubtitle;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblNotes;
    private javax.swing.JLabel lblPetLabel;
    private javax.swing.JLabel lblReasonLabel;
    private javax.swing.JLabel lblRecentsAppointments;
    private javax.swing.JLabel lblScheduledCount;
    private javax.swing.JLabel lblScheduledTitle;
    private javax.swing.JLabel lblServiceLabel;
    private javax.swing.JLabel lblStaffLabel;
    private javax.swing.JLabel lblSubtitle;
    private javax.swing.JLabel lblTimeTable;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTodayCount;
    private javax.swing.JLabel lblTodayTitle;
    private javax.swing.JLabel lblformTitle;
    private javax.swing.JPanel pnlAppointmentContent;
    private javax.swing.JPanel pnlAppointmentDetails;
    private javax.swing.JPanel pnlCardCancelled;
    private javax.swing.JPanel pnlCardCompleted;
    private javax.swing.JPanel pnlCardScheduled;
    private javax.swing.JPanel pnlCardToday;
    private view.SidebarPanel sidebarPanel1;
    private javax.swing.JTable tblAppointments;
    private javax.swing.JTextField txtDate;
    private javax.swing.JTextField txtNotes;
    private javax.swing.JTextField txtReason;
    private javax.swing.JTextField txtTime;
    // End of variables declaration//GEN-END:variables
}

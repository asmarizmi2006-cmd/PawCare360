package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import exception.ValidationException;
import model.Staff;
import service.StaffService;
import util.Validator;
import view.StaffForm;

// Staff screen controller
public class StaffController extends BaseController<StaffForm>
{
    private final StaffService staffService = new StaffService(); // Facade
    private int selectedStaffId = 0; // Selected row

    public StaffController(StaffForm view)
    {
        super(view);
        attachSidebar("STAFF");
        view.getCmbRole().setModel(new DefaultComboBoxModel<>(
                new String[]{"Veterinarian", "Groomer", "Receptionist", "Manager", "Administrator"}));
        view.getCmbStatus().setModel(new DefaultComboBoxModel<>(
                new String[]{"Active", "On Leave", "Inactive"}));
        wire();
        guard(this::loadStaff);
    }

    // Factory entry
    public static StaffForm open()
    {
        StaffForm v = new StaffForm();
        new StaffController(v);
        return v;
    }

    private void wire()
    {
        view.getBtnAdd().addActionListener(e -> guard(this::addStaff));
        view.getBtnUpdate().addActionListener(e -> guard(this::updateStaff));
        view.getBtnDelete().addActionListener(e -> guard(this::deleteStaff));
        view.getBtnClear().addActionListener(e ->
        {
            clearForm();
            view.getTxtFullName().requestFocusInWindow();
        });
        view.getTable().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                guard(StaffController.this::fillFromRow);
            }
        });
        // Enter key chain
        focusNext(view.getTxtFullName(), view.getCmbRole());
        focusNext(view.getTxtPhone(), view.getTxtEmail());
        focusNext(view.getTxtEmail(), view.getTxtSpecialization());
        focusNext(view.getTxtSpecialization(), view.getTxtHireDate());
        view.getTxtHireDate().addActionListener(e -> view.getCmbStatus().requestFocusInWindow());
    }

    private void fillFromRow()
    {
        int r = view.getTable().getSelectedRow();
        if (r < 0)
        {
            return;
        }
        selectedStaffId = Integer.parseInt(view.cellText(r, 0));
        view.fillForm(view.cellText(r, 1), view.cellText(r, 2), view.cellText(r, 3),
                view.cellText(r, 4), view.cellText(r, 5), view.cellText(r, 6), view.cellText(r, 7));
    }

    private void addStaff()
    {
        staffService.addStaff(readStaff());
        info("Staff added successfully!");
        clearForm();
        loadStaff();
    }

    private void updateStaff()
    {
        if (selectedStaffId <= 0)
        {
            throw new ValidationException("Please select a staff member first.");
        }
        staffService.updateStaff(readStaff());
        info("Staff updated successfully!");
        clearForm();
        loadStaff();
    }

    private void deleteStaff()
    {
        if (selectedStaffId <= 0)
        {
            throw new ValidationException("Please select a staff member to delete.");
        }
        if (!confirm("Delete this staff member?"))
        {
            return;
        }
        staffService.deleteStaff(selectedStaffId);
        info("Staff deleted successfully!");
        clearForm();
        loadStaff();
    }

    private void clearForm()
    {
        selectedStaffId = 0;
        view.clearForm();
    }

    // Form to model
    // Build the Staff model and validate every user-entered value before saving.
    private Staff readStaff()
    {
        Staff s = new Staff();
        s.setStaffId(selectedStaffId);

        s.setFullName(Validator.requireName(view.getFullNameText(), "Full Name"));
        s.setRole(Validator.requireText(view.getRoleText(), "Role"));
        s.setPhone(Validator.requirePhone(view.getPhoneText(), "Phone"));
        s.setEmail(Validator.optionalEmail(view.getEmailText(), "Email"));
        s.setSpecialization(Validator.maxLength(view.getSpecializationText().trim(), 100, "Specialization"));

        String hireDate = Validator.requireText(view.getHireDateText(), "Hire date");
        Validator.parseDate(hireDate, "Hire date");
        s.setHireDate(hireDate);

        String status = Validator.requireText(view.getStatusText(), "Status");
        if (!status.equals("Active") && !status.equals("On Leave") && !status.equals("Inactive"))
        {
            throw new ValidationException("Status", "Please select a valid staff status.");
        }
        s.setStatus(status);

        return s;
    }

    private void loadStaff()
    {
        List<Staff> list = staffService.getAllStaff();
        List<Object[]> rows = new ArrayList<>();
        for (Staff s : list)
        {
            rows.add(new Object[]{s.getStaffId(), s.getFullName(), s.getRole(), s.getPhone(),
                s.getEmail(), s.getSpecialization(), s.getHireDate(), s.getStatus()});
        }
        view.setRows(rows);
        view.setStats(list.size(), count(list, "Active"), count(list, "On Leave"), count(list, "Inactive"));
    }

    private long count(List<Staff> list, String status)
    {
        return list.stream().filter(s -> status.equalsIgnoreCase(s.getStatus())).count();
    }
}

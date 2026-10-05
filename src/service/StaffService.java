/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.ValidationException;
import dao.StaffDAO;
import model.Staff;

import java.util.List;

public class StaffService {

    private final StaffDAO staffDAO;

    public StaffService() {
        staffDAO = new StaffDAO();
    }

     // ADD STAFF
    public boolean addStaff(Staff staff) {

        validateStaff(staff);

        return staffDAO.addStaff(staff);
    }

      // GET ALL STAFF
    public List<Staff> getAllStaff() {

        return staffDAO.getAllStaff();
    }

    // UPDATE STAFF
    public boolean updateStaff(Staff staff) {

        if (staff.getStaffId() <= 0) {
            throw new ValidationException(
                    "Invalid staff ID."
            );
        }

        validateStaff(staff);

        return staffDAO.updateStaff(staff);
    }

   // DELETE STAFF
    public boolean deleteStaff(int staffId) {

        if (staffId <= 0) {
            throw new ValidationException(
                    "Invalid staff ID."
            );
        }

        return staffDAO.deleteStaff(staffId);
    }

    // VALIDATE STAFF
    private void validateStaff(Staff staff) {

        if (staff == null) {
            throw new ValidationException(
                    "Staff information cannot be empty."
            );
        }

        // Full name
        if (staff.getFullName() == null
                || staff.getFullName().trim().isEmpty()) {

            throw new ValidationException(
                    "Staff full name is required."
            );
        }

        if (!staff.getFullName().matches(
                "[a-zA-Z .']+")) {

            throw new ValidationException(
                    "Full name can contain only letters, spaces, dots and apostrophes."
            );
        }

        // Role
        if (staff.getRole() == null
                || staff.getRole().trim().isEmpty()) {

            throw new ValidationException(
                    "Staff role is required."
            );
        }

        // Phone - optional
        if (staff.getPhone() != null
                && !staff.getPhone().trim().isEmpty()) {

            if (!staff.getPhone().matches(
                    "^(07\\d{8}|\\+947\\d{8})$")) {

                throw new ValidationException(
                        "Please enter a valid Sri Lankan mobile number."
                );
            }
        }

        // Email - optional
        if (staff.getEmail() != null
                && !staff.getEmail().trim().isEmpty()) {

            if (!staff.getEmail().matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

                throw new ValidationException(
                        "Please enter a valid email address."
                );
            }
        }

        // Status
        if (staff.getStatus() == null
                || staff.getStatus().trim().isEmpty()) {

            staff.setStatus("Active");
        }
    }
}

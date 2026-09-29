/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Staff;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {

    // ADD STAFF
    
    public boolean addStaff(Staff staff) {

        String sql = "INSERT INTO staff "
                + "(full_name, role, phone, email, specialization, hire_date, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, staff.getFullName());
            pst.setString(2, staff.getRole());
            pst.setString(3, staff.getPhone());
            pst.setString(4, staff.getEmail());
            pst.setString(5, staff.getSpecialization());

            // Convert String date to SQL Date
            if (staff.getHireDate() == null
                    || staff.getHireDate().trim().isEmpty()) {

                pst.setNull(6, Types.DATE);

            } else {

                pst.setDate(
                        6,
                        Date.valueOf(staff.getHireDate())
                );
            }

            pst.setString(7, staff.getStatus());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

       // GET ALL STAFF
    
    public List<Staff> getAllStaff() {

        List<Staff> staffList = new ArrayList<>();

        String sql = "SELECT * FROM staff ORDER BY staff_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {

                Staff staff = new Staff();

                staff.setStaffId(
                        rs.getInt("staff_id")
                );

                staff.setFullName(
                        rs.getString("full_name")
                );

                staff.setRole(
                        rs.getString("role")
                );

                staff.setPhone(
                        rs.getString("phone")
                );

                staff.setEmail(
                        rs.getString("email")
                );

                staff.setSpecialization(
                        rs.getString("specialization")
                );

                Date hireDate =
                        rs.getDate("hire_date");

                if (hireDate != null) {

                    staff.setHireDate(
                            hireDate.toString()
                    );

                } else {

                    staff.setHireDate("");
                }

                staff.setStatus(
                        rs.getString("status")
                );

                staffList.add(staff);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return staffList;
    }

    
    // UPDATE STAFF
    
    public boolean updateStaff(Staff staff) {

        String sql = "UPDATE staff SET "
                + "full_name = ?, "
                + "role = ?, "
                + "phone = ?, "
                + "email = ?, "
                + "specialization = ?, "
                + "hire_date = ?, "
                + "status = ? "
                + "WHERE staff_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, staff.getFullName());
            pst.setString(2, staff.getRole());
            pst.setString(3, staff.getPhone());
            pst.setString(4, staff.getEmail());
            pst.setString(5, staff.getSpecialization());

            if (staff.getHireDate() == null
                    || staff.getHireDate().trim().isEmpty()) {

                pst.setNull(6, Types.DATE);

            } else {

                pst.setDate(
                        6,
                        Date.valueOf(staff.getHireDate())
                );
            }

            pst.setString(7, staff.getStatus());

            pst.setInt(
                    8,
                    staff.getStaffId()
            );

            return pst.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

 
    // DELETE STAFF
  
    public boolean deleteStaff(int staffId) {

        String sql =
                "DELETE FROM staff WHERE staff_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, staffId);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}

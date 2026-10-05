package dao;

import model.Staff;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// Staff data access
public class StaffDAO extends BaseDAO
{
    // Row mapper
    private Staff map(ResultSet rs) throws SQLException
    {
        Staff s = new Staff();
        s.setStaffId(rs.getInt("staff_id"));
        s.setFullName(rs.getString("full_name"));
        s.setRole(rs.getString("role"));
        s.setPhone(rs.getString("phone"));
        s.setEmail(rs.getString("email"));
        s.setSpecialization(rs.getString("specialization"));
        Date hireDate = rs.getDate("hire_date");
        s.setHireDate(hireDate != null ? hireDate.toString() : "");
        s.setStatus(rs.getString("status"));
        return s;
    }

    // Blank to null
    private Date toDate(String text)
    {
        return (text == null || text.trim().isEmpty()) ? null : Date.valueOf(text);
    }

    // ADD STAFF
    public boolean addStaff(Staff staff)
    {
        return executeUpdate("INSERT INTO staff (full_name, role, phone, email, specialization, hire_date, status) VALUES (?, ?, ?, ?, ?, ?, ?)",
                "add staff", staff.getFullName(), staff.getRole(), staff.getPhone(), staff.getEmail(),
                staff.getSpecialization(), toDate(staff.getHireDate()), staff.getStatus()) > 0;
    }

    // GET ALL STAFF
    public List<Staff> getAllStaff()
    {
        return queryList("SELECT * FROM staff ORDER BY staff_id", "load staff", this::map);
    }

    // UPDATE STAFF
    public boolean updateStaff(Staff staff)
    {
        return executeUpdate("UPDATE staff SET full_name = ?, role = ?, phone = ?, email = ?, specialization = ?, hire_date = ?, status = ? WHERE staff_id = ?",
                "update staff", staff.getFullName(), staff.getRole(), staff.getPhone(), staff.getEmail(),
                staff.getSpecialization(), toDate(staff.getHireDate()), staff.getStatus(),
                staff.getStaffId()) > 0;
    }

    // DELETE STAFF
    public boolean deleteStaff(int staffId)
    {
        return executeUpdate("DELETE FROM staff WHERE staff_id = ?", "delete staff", staffId) > 0;
    }
}

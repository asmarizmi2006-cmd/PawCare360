package dao;

import model.Appointment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

// Appointment data access
public class AppointmentDAO extends BaseDAO
{
    // Create
    public boolean addAppointment(Appointment a)
    {
        String sql = "INSERT INTO appointments (customer_id, pet_id, staff_id, service_id, appointment_datetime, reason, status, notes) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Integer staff = a.getStaffId();
        return executeUpdate(sql, "save the appointment", a.getCustomerId(), a.getPetId(), staff,
                a.getServiceId(), a.getAppointmentDatetime(), a.getReason(), a.getStatus(), a.getNotes()) > 0;
    }

    // Staff conflict
    public boolean hasConflict(int staffId, Timestamp appointmentDatetime)
    {
        String sql = "SELECT COUNT(*) FROM appointments WHERE staff_id = ? AND appointment_datetime = ? AND status != 'Cancelled'";
        return queryOne(sql, "check appointment conflicts", rs -> rs.getInt(1) > 0,
                staffId, appointmentDatetime).orElse(false);
    }

    // Read all
    public List<Appointment> getAllAppointments()
    {
        return queryList("SELECT * FROM appointments ORDER BY appointment_datetime DESC",
                "load appointments", AppointmentDAO::mapRow);
    }

    // Update status
    public boolean updateStatus(int appointmentId, String status)
    {
        return executeUpdate("UPDATE appointments SET status = ? WHERE appointment_id = ?",
                "update the appointment", status, appointmentId) > 0;
    }

    // Delete
    public boolean deleteAppointment(int appointmentId)
    {
        return executeUpdate("DELETE FROM appointments WHERE appointment_id = ?",
                "delete the appointment", appointmentId) > 0;
    }

    private static Appointment mapRow(ResultSet rs) throws SQLException
    {
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getInt("appointment_id"));
        a.setCustomerId(rs.getInt("customer_id"));
        a.setPetId(rs.getInt("pet_id"));
        int staffId = rs.getInt("staff_id");
        a.setStaffId(rs.wasNull() ? null : staffId);
        a.setServiceId(rs.getInt("service_id"));
        a.setAppointmentDatetime(rs.getTimestamp("appointment_datetime"));
        a.setReason(rs.getString("reason"));
        a.setStatus(rs.getString("status"));
        a.setNotes(rs.getString("notes"));
        return a;
    }
}

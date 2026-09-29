/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Appointment;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO 
{

    // CREATE
    public boolean addAppointment(Appointment appointment) 
    {
        String sql = "INSERT INTO appointments (customer_id, pet_id, staff_id, service_id, appointment_datetime, reason, status, notes) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setInt(1, appointment.getCustomerId());
            statement.setInt(2, appointment.getPetId());
            if (appointment.getStaffId() != null) 
            {
                statement.setInt(3, appointment.getStaffId());
            } 
            else 
            {
                statement.setNull(3, java.sql.Types.INTEGER);
            }
            statement.setInt(4, appointment.getServiceId());
            statement.setTimestamp(5, appointment.getAppointmentDatetime());
            statement.setString(6, appointment.getReason());
            statement.setString(7, appointment.getStatus());
            statement.setString(8, appointment.getNotes());

            return statement.executeUpdate() > 0;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return false;
    }

    // Conflict check: same staff, same exact datetime, not cancelled
    public boolean hasConflict(int staffId, Timestamp appointmentDatetime) 
    {
        String sql = "SELECT COUNT(*) FROM appointments WHERE staff_id = ? AND appointment_datetime = ? AND status != 'Cancelled'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setInt(1, staffId);
            statement.setTimestamp(2, appointmentDatetime);

            try (ResultSet resultSet = statement.executeQuery()) 
            {
                if (resultSet.next()) 
                {
                    return resultSet.getInt(1) > 0;
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return false;
    }

    // READ - all appointments
    public List<Appointment> getAllAppointments() 
    {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT * FROM appointments ORDER BY appointment_datetime DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) 
        {
            while (resultSet.next()) 
            {
                list.add(mapRow(resultSet));
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public boolean updateStatus(int appointmentId, String status) 
    {
        String sql = "UPDATE appointments SET status = ? WHERE appointment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setString(1, status);
            statement.setInt(2, appointmentId);
            return statement.executeUpdate() > 0;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return false;
    }

    // DELETE
    public boolean deleteAppointment(int appointmentId) 
    {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setInt(1, appointmentId);
            return statement.executeUpdate() > 0;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return false;
    }

    private Appointment mapRow(ResultSet resultSet) throws Exception 
    {
        Appointment appointment = new Appointment();
        appointment.setAppointmentId(resultSet.getInt("appointment_id"));
        appointment.setCustomerId(resultSet.getInt("customer_id"));
        appointment.setPetId(resultSet.getInt("pet_id"));
        int staffId = resultSet.getInt("staff_id");
        appointment.setStaffId(resultSet.wasNull() ? null : staffId);
        appointment.setServiceId(resultSet.getInt("service_id"));
        appointment.setAppointmentDatetime(resultSet.getTimestamp("appointment_datetime"));
        appointment.setReason(resultSet.getString("reason"));
        appointment.setStatus(resultSet.getString("status"));
        appointment.setNotes(resultSet.getString("notes"));
        return appointment;
    }
}
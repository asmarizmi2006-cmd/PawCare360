/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.AppointmentDAO;
import exception.AppointmentConflictException;
import exception.ValidationException;
import model.Appointment;

import java.sql.Timestamp;
import java.util.List;

public class AppointmentService 
{

    private final AppointmentDAO appointmentDAO;

    public AppointmentService() 
    {
        appointmentDAO = new AppointmentDAO();
    }

    public boolean bookAppointment(Appointment appointment) throws AppointmentConflictException 
    {
        if (appointment.getCustomerId() <= 0) 
        {
            throw new ValidationException("Please select a customer.");
        }
        if (appointment.getPetId() <= 0) 
        {
            throw new ValidationException("Please select a pet.");
        }
        if (appointment.getStaffId() == null || appointment.getStaffId() <= 0) 
        {
            throw new ValidationException("Please select a staff member.");
        }
        if (appointment.getServiceId() <= 0) 
        {
            throw new ValidationException("Please select a service.");
        }

        Timestamp datetime = appointment.getAppointmentDatetime();
        if (datetime == null) 
        {
            throw new ValidationException("Please select a date and time.");
        }
        if (datetime.before(new Timestamp(System.currentTimeMillis()))) 
        {
            throw new ValidationException("Appointment date/time cannot be in the past.");
        }

        if (appointmentDAO.hasConflict(appointment.getStaffId(), datetime)) 
        {
            throw new AppointmentConflictException(
                "This staff member already has an appointment at that exact date and time. Please choose a different slot.");
        }

        appointment.setStatus("Scheduled");
        return appointmentDAO.addAppointment(appointment);
    }

    public List<Appointment> getAllAppointments() 
    {
        return appointmentDAO.getAllAppointments();
    }

    public boolean updateStatus(int appointmentId, String status) 
    {
        return appointmentDAO.updateStatus(appointmentId, status);
    }

    public boolean deleteAppointment(int appointmentId) 
    {
        return appointmentDAO.deleteAppointment(appointmentId);
    }
}

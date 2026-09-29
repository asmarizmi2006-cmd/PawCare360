/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

public class Appointment 
{
    private int appointmentId;
    private int customerId;
    private int petId;
    private Integer staffId;
    private int serviceId;
    private Timestamp appointmentDatetime;
    private String reason;
    private String status;
    private String notes;

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getPetId() { return petId; }
    public void setPetId(int petId) { this.petId = petId; }

    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public Timestamp getAppointmentDatetime() { return appointmentDatetime; }
    public void setAppointmentDatetime(Timestamp appointmentDatetime) { this.appointmentDatetime = appointmentDatetime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
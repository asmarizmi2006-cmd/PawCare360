/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Staff extends Person
{

    private int staffId;
    private String role;
    private String specialization;
    private String hireDate;

    // Empty constructor
    public Staff() {
    }

    // Full constructor
    public Staff(int staffId, String fullName, String role,
                 String phone, String email, String specialization,
                 String hireDate, String status) {

        this.staffId = staffId;
        setFullName(fullName);
        this.role = role;
        setPhone(phone);
        setEmail(email);
        this.specialization = specialization;
        this.hireDate = hireDate;
        setStatus(status);
    }

    // Getters and Setters

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    @Override
    public String getPersonType()
    {
        return "Staff";
    }
}

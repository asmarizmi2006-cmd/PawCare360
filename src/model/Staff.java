/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Staff {

    private int staffId;
    private String fullName;
    private String role;
    private String phone;
    private String email;
    private String specialization;
    private String hireDate;
    private String status;

    // Empty constructor
    public Staff() {
    }

    // Full constructor
    public Staff(int staffId, String fullName, String role,
                 String phone, String email, String specialization,
                 String hireDate, String status) {

        this.staffId = staffId;
        this.fullName = fullName;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.specialization = specialization;
        this.hireDate = hireDate;
        this.status = status;
    }

    // Getters and Setters

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

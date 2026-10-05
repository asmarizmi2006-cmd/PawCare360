/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Customer extends Person
{

    private int customerId;
    private String address;

    public Customer() 
    {
        
    }

    public Customer(int customerId, String fullName, String phone, String email, String address) 
    {
        this.customerId = customerId;
        setFullName(fullName);
        setPhone(phone);
        setEmail(email);
        this.address = address;
    }

    public int getCustomerId() 
    {
        return customerId;
    }

    public void setCustomerId(int customerId) 
    {
        this.customerId = customerId;
    }

    public String getAddress() 
    {
        return address;
    }

    public void setAddress(String address) 
    {
        this.address = address;
    }

    @Override
    public String getPersonType()
    {
        return "Customer";
    }
}

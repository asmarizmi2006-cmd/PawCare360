/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.CustomerDAO;
import model.Customer;

import java.util.List;

public class CustomerService 
{

    private final CustomerDAO customerDAO;

    public CustomerService() 
    {
        customerDAO = new CustomerDAO();
    }

    // CREATE
    public boolean addCustomer(Customer customer) 
    {

        if (customer.getFullName() == null
                || customer.getFullName().trim().isEmpty()) 
        {
            throw new IllegalArgumentException("Customer name is required.");
        }

        if (customer.getPhone() == null
                || customer.getPhone().trim().isEmpty()) 
        {
            throw new IllegalArgumentException("Phone number is required.");
        }

        return customerDAO.addCustomer(customer);
    }

    // READ
    public List<Customer> getAllCustomers() 
    {
        return customerDAO.getAllCustomers();
    }

    // UPDATE
    public boolean updateCustomer(Customer customer) 
    {

        if (customer.getCustomerId() <= 0) 
        {
            throw new IllegalArgumentException("Invalid customer ID.");
        }

        if (customer.getFullName() == null
                || customer.getFullName().trim().isEmpty()) 
        {
            throw new IllegalArgumentException("Customer name is required.");
        }

        return customerDAO.updateCustomer(customer);
    }

    // DELETE
    public boolean deleteCustomer(int customerId) 
    {

        if (customerId <= 0) 
        {
            throw new IllegalArgumentException("Invalid customer ID.");
        }

        return customerDAO.deleteCustomer(customerId);
    }
}
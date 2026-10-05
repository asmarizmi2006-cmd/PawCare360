/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.CustomerDAO;
import exception.DuplicateRecordException;
import exception.ValidationException;
import util.Validator;
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
        validate(customer);
        if (customerDAO.phoneExists(customer.getPhone(), 0))
        {
            throw new DuplicateRecordException("A customer with this phone already exists.");
        }
        return customerDAO.addCustomer(customer);
    }

    // Shared checks
    private void validate(Customer c)
    {
        c.setFullName(Validator.requireName(c.getFullName(), "Customer name"));
        c.setPhone(Validator.requirePhone(c.getPhone(), "Phone"));
        c.setEmail(Validator.optionalEmail(c.getEmail(), "Email"));
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
            throw new ValidationException("Invalid customer ID.");
        }

        validate(customer);
        if (customerDAO.phoneExists(customer.getPhone(), customer.getCustomerId()))
        {
            throw new DuplicateRecordException("Another customer has this phone.");
        }
        return customerDAO.updateCustomer(customer);
    }

    // DELETE
    public boolean deleteCustomer(int customerId) 
    {

        if (customerId <= 0) 
        {
            throw new ValidationException("Invalid customer ID.");
        }

        return customerDAO.deleteCustomer(customerId);
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Customer;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO 
{

    // CREATE
    public boolean addCustomer(Customer customer) 
    {

        String sql = "INSERT INTO customers "
        + "(full_name, phone, email, address, status) "
        + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {

            statement.setString(1, customer.getFullName());
            statement.setString(2, customer.getPhone());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getAddress());
            statement.setString(5, customer.getStatus());

            return statement.executeUpdate() > 0;

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    // READ
    public List<Customer> getAllCustomers() 
    {

        List<Customer> customers = new ArrayList<>();

        String sql = "SELECT * FROM customers";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) 
        {

            while (resultSet.next()) 
            {

                Customer customer = new Customer();

                customer.setCustomerId(
                        resultSet.getInt("customer_id"));

                customer.setFullName(
                        resultSet.getString("full_name"));

                customer.setPhone(
                        resultSet.getString("phone"));

                customer.setEmail(
                        resultSet.getString("email"));

                customer.setAddress(
                        resultSet.getString("address"));
                
                customer.setStatus(
                        resultSet.getString("status"));

                customers.add(customer);
            }

        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        return customers;
    }

    // UPDATE
    public boolean updateCustomer(Customer customer) 
    {
        String sql = "UPDATE customers SET "
                + "full_name = ?, phone = ?, email = ?, address = ?, status = ? "
                + "WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {

            statement.setString(1, customer.getFullName());
            statement.setString(2, customer.getPhone());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getAddress());
            statement.setString(5, customer.getStatus());
            statement.setInt(6, customer.getCustomerId());

            return statement.executeUpdate() > 0;

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteCustomer(int customerId) 
    {

        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {

            statement.setInt(1, customerId);

            return statement.executeUpdate() > 0;

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
            return false;
        }
    }
}
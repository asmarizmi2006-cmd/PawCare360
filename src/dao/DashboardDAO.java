/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Customer;
import model.Pet;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO 
{

    private int countRows(String tableName) 
    {
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) 
        {
            if (resultSet.next()) return resultSet.getInt(1);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return 0;
    }

    private int countActive(String tableName) 
    {
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE status = 'Active'";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) 
        {
            if (resultSet.next()) return resultSet.getInt(1);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return 0;
    }

    public int countCustomers() { return countRows("customers"); }
    public int countPets() { return countRows("pets"); }
    public int countStaff() { return countRows("staff"); }
    public int countActiveCustomers() { return countActive("customers"); }
    public int countActiveStaff() { return countActive("staff"); }

    public List<Customer> getRecentCustomers(int limit) 
    {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY customer_id DESC LIMIT ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) 
            {
                while (resultSet.next()) 
                {
                    Customer customer = new Customer();
                    customer.setCustomerId(resultSet.getInt("customer_id"));
                    customer.setFullName(resultSet.getString("full_name"));
                    customer.setStatus(resultSet.getString("status"));
                    list.add(customer);
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return list;
    }

    public List<Pet> getRecentPets(int limit) 
    {
        List<Pet> list = new ArrayList<>();
        String sql = "SELECT * FROM pets ORDER BY pet_id DESC LIMIT ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) 
            {
                while (resultSet.next()) 
                {
                    Pet pet = new Pet();
                    pet.setPetName(resultSet.getString("pet_name"));
                    pet.setSpecies(resultSet.getString("species"));
                    list.add(pet);
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return list;
    }
}
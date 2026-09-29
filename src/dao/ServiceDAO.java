/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.ServiceItem;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO 
{

    public List<ServiceItem> getAllServices() 
    {
        List<ServiceItem> list = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE status = 'Active' ORDER BY service_name";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) 
        {
            while (resultSet.next()) 
            {
                ServiceItem service = new ServiceItem();
                service.setServiceId(resultSet.getInt("service_id"));
                service.setServiceName(resultSet.getString("service_name"));
                service.setCategory(resultSet.getString("category"));
                service.setPrice(resultSet.getBigDecimal("price"));
                int duration = resultSet.getInt("duration_minutes");
                service.setDurationMinutes(resultSet.wasNull() ? null : duration);
                service.setDescription(resultSet.getString("description"));
                service.setStatus(resultSet.getString("status"));
                list.add(service);
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return list;
    }
}
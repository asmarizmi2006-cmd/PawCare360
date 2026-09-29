/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.User;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO 
{

    // READ - used for login
    public User getUserByUsername(String username) 
    {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) 
        {
            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) 
            {
                if (resultSet.next()) 
                {
                    User user = new User();
                    user.setUserId(resultSet.getInt("user_id"));
                    int staffId = resultSet.getInt("staff_id");
                    user.setStaffId(resultSet.wasNull() ? null : staffId);
                    user.setUsername(resultSet.getString("username"));
                    user.setPasswordHash(resultSet.getString("password_hash"));
                    user.setRole(resultSet.getString("role"));
                    user.setStatus(resultSet.getString("status"));
                    return user;
                }
            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
}
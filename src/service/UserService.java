/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.UserDAO;
import model.User;

public class UserService 
{

    private final UserDAO userDAO;

    public UserService() 
    {
        userDAO = new UserDAO();
    }

    public User login(String username, String password) 
    {
        if (username == null || username.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        User user = userDAO.getUserByUsername(username);

        if (user == null) 
        {
            return null;
        }
        if (!"Active".equalsIgnoreCase(user.getStatus())) 
        {
            return null;
        }
        if (!user.getPasswordHash().equals(password)) 
        {
            return null;
        }
        return user;
    }
}
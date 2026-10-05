/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import exception.ValidationException;
import dao.UserDAO;
import exception.InvalidRoleException;
import model.User;

// Service layer (
public class UserService 
{
    private final UserDAO userDAO;

    public UserService() 
    {
        userDAO = new UserDAO();
    }

    // Method overloading 
    public User login(String username, String password) throws InvalidRoleException 
    {
        return login(username, password, null);
    }

    public User login(String username, String password, String selectedRole) throws InvalidRoleException 
    {
        if (username == null || username.trim().isEmpty()) 
        {
            throw new ValidationException("Username cannot be empty.");
        }
        if (password == null || password.trim().isEmpty()) 
        {
            throw new ValidationException("Password cannot be empty.");
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
        if (!util.PasswordUtil.matches(password, user.getPasswordHash())) 
        {
            return null;
        }
        // Custom exception 
        if (selectedRole != null && !user.getRole().equalsIgnoreCase(selectedRole)) 
        {
            throw new InvalidRoleException(
                "This account is registered as " + user.getRole() + ", not " + selectedRole + ".");
        }
        return user;
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import model.User;

// Singleton pattern 
public class Session 
{
    private static Session instance;

    private User currentUser;

    private Session() 
    {
        // private constructor 
    }

    public static Session getInstance() 
    {
        if (instance == null) 
        {
            instance = new Session();
        }
        return instance;
    }

    public User getCurrentUser() 
    {
        return currentUser;
    }

    public void setCurrentUser(User user) 
    {
        this.currentUser = user;
    }

    public String getCurrentRole() 
    {
        return currentUser == null ? null : currentUser.getRole();
    }

    // Encapsulation 
    public void clear() 
    {
        currentUser = null;
    }
}
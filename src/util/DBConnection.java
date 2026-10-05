/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// DB connection factory
public class DBConnection 
{
        // Defaults, override in db.properties
        private static String url = "jdbc:mysql://localhost:3306/pawcare360";
        private static String user = "root";
        private static String password = "";

        static 
        {
            Properties props = new Properties();
            try (FileInputStream in = new FileInputStream("db.properties")) 
            {
                props.load(in);
                url = props.getProperty("db.url", url);
                user = props.getProperty("db.user", user);
                password = props.getProperty("db.password", password);
            } 
            catch (IOException e) 
            {
                // Use defaults
            }
        }

        public static Connection getConnection() throws SQLException 
        {
            return DriverManager.getConnection(url, user, password);
        }

        // Quick connectivity check
        public static boolean isAvailable() 
        {
            try (Connection c = getConnection()) 
            {
                return c.isValid(2);
            } 
            catch (SQLException e) 
            {
                return false;
            }
        }
}

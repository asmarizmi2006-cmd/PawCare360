package util;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class PasswordReset {

    public static void main(String[] args) {

        resetPassword("admin", "admin123");
        resetPassword("manager", "manager123");
        resetPassword("vet", "vet123");
        resetPassword("nurse", "nurse123");
        resetPassword("groomer", "groomer123");
        resetPassword("reception", "reception123");

        System.out.println("All passwords updated successfully.");
    }

    private static void resetPassword(String username, String password) {

        String hash = PasswordUtil.hash(password);

        String sql = "UPDATE users SET password_hash = ? WHERE username = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, hash);
            ps.setString(2, username);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println(
                    username + " -> " + password
                );
            } else {
                System.out.println(
                    username + " -> USER NOT FOUND"
                );
            }

        } catch (Exception e) {
            System.out.println(
                "Failed to update " + username
            );
            e.printStackTrace();
        }
    }
}
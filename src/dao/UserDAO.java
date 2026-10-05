package dao;

import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

// User data access
public class UserDAO extends BaseDAO
{
    // Row mapper
    private User map(ResultSet rs) throws SQLException
    {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        int staffId = rs.getInt("staff_id");
        user.setStaffId(rs.wasNull() ? null : staffId);
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setRole(rs.getString("role"));
        user.setStatus(rs.getString("status"));
        return user;
    }

    // Null when missing
    public User getUserByUsername(String username)
    {
        return queryOne("SELECT * FROM users WHERE username = ?", "load user", this::map, username)
                .orElse(null);
    }
}

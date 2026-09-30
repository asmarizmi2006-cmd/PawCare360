package dao;

import model.ServiceItem;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    public void addService(ServiceItem s) {
        String sql = "INSERT INTO services (service_name, category, price, duration_minutes, description, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getServiceName());
            ps.setString(2, s.getCategory());
            ps.setBigDecimal(3, s.getPrice());
            ps.setInt(4, s.getDurationMinutes());
            ps.setString(5, s.getDescription());
            ps.setString(6, s.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ServiceItem> getAllServices() {
        List<ServiceItem> list = new ArrayList<>();
        String sql = "SELECT * FROM services WHERE status = 'Active' ORDER BY service_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ServiceItem> getAllServicesForManagement() {
        List<ServiceItem> list = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY service_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateService(ServiceItem s) {
        String sql = "UPDATE services SET service_name=?, category=?, price=?, duration_minutes=?, description=?, status=? WHERE service_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getServiceName());
            ps.setString(2, s.getCategory());
            ps.setBigDecimal(3, s.getPrice());
            ps.setInt(4, s.getDurationMinutes());
            ps.setString(5, s.getDescription());
            ps.setString(6, s.getStatus());
            ps.setInt(7, s.getServiceId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteService(int serviceId) {
        String sql = "DELETE FROM services WHERE service_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ServiceItem mapRow(ResultSet rs) throws SQLException {
        ServiceItem s = new ServiceItem();
        s.setServiceId(rs.getInt("service_id"));
        s.setServiceName(rs.getString("service_name"));
        s.setCategory(rs.getString("category"));
        s.setPrice(rs.getBigDecimal("price"));
        s.setDurationMinutes(rs.getInt("duration_minutes"));
        s.setDescription(rs.getString("description"));
        s.setStatus(rs.getString("status"));
        return s;
    }
}
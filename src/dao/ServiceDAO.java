package dao;

import model.ServiceItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// Service data access
public class ServiceDAO extends BaseDAO
{
    // Row mapper
    private ServiceItem mapRow(ResultSet rs) throws SQLException
    {
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

    public void addService(ServiceItem s)
    {
        executeUpdate("INSERT INTO services (service_name, category, price, duration_minutes, description, status) VALUES (?, ?, ?, ?, ?, ?)",
                "add service", s.getServiceName(), s.getCategory(), s.getPrice(), s.getDurationMinutes(),
                s.getDescription(), s.getStatus());
    }

    public List<ServiceItem> getAllServices()
    {
        return queryList("SELECT * FROM services WHERE status = 'Active' ORDER BY service_id",
                "load services", this::mapRow);
    }

    public List<ServiceItem> getAllServicesForManagement()
    {
        return queryList("SELECT * FROM services ORDER BY service_id", "load services", this::mapRow);
    }

    public void updateService(ServiceItem s)
    {
        executeUpdate("UPDATE services SET service_name=?, category=?, price=?, duration_minutes=?, description=?, status=? WHERE service_id=?",
                "update service", s.getServiceName(), s.getCategory(), s.getPrice(), s.getDurationMinutes(),
                s.getDescription(), s.getStatus(), s.getServiceId());
    }

    public void deleteService(int serviceId)
    {
        executeUpdate("DELETE FROM services WHERE service_id=?", "delete service", serviceId);
    }
}

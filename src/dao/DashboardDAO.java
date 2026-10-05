package dao;

import model.Customer;
import model.Pet;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Dashboard queries
public class DashboardDAO extends BaseDAO
{
    private int countRows(String tableName)
    {
        return queryOne("SELECT COUNT(*) FROM " + tableName, "count " + tableName,
                rs -> rs.getInt(1)).orElse(0);
    }

    private int countActive(String tableName)
    {
        return queryOne("SELECT COUNT(*) FROM " + tableName + " WHERE status = 'Active'",
                "count " + tableName, rs -> rs.getInt(1)).orElse(0);
    }

    public int countCustomers() { return countRows("customers"); }
    public int countPets() { return countRows("pets"); }
    public int countStaff() { return countRows("staff"); }
    public int countActiveCustomers() { return countActive("customers"); }
    public int countActiveStaff() { return countActive("staff"); }

    public List<Customer> getRecentCustomers(int limit)
    {
        return queryList("SELECT * FROM customers ORDER BY customer_id DESC LIMIT ?", "load customers", rs ->
        {
            Customer customer = new Customer();
            customer.setCustomerId(rs.getInt("customer_id"));
            customer.setFullName(rs.getString("full_name"));
            customer.setStatus(rs.getString("status"));
            return customer;
        }, limit);
    }

    public List<Pet> getRecentPets(int limit)
    {
        return queryList("SELECT * FROM pets ORDER BY pet_id DESC LIMIT ?", "load pets", rs ->
        {
            Pet pet = new Pet();
            pet.setPetName(rs.getString("pet_name"));
            pet.setSpecies(rs.getString("species"));
            return pet;
        }, limit);
    }

    public List<String> getRecentAppointments(int limit)
    {
        String sql = "SELECT a.appointment_datetime, a.status, c.full_name, p.pet_name " +
                     "FROM appointments a " +
                     "JOIN customers c ON a.customer_id = c.customer_id " +
                     "JOIN pets p ON a.pet_id = p.pet_id " +
                     "ORDER BY a.appointment_datetime DESC LIMIT ?";
        return queryList(sql, "load appointments",
                rs -> rs.getString("pet_name") + "  (" + rs.getString("full_name") + ")  -  "
                        + rs.getString("status"), limit);
    }

    public List<String> getRecentStaffNames(int limit)
    {
        return queryList("SELECT full_name, role, status FROM staff ORDER BY staff_id DESC LIMIT ?",
                "load staff",
                rs -> rs.getString("full_name") + "  -  " + rs.getString("role")
                        + "  (" + rs.getString("status") + ")", limit);
    }

    // Counts, oldest first
    public int[] getAppointmentsPerDayLast7()
    {
        int[] counts = new int[7];
        String sql = "SELECT DATE(appointment_datetime) AS d, COUNT(*) AS c " +
                     "FROM appointments " +
                     "WHERE appointment_datetime >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                     "GROUP BY DATE(appointment_datetime)";
        LocalDate today = LocalDate.now();
        queryList(sql, "load appointment counts", rs ->
        {
            LocalDate day = rs.getDate("d").toLocalDate();
            int index = 6 - (int) ChronoUnit.DAYS.between(day, today);
            if (index >= 0 && index < 7)
            {
                counts[index] = rs.getInt("c");
            }
            return index;
        });
        return counts;
    }

    public Map<String, Integer> getPetSpeciesBreakdown()
    {
        Map<String, Integer> map = new LinkedHashMap<>();
        queryList("SELECT species, COUNT(*) AS c FROM pets GROUP BY species ORDER BY c DESC",
                "load species counts", rs ->
        {
            map.put(rs.getString("species"), rs.getInt("c"));
            return 0;
        });
        return map;
    }

    // Money collected
    public java.math.BigDecimal totalCollected()
    {
        return queryOne("SELECT COALESCE(SUM(paid_amount), 0) FROM invoices WHERE status <> 'Cancelled'",
                "load revenue", rs -> rs.getBigDecimal(1)).orElse(java.math.BigDecimal.ZERO);
    }

    // Money still owed
    public java.math.BigDecimal totalOutstanding()
    {
        return queryOne("SELECT COALESCE(SUM(total_amount - paid_amount), 0) FROM invoices WHERE status <> 'Cancelled'",
                "load outstanding", rs -> rs.getBigDecimal(1)).orElse(java.math.BigDecimal.ZERO);
    }
}

package dao;

import model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// Customer data access
public class CustomerDAO extends BaseDAO
{
    // Row mapper
    private Customer map(ResultSet rs) throws SQLException
    {
        Customer c = new Customer();
        c.setCustomerId(rs.getInt("customer_id"));
        c.setFullName(rs.getString("full_name"));
        c.setPhone(rs.getString("phone"));
        c.setEmail(rs.getString("email"));
        c.setAddress(rs.getString("address"));
        c.setStatus(rs.getString("status"));
        return c;
    }

    // CREATE
    public boolean addCustomer(Customer customer)
    {
        return executeUpdate("INSERT INTO customers (full_name, phone, email, address, status) VALUES (?, ?, ?, ?, ?)",
                "add customer", customer.getFullName(), customer.getPhone(), customer.getEmail(),
                customer.getAddress(), customer.getStatus()) > 0;
    }

    // READ
    public List<Customer> getAllCustomers()
    {
        return queryList("SELECT * FROM customers", "load customers", this::map);
    }

    // UPDATE
    public boolean updateCustomer(Customer customer)
    {
        return executeUpdate("UPDATE customers SET full_name = ?, phone = ?, email = ?, address = ?, status = ? WHERE customer_id = ?",
                "update customer", customer.getFullName(), customer.getPhone(), customer.getEmail(),
                customer.getAddress(), customer.getStatus(), customer.getCustomerId()) > 0;
    }

    // DELETE
    public boolean deleteCustomer(int customerId)
    {
        return executeUpdate("DELETE FROM customers WHERE customer_id = ?", "delete customer", customerId) > 0;
    }

    // Duplicate phone check
    public boolean phoneExists(String phone, int excludeId)
    {
        return queryOne("SELECT COUNT(*) FROM customers WHERE phone = ? AND customer_id <> ?",
                "check phone", rs -> rs.getInt(1) > 0, phone, excludeId).orElse(false);
    }
}

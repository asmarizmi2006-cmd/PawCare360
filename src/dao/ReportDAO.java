package dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Report data queries
public class ReportDAO extends BaseDAO
{
    // Row to map
    private Map<String, ?> toMap(ResultSet rs) throws SQLException
    {
        ResultSetMetaData md = rs.getMetaData();
        Map<String, Object> row = new LinkedHashMap<>();
        for (int i = 1; i <= md.getColumnCount(); i++)
        {
            row.put(md.getColumnLabel(i), normalise(rs.getObject(i)));
        }
        return row;
    }

    // Driver type fix
    private Object normalise(Object v)
    {
        if (v instanceof java.time.LocalDateTime)
        {
            return Timestamp.valueOf((java.time.LocalDateTime) v);
        }
        if (v instanceof Double || v instanceof Float)
        {
            return new java.math.BigDecimal(v.toString());
        }
        return v;
    }

    // Invoice header
    public Map<String, ?> invoiceHeader(int invoiceId)
    {
        String sql = "SELECT i.invoice_id, i.invoice_date, i.subtotal, i.discount, i.tax, i.total_amount, "
                + "i.paid_amount, i.status, c.full_name AS customer_name, c.phone AS customer_phone, "
                + "p.pet_name AS pet_name, p.species AS species "
                + "FROM invoices i JOIN customers c ON c.customer_id = i.customer_id "
                + "LEFT JOIN pets p ON p.pet_id = i.pet_id WHERE i.invoice_id = ?";
        return queryOne(sql, "load invoice header", this::toMap, invoiceId).orElse(null);
    }

    // Invoice lines
    public List<Map<String, ?>> invoiceLines(int invoiceId)
    {
        String sql = "SELECT item_type, description, quantity, unit_price, line_total "
                + "FROM invoice_details WHERE invoice_id = ? ORDER BY detail_id";
        return queryList(sql, "load invoice lines", this::toMap, invoiceId);
    }

    // Invoice payments
    public List<Map<String, ?>> invoicePayments(int invoiceId)
    {
        String sql = "SELECT payment_date, amount, payment_method, reference_no "
                + "FROM payments WHERE invoice_id = ? ORDER BY payment_date";
        return queryList(sql, "load invoice payments", this::toMap, invoiceId);
    }

    // Revenue per invoice (4 tables)
    public List<Map<String, ?>> revenue(Timestamp from, Timestamp to)
    {
        String sql = "SELECT i.invoice_id, i.invoice_date, DATE_FORMAT(i.invoice_date, '%Y-%m') AS period, "
                + "c.full_name AS customer_name, COALESCE(p.pet_name, '-') AS pet_name, "
                + "i.total_amount, i.paid_amount, (i.total_amount - i.paid_amount) AS balance, i.status, "
                + "COALESCE(GROUP_CONCAT(DISTINCT pay.payment_method SEPARATOR ', '), '-') AS methods "
                + "FROM invoices i JOIN customers c ON c.customer_id = i.customer_id "
                + "LEFT JOIN pets p ON p.pet_id = i.pet_id "
                + "LEFT JOIN payments pay ON pay.invoice_id = i.invoice_id "
                + "WHERE i.status <> 'Cancelled' AND i.invoice_date BETWEEN ? AND ? "
                + "GROUP BY i.invoice_id, i.invoice_date, c.full_name, p.pet_name, i.total_amount, i.paid_amount, i.status "
                + "ORDER BY i.invoice_date";
        return queryList(sql, "load revenue report", this::toMap, from, to);
    }

    // Service performance (4 tables)
    public List<Map<String, ?>> servicePerformance(Timestamp from, Timestamp to)
    {
        String sql = "SELECT a.appointment_id, a.appointment_datetime, a.status, "
                + "s.service_name, COALESCE(s.category, 'Other') AS category, s.price, "
                + "COALESCE(st.full_name, 'Unassigned') AS staff_name, c.full_name AS customer_name, "
                + "pt.pet_name AS pet_name "
                + "FROM appointments a JOIN services s ON s.service_id = a.service_id "
                + "JOIN customers c ON c.customer_id = a.customer_id "
                + "JOIN pets pt ON pt.pet_id = a.pet_id "
                + "LEFT JOIN staff st ON st.staff_id = a.staff_id "
                + "WHERE a.appointment_datetime BETWEEN ? AND ? "
                + "ORDER BY s.service_name, a.appointment_datetime";
        return queryList(sql, "load service report", this::toMap, from, to);
    }
}

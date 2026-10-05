package dao;

import exception.InsufficientStockException;
import exception.ValidationException;
import model.Invoice;
import model.InvoiceItem;
import model.Payment;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

// Invoice data access
public class InvoiceDAO extends BaseDAO
{
    private static final String SELECT_WITH_NAMES =
            "SELECT i.*, c.full_name AS customer_name, p.pet_name AS pet_name "
            + "FROM invoices i JOIN customers c ON c.customer_id = i.customer_id "
            + "LEFT JOIN pets p ON p.pet_id = i.pet_id ";

    // Row mapper
    private Invoice map(ResultSet rs) throws SQLException
    {
        Invoice inv = new Invoice();
        inv.setInvoiceId(rs.getInt("invoice_id"));
        inv.setCustomerId(rs.getInt("customer_id"));
        int petId = rs.getInt("pet_id");
        inv.setPetId(rs.wasNull() ? null : petId);
        int apptId = rs.getInt("appointment_id");
        inv.setAppointmentId(rs.wasNull() ? null : apptId);
        inv.setInvoiceDate(rs.getTimestamp("invoice_date"));
        inv.setSubtotal(rs.getBigDecimal("subtotal"));
        inv.setDiscount(rs.getBigDecimal("discount"));
        inv.setTax(rs.getBigDecimal("tax"));
        inv.setTotalAmount(rs.getBigDecimal("total_amount"));
        inv.setPaidAmount(rs.getBigDecimal("paid_amount"));
        inv.setStatus(rs.getString("status"));
        inv.setNotes(rs.getString("notes"));
        inv.setCustomerName(rs.getString("customer_name"));
        inv.setPetName(rs.getString("pet_name"));
        return inv;
    }

    private InvoiceItem mapItem(ResultSet rs) throws SQLException
    {
        InvoiceItem it = new InvoiceItem();
        it.setDetailId(rs.getInt("detail_id"));
        it.setInvoiceId(rs.getInt("invoice_id"));
        it.setItemType(rs.getString("item_type"));
        int itemId = rs.getInt("item_id");
        it.setItemId(rs.wasNull() ? null : itemId);
        it.setDescription(rs.getString("description"));
        it.setQuantity(rs.getInt("quantity"));
        it.setUnitPrice(rs.getBigDecimal("unit_price"));
        return it;
    }

    public List<Invoice> getAllInvoices()
    {
        return queryList(SELECT_WITH_NAMES + "ORDER BY i.invoice_id DESC", "load invoices", this::map);
    }

    public Optional<Invoice> findById(int invoiceId)
    {
        return queryOne(SELECT_WITH_NAMES + "WHERE i.invoice_id = ?", "load invoice", this::map, invoiceId);
    }

    public List<InvoiceItem> getItems(int invoiceId)
    {
        return queryList("SELECT * FROM invoice_details WHERE invoice_id = ? ORDER BY detail_id",
                "load invoice items", this::mapItem, invoiceId);
    }

    // Save invoice atomically
    public int createInvoice(Invoice inv, Payment firstPayment)
    {
        return inTransaction("save invoice", con ->
        {
            int id;
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO invoices (customer_id, pet_id, appointment_id, invoice_date, subtotal, discount, tax, total_amount, paid_amount, status, notes) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, 'Unpaid', ?)", Statement.RETURN_GENERATED_KEYS))
            {
                bind(ps, inv.getCustomerId(), inv.getPetId(), inv.getAppointmentId(),
                        new Timestamp(System.currentTimeMillis()), inv.getSubtotal(), inv.getDiscount(),
                        inv.getTax(), inv.getTotalAmount(), inv.getNotes());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys())
                {
                    keys.next();
                    id = keys.getInt(1);
                }
            }

            for (InvoiceItem it : inv.getItems())
            {
                update(con, "INSERT INTO invoice_details (invoice_id, item_type, item_id, description, quantity, unit_price, line_total) VALUES (?, ?, ?, ?, ?, ?, ?)",
                        id, it.getItemType(), it.getItemId(), it.getDescription(), it.getQuantity(),
                        it.getUnitPrice(), it.getLineTotal());

                // Counter medicine sale
                if (InvoiceItem.MEDICINE.equals(it.getItemType()) && it.getItemId() != null)
                {
                    int rows = update(con, "UPDATE medicines SET stock_quantity = stock_quantity - ? WHERE medicine_id = ? AND stock_quantity >= ?",
                            it.getQuantity(), it.getItemId(), it.getQuantity());
                    if (rows == 0)
                    {
                        throw new InsufficientStockException("Not enough stock for " + it.getDescription() + ".");
                    }
                }
            }

            if (firstPayment != null && firstPayment.getAmount().signum() > 0)
            {
                firstPayment.setInvoiceId(id);
                new PaymentDAO().insertPayment(con, firstPayment, inv.getTotalAmount());
            }
            return id;
        });
    }

    // Cancel unpaid invoice
    public void cancelInvoice(int invoiceId)
    {
        inTransaction("cancel invoice", con ->
        {
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT status, paid_amount FROM invoices WHERE invoice_id = ? FOR UPDATE"))
            {
                ps.setInt(1, invoiceId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (!rs.next())
                    {
                        throw new ValidationException("Invoice not found.");
                    }
                    if (Invoice.CANCELLED.equals(rs.getString("status")))
                    {
                        throw new ValidationException("Invoice is already cancelled.");
                    }
                    if (rs.getBigDecimal("paid_amount").compareTo(BigDecimal.ZERO) > 0)
                    {
                        throw new ValidationException("Invoices with payments cannot be cancelled.");
                    }
                }
            }

            // Put medicine stock back
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT item_id, quantity FROM invoice_details WHERE invoice_id = ? AND item_type = 'Medicine' AND item_id IS NOT NULL"))
            {
                ps.setInt(1, invoiceId);
                try (ResultSet rs = ps.executeQuery())
                {
                    while (rs.next())
                    {
                        update(con, "UPDATE medicines SET stock_quantity = stock_quantity + ? WHERE medicine_id = ?",
                                rs.getInt("quantity"), rs.getInt("item_id"));
                    }
                }
            }
            update(con, "UPDATE invoices SET status = 'Cancelled' WHERE invoice_id = ?", invoiceId);
            return null;
        });
    }
}

package dao;

import exception.PaymentException;
import model.Invoice;
import model.Payment;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

// Payment data access
public class PaymentDAO extends BaseDAO
{
    private Payment map(ResultSet rs) throws SQLException
    {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("payment_id"));
        p.setInvoiceId(rs.getInt("invoice_id"));
        p.setPaymentDate(rs.getTimestamp("payment_date"));
        p.setAmount(rs.getBigDecimal("amount"));
        p.setPaymentMethod(rs.getString("payment_method"));
        p.setReferenceNo(rs.getString("reference_no"));
        return p;
    }

    public List<Payment> getByInvoice(int invoiceId)
    {
        return queryList("SELECT * FROM payments WHERE invoice_id = ? ORDER BY payment_id",
                "load payments", this::map, invoiceId);
    }

    // Record payment atomically
    public void addPayment(Payment payment)
    {
        inTransaction("record payment", con ->
        {
            BigDecimal total;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT total_amount, status FROM invoices WHERE invoice_id = ? FOR UPDATE"))
            {
                ps.setInt(1, payment.getInvoiceId());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (!rs.next())
                    {
                        throw new PaymentException("Invoice not found.");
                    }
                    if (Invoice.CANCELLED.equals(rs.getString("status")))
                    {
                        throw new PaymentException("Cancelled invoices cannot be paid.");
                    }
                    total = rs.getBigDecimal("total_amount");
                }
            }
            insertPayment(con, payment, total);
            return null;
        });
    }

    // Used inside transactions
    void insertPayment(Connection con, Payment payment, BigDecimal invoiceTotal) throws SQLException
    {
        BigDecimal alreadyPaid;
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE invoice_id = ?"))
        {
            ps.setInt(1, payment.getInvoiceId());
            try (ResultSet rs = ps.executeQuery())
            {
                rs.next();
                alreadyPaid = rs.getBigDecimal(1);
            }
        }

        BigDecimal newPaid = alreadyPaid.add(payment.getAmount());
        if (newPaid.compareTo(invoiceTotal) > 0)
        {
            throw new PaymentException("Payment exceeds the balance of LKR "
                    + invoiceTotal.subtract(alreadyPaid).setScale(2) + ".");
        }

        update(con, "INSERT INTO payments (invoice_id, payment_date, amount, payment_method, reference_no) VALUES (?, ?, ?, ?, ?)",
                payment.getInvoiceId(), new Timestamp(System.currentTimeMillis()), payment.getAmount(),
                payment.getPaymentMethod(), payment.getReferenceNo());

        String status = newPaid.compareTo(invoiceTotal) >= 0 ? Invoice.PAID
                : (newPaid.signum() > 0 ? Invoice.PARTIAL : Invoice.UNPAID);
        update(con, "UPDATE invoices SET paid_amount = ?, status = ? WHERE invoice_id = ?",
                newPaid, status, payment.getInvoiceId());
    }
}

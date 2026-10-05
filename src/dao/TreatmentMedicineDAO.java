package dao;

import exception.DatabaseException;
import exception.InsufficientStockException;
import model.TreatmentMedicine;
import util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

// Prescription data access
public class TreatmentMedicineDAO extends BaseDAO
{
    // Lines for treatment
    public List<TreatmentMedicine> findByTreatment(int treatmentId)
    {
        return queryList("SELECT tm.id, tm.treatment_id, tm.medicine_id, m.medicine_name, tm.quantity, "
                + "tm.dosage, tm.instructions, tm.unit_price "
                + "FROM treatment_medicines tm JOIN medicines m ON m.medicine_id = tm.medicine_id "
                + "WHERE tm.treatment_id = ? ORDER BY tm.id", "load prescription", rs ->
        {
            TreatmentMedicine t = new TreatmentMedicine();
            t.setId(rs.getInt("id"));
            t.setTreatmentId(rs.getInt("treatment_id"));
            t.setMedicineId(rs.getInt("medicine_id"));
            t.setMedicineName(rs.getString("medicine_name"));
            t.setQuantity(rs.getInt("quantity"));
            t.setDosage(rs.getString("dosage"));
            t.setInstructions(rs.getString("instructions"));
            t.setUnitPrice(rs.getBigDecimal("unit_price"));
            return t;
        }, treatmentId);
    }

    // Insert and reduce stock
    public int add(TreatmentMedicine tm)
    {
        try (Connection con = DBConnection.getConnection())
        {
            boolean auto = con.getAutoCommit();
            con.setAutoCommit(false);
            try
            {
                BigDecimal price;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT unit_price FROM medicines WHERE medicine_id = ?"))
                {
                    ps.setInt(1, tm.getMedicineId());
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (!rs.next())
                        {
                            throw new exception.ValidationException("Medicine", "Selected medicine no longer exists.");
                        }
                        price = rs.getBigDecimal("unit_price");
                    }
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE medicines SET stock_quantity = stock_quantity - ? WHERE medicine_id = ? AND stock_quantity >= ?"))
                {
                    ps.setInt(1, tm.getQuantity());
                    ps.setInt(2, tm.getMedicineId());
                    ps.setInt(3, tm.getQuantity());
                    if (ps.executeUpdate() == 0)
                    {
                        throw new InsufficientStockException("Only " + currentStock(con, tm.getMedicineId()) + " left in stock");
                    }
                }
                int id = 0;
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO treatment_medicines (treatment_id, medicine_id, quantity, dosage, instructions, unit_price) VALUES (?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS))
                {
                    ps.setInt(1, tm.getTreatmentId());
                    ps.setInt(2, tm.getMedicineId());
                    ps.setInt(3, tm.getQuantity());
                    ps.setString(4, tm.getDosage());
                    ps.setString(5, tm.getInstructions());
                    ps.setBigDecimal(6, price);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys())
                    {
                        if (keys.next())
                        {
                            id = keys.getInt(1);
                        }
                    }
                }
                con.commit();
                tm.setId(id);
                tm.setUnitPrice(price);
                return id;
            }
            catch (SQLException | RuntimeException e)
            {
                con.rollback();
                throw e;
            }
            finally
            {
                con.setAutoCommit(auto);
            }
        }
        catch (SQLException e)
        {
            throw DatabaseException.wrap(e, "add medicine to treatment");
        }
    }

    // Delete and restore stock
    public void remove(int id)
    {
        try (Connection con = DBConnection.getConnection())
        {
            boolean auto = con.getAutoCommit();
            con.setAutoCommit(false);
            try
            {
                int medicineId;
                int qty;
                try (PreparedStatement ps = con.prepareStatement(
                        "SELECT medicine_id, quantity FROM treatment_medicines WHERE id = ? FOR UPDATE"))
                {
                    ps.setInt(1, id);
                    try (ResultSet rs = ps.executeQuery())
                    {
                        if (!rs.next())
                        {
                            con.rollback();
                            return;
                        }
                        medicineId = rs.getInt("medicine_id");
                        qty = rs.getInt("quantity");
                    }
                }
                try (PreparedStatement ps = con.prepareStatement("DELETE FROM treatment_medicines WHERE id = ?"))
                {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = con.prepareStatement(
                        "UPDATE medicines SET stock_quantity = stock_quantity + ? WHERE medicine_id = ?"))
                {
                    ps.setInt(1, qty);
                    ps.setInt(2, medicineId);
                    ps.executeUpdate();
                }
                con.commit();
            }
            catch (SQLException | RuntimeException e)
            {
                con.rollback();
                throw e;
            }
            finally
            {
                con.setAutoCommit(auto);
            }
        }
        catch (SQLException e)
        {
            throw DatabaseException.wrap(e, "remove medicine from treatment");
        }
    }

    // Current stock
    private int currentStock(Connection con, int medicineId) throws SQLException
    {
        try (PreparedStatement ps = con.prepareStatement("SELECT stock_quantity FROM medicines WHERE medicine_id = ?"))
        {
            ps.setInt(1, medicineId);
            try (ResultSet rs = ps.executeQuery())
            {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }
}

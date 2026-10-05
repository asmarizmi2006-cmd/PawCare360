package dao;

import model.Medicine;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Medicine data access
public class MedicineDAO extends BaseDAO
{
    // Row mapper
    private Medicine map(ResultSet rs) throws SQLException
    {
        Medicine m = new Medicine();
        m.setMedicineId(rs.getInt("medicine_id"));
        m.setMedicineName(rs.getString("medicine_name"));
        m.setCategory(rs.getString("category"));
        m.setUnitPrice(rs.getBigDecimal("unit_price"));
        m.setStockQuantity(rs.getInt("stock_quantity"));
        m.setStatus(rs.getString("status"));
        return m;
    }

    public List<Medicine> getAllMedicines()
    {
        return queryList("SELECT * FROM medicines ORDER BY medicine_name", "load medicines", this::map);
    }

    public List<Medicine> getActiveMedicines()
    {
        return queryList("SELECT * FROM medicines WHERE status = 'Active' ORDER BY medicine_name",
                "load medicines", this::map);
    }

    public Optional<Medicine> findById(int id)
    {
        return queryOne("SELECT * FROM medicines WHERE medicine_id = ?", "load medicine", this::map, id);
    }

    public boolean addMedicine(Medicine m)
    {
        return executeUpdate("INSERT INTO medicines (medicine_name, category, unit_price, stock_quantity, status) VALUES (?, ?, ?, ?, ?)",
                "add medicine", m.getMedicineName(), m.getCategory(), m.getUnitPrice(), m.getStockQuantity(), m.getStatus()) > 0;
    }

    public boolean updateMedicine(Medicine m)
    {
        return executeUpdate("UPDATE medicines SET medicine_name=?, category=?, unit_price=?, stock_quantity=?, status=? WHERE medicine_id=?",
                "update medicine", m.getMedicineName(), m.getCategory(), m.getUnitPrice(), m.getStockQuantity(),
                m.getStatus(), m.getMedicineId()) > 0;
    }

    public boolean deleteMedicine(int id)
    {
        return executeUpdate("DELETE FROM medicines WHERE medicine_id = ?", "delete medicine", id) > 0;
    }

    // Stock change, negative to reduce
    public boolean adjustStock(int medicineId, int delta)
    {
        return executeUpdate("UPDATE medicines SET stock_quantity = stock_quantity + ? WHERE medicine_id = ? AND stock_quantity + ? >= 0",
                "update stock", delta, medicineId, delta) > 0;
    }
}

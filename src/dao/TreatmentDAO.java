package dao;

import model.Treatment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// Treatment data access
public class TreatmentDAO extends BaseDAO
{
    // Row mapper
    private Treatment mapRow(ResultSet rs) throws SQLException
    {
        Treatment t = new Treatment();
        t.setTreatmentId(rs.getInt("treatment_id"));
        t.setAppointmentId(rs.getInt("appointment_id"));
        int staffId = rs.getInt("staff_id");
        t.setStaffId(rs.wasNull() ? null : staffId);
        t.setDiagnosis(rs.getString("diagnosis"));
        t.setTreatmentDetails(rs.getString("treatment_details"));
        t.setTreatmentDate(rs.getDate("treatment_date"));
        t.setNotes(rs.getString("notes"));
        return t;
    }

    public void addTreatment(Treatment t)
    {
        executeUpdate("INSERT INTO treatments (appointment_id, staff_id, diagnosis, treatment_details, treatment_date, notes) VALUES (?, ?, ?, ?, ?, ?)",
                "add treatment", t.getAppointmentId(), t.getStaffId(), t.getDiagnosis(),
                t.getTreatmentDetails(), t.getTreatmentDate(), t.getNotes());
    }

    public List<Treatment> getAllTreatments()
    {
        return queryList("SELECT * FROM treatments ORDER BY treatment_id DESC", "load treatments", this::mapRow);
    }

    public void updateTreatment(Treatment t)
    {
        executeUpdate("UPDATE treatments SET appointment_id=?, staff_id=?, diagnosis=?, treatment_details=?, treatment_date=?, notes=? WHERE treatment_id=?",
                "update treatment", t.getAppointmentId(), t.getStaffId(), t.getDiagnosis(),
                t.getTreatmentDetails(), t.getTreatmentDate(), t.getNotes(), t.getTreatmentId());
    }

    public void deleteTreatment(int treatmentId)
    {
        executeUpdate("DELETE FROM treatments WHERE treatment_id=?", "delete treatment", treatmentId);
    }
}

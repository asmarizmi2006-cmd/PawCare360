/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.Treatment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO {

    public void addTreatment(Treatment t) {
        String sql = "INSERT INTO treatments (appointment_id, staff_id, diagnosis, treatment_details, treatment_date, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getAppointmentId());
            if (t.getStaffId() != null) {
                ps.setInt(2, t.getStaffId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, t.getDiagnosis());
            ps.setString(4, t.getTreatmentDetails());
            ps.setDate(5, t.getTreatmentDate());
            ps.setString(6, t.getNotes());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Treatment> getAllTreatments() {
        List<Treatment> list = new ArrayList<>();
        String sql = "SELECT * FROM treatments ORDER BY treatment_id DESC";
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

    public void updateTreatment(Treatment t) {
        String sql = "UPDATE treatments SET appointment_id=?, staff_id=?, diagnosis=?, treatment_details=?, treatment_date=?, notes=? WHERE treatment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getAppointmentId());
            if (t.getStaffId() != null) {
                ps.setInt(2, t.getStaffId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setString(3, t.getDiagnosis());
            ps.setString(4, t.getTreatmentDetails());
            ps.setDate(5, t.getTreatmentDate());
            ps.setString(6, t.getNotes());
            ps.setInt(7, t.getTreatmentId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTreatment(int treatmentId) {
        String sql = "DELETE FROM treatments WHERE treatment_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, treatmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Treatment mapRow(ResultSet rs) throws SQLException {
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
}

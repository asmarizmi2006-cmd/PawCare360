/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.BoardingBooking;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardingBookingDAO {

    public void addBooking(BoardingBooking b) {
        String sql = "INSERT INTO boarding_bookings (customer_id, pet_id, room_id, check_in, check_out, number_of_days, total_amount, status, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, b.getCustomerId());
            ps.setInt(2, b.getPetId());
            ps.setInt(3, b.getRoomId());
            ps.setDate(4, b.getCheckIn());
            ps.setDate(5, b.getCheckOut());
            ps.setInt(6, b.getNumberOfDays());
            ps.setBigDecimal(7, b.getTotalAmount());
            ps.setString(8, b.getStatus());
            ps.setString(9, b.getNotes());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BoardingBooking> getAllBookings() {
        List<BoardingBooking> list = new ArrayList<>();
        String sql = "SELECT * FROM boarding_bookings ORDER BY boarding_id DESC";
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

    public void updateStatus(int boardingId, String status) {
        String sql = "UPDATE boarding_bookings SET status=? WHERE boarding_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, boardingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBooking(int boardingId) {
        String sql = "DELETE FROM boarding_bookings WHERE boarding_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, boardingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private BoardingBooking mapRow(ResultSet rs) throws SQLException {
        BoardingBooking b = new BoardingBooking();
        b.setBoardingId(rs.getInt("boarding_id"));
        b.setCustomerId(rs.getInt("customer_id"));
        b.setPetId(rs.getInt("pet_id"));
        b.setRoomId(rs.getInt("room_id"));
        b.setCheckIn(rs.getDate("check_in"));
        b.setCheckOut(rs.getDate("check_out"));
        b.setNumberOfDays(rs.getInt("number_of_days"));
        b.setTotalAmount(rs.getBigDecimal("total_amount"));
        b.setStatus(rs.getString("status"));
        b.setNotes(rs.getString("notes"));
        return b;
    }
}
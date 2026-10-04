/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.BoardingRoom;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardingRoomDAO {

    public List<BoardingRoom> getAllRooms() {
        List<BoardingRoom> list = new ArrayList<>();
        String sql = "SELECT * FROM boarding_rooms ORDER BY room_id";
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

    public List<BoardingRoom> getAvailableRooms() {
        List<BoardingRoom> list = new ArrayList<>();
        String sql = "SELECT * FROM boarding_rooms WHERE status = 'Available' ORDER BY room_id";
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

    public void updateRoomStatus(int roomId, String status) {
        String sql = "UPDATE boarding_rooms SET status=? WHERE room_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, roomId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private BoardingRoom mapRow(ResultSet rs) throws SQLException {
        BoardingRoom r = new BoardingRoom();
        r.setRoomId(rs.getInt("room_id"));
        r.setRoomNumber(rs.getString("room_number"));
        r.setRoomType(rs.getString("room_type"));
        r.setPricePerDay(rs.getBigDecimal("price_per_day"));
        r.setStatus(rs.getString("status"));
        r.setDescription(rs.getString("description"));
        return r;
    }
}
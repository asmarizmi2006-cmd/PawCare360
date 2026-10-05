package dao;

import model.BoardingRoom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Room data
public class BoardingRoomDAO extends BaseDAO
{
    public List<BoardingRoom> getAllRooms()
    {
        return queryList("SELECT * FROM boarding_rooms ORDER BY room_id",
                "load boarding rooms", BoardingRoomDAO::mapRow);
    }

    public List<BoardingRoom> getAvailableRooms()
    {
        return queryList("SELECT * FROM boarding_rooms WHERE status = 'Available' ORDER BY room_id",
                "load available rooms", BoardingRoomDAO::mapRow);
    }

    public Optional<BoardingRoom> getRoomById(int roomId)
    {
        return queryOne("SELECT * FROM boarding_rooms WHERE room_id = ?",
                "load the boarding room", BoardingRoomDAO::mapRow, roomId);
    }

    public void updateRoomStatus(int roomId, String status)
    {
        executeUpdate("UPDATE boarding_rooms SET status = ? WHERE room_id = ?",
                "update the room status", status, roomId);
    }

    private static BoardingRoom mapRow(ResultSet rs) throws SQLException
    {
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

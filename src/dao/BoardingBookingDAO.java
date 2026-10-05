package dao;

import exception.BoardingConflictException;
import exception.DatabaseException;
import exception.ValidationException;
import model.BoardingBooking;
import util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Booking data
public class BoardingBookingDAO extends BaseDAO
{
    private static final String INSERT_SQL =
        "INSERT INTO boarding_bookings (customer_id, pet_id, room_id, check_in, check_out, "
        + "number_of_days, total_amount, status, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String OVERLAP_SQL =
        "SELECT COUNT(*) FROM boarding_bookings WHERE pet_id = ? AND boarding_id <> ? "
        + "AND status NOT IN ('Cancelled', 'Checked Out') AND check_in < ? AND check_out > ?";

    // Plain insert
    public int addBooking(BoardingBooking b)
    {
        return insertReturningId(INSERT_SQL, "save the boarding booking",
                b.getCustomerId(), b.getPetId(), b.getRoomId(), b.getCheckIn(), b.getCheckOut(),
                b.getNumberOfDays(), b.getTotalAmount(), b.getStatus(), b.getNotes());
    }

    // Insert, occupy
    public int addBookingAndOccupyRoom(BoardingBooking b)
    {
        return inTransaction("book the room", con ->
        {
            try (PreparedStatement ps = con.prepareStatement("SELECT status FROM boarding_rooms WHERE room_id = ? FOR UPDATE"))
            {
                ps.setInt(1, b.getRoomId());
                try (ResultSet rs = ps.executeQuery())
                {
                    if (!rs.next())
                    {
                        throw new ValidationException("Room", "The selected room does not exist.");
                    }
                    if (!"Available".equalsIgnoreCase(rs.getString(1)))
                    {
                        throw new ValidationException("Room", "That room is no longer available. Please choose another room.");
                    }
                }
            }
            if (overlapExists(con, b.getPetId(), 0, b.getCheckIn(), b.getCheckOut()))
            {
                throw new BoardingConflictException("This pet already has a boarding booking that overlaps these dates.");
            }
            int id;
            try (PreparedStatement ps = con.prepareStatement(INSERT_SQL, java.sql.Statement.RETURN_GENERATED_KEYS))
            {
                bind(ps, b.getCustomerId(), b.getPetId(), b.getRoomId(), b.getCheckIn(), b.getCheckOut(),
                        b.getNumberOfDays(), b.getTotalAmount(), b.getStatus(), b.getNotes());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys())
                {
                    id = keys.next() ? keys.getInt(1) : 0;
                }
            }
            setRoomStatus(con, b.getRoomId(), "Occupied");
            return id;
        });
    }

    // Overlap check
    public boolean hasOverlap(int petId, int excludeId, Date checkIn, Date checkOut)
    {
        return queryOne(OVERLAP_SQL, "check booking overlap", rs -> rs.getInt(1) > 0,
                petId, excludeId, checkOut, checkIn).orElse(false);
    }

    public List<BoardingBooking> getAllBookings()
    {
        return queryList("SELECT * FROM boarding_bookings ORDER BY boarding_id DESC",
                "load boarding bookings", BoardingBookingDAO::mapRow);
    }

    public Optional<BoardingBooking> getBookingById(int boardingId)
    {
        return queryOne("SELECT * FROM boarding_bookings WHERE boarding_id = ?",
                "load the boarding booking", BoardingBookingDAO::mapRow, boardingId);
    }

    // Status only
    public void updateStatus(int boardingId, String status)
    {
        executeUpdate("UPDATE boarding_bookings SET status = ? WHERE boarding_id = ?",
                "update the booking status", status, boardingId);
    }

    // Status, room
    public void updateStatusAndRoom(int boardingId, String status)
    {
        inTransaction("update the booking status", con ->
        {
            int roomId = -1;
            boolean wasActive = false;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT room_id, status FROM boarding_bookings WHERE boarding_id = ? FOR UPDATE"))
            {
                ps.setInt(1, boardingId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (!rs.next())
                    {
                        throw new ValidationException("The booking no longer exists.");
                    }
                    roomId = rs.getInt(1);
                    wasActive = isActive(rs.getString(2));
                }
            }
            try (PreparedStatement ps = con.prepareStatement("UPDATE boarding_bookings SET status = ? WHERE boarding_id = ?"))
            {
                bind(ps, status, boardingId);
                ps.executeUpdate();
            }
            if (wasActive && !isActive(status))
            {
                setRoomStatus(con, roomId, "Available");
            }
            else if (!wasActive && isActive(status))
            {
                setRoomStatus(con, roomId, "Occupied");
            }
            return null;
        });
    }

    // Delete, free
    public void deleteBooking(int boardingId)
    {
        inTransaction("delete the booking", con ->
        {
            int roomId;
            boolean active;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT room_id, status FROM boarding_bookings WHERE boarding_id = ? FOR UPDATE"))
            {
                ps.setInt(1, boardingId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (!rs.next())
                    {
                        return null;
                    }
                    roomId = rs.getInt(1);
                    active = isActive(rs.getString(2));
                }
            }
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM boarding_bookings WHERE boarding_id = ?"))
            {
                bind(ps, boardingId);
                ps.executeUpdate();
            }
            if (active)
            {
                setRoomStatus(con, roomId, "Available");
            }
            return null;
        });
    }

    // Active check
    public static boolean isActive(String status)
    {
        return !"Cancelled".equalsIgnoreCase(status) && !"Checked Out".equalsIgnoreCase(status);
    }

    private static boolean overlapExists(Connection con, int petId, int excludeId, Date in, Date out) throws SQLException
    {
        try (PreparedStatement ps = con.prepareStatement(OVERLAP_SQL))
        {
            bind(ps, petId, excludeId, out, in);
            try (ResultSet rs = ps.executeQuery())
            {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private static void setRoomStatus(Connection con, int roomId, String status) throws SQLException
    {
        try (PreparedStatement ps = con.prepareStatement("UPDATE boarding_rooms SET status = ? WHERE room_id = ?"))
        {
            bind(ps, status, roomId);
            ps.executeUpdate();
        }
    }

    private static BoardingBooking mapRow(ResultSet rs) throws SQLException
    {
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

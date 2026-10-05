package service;

import dao.BoardingBookingDAO;
import exception.BoardingConflictException;
import exception.ValidationException;
import model.BoardingBooking;
import model.BoardingRoom;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BoardingBookingService
{
    private final BoardingBookingDAO bookingDAO = new BoardingBookingDAO();
    private final BoardingRoomService roomService = new BoardingRoomService();

    // Days between dates
    public static int calculateDays(Date checkIn, Date checkOut)
    {
        return (int) ChronoUnit.DAYS.between(checkIn.toLocalDate(), checkOut.toLocalDate());
    }

    // Total amount
    public static BigDecimal calculateTotal(BoardingRoom room, Date checkIn, Date checkOut)
    {
        return room.getPricePerDay().multiply(BigDecimal.valueOf(calculateDays(checkIn, checkOut)))
                .setScale(2, java.math.RoundingMode.HALF_UP);
    }

    // Validate, book
    public void bookRoom(BoardingBooking b)
    {
        validate(b);
        BoardingRoom room = roomService.getRoomById(b.getRoomId())
                .orElseThrow(() -> new ValidationException("Room", "The selected room does not exist."));
        if (!"Available".equalsIgnoreCase(room.getStatus()))
        {
            throw new ValidationException("Room", "That room is not available.");
        }
        b.setNumberOfDays(calculateDays(b.getCheckIn(), b.getCheckOut()));
        b.setTotalAmount(calculateTotal(room, b.getCheckIn(), b.getCheckOut()));
        if (b.getStatus() == null || b.getStatus().isBlank())
        {
            b.setStatus("Booked");
        }
        if (!BoardingBookingDAO.isActive(b.getStatus()))
        {
            throw new ValidationException("Status", "A new booking must be Booked or Checked In.");
        }
        if (bookingDAO.hasOverlap(b.getPetId(), 0, b.getCheckIn(), b.getCheckOut()))
        {
            throw new BoardingConflictException("This pet already has a boarding booking that overlaps these dates.");
        }
        bookingDAO.addBookingAndOccupyRoom(b);
    }

    public List<BoardingBooking> getAllBookings()
    {
        return bookingDAO.getAllBookings();
    }

    public void updateStatus(int boardingId, String status)
    {
        bookingDAO.updateStatusAndRoom(boardingId, status);
    }

    public void checkOut(int boardingId, int roomId)
    {
        BoardingBooking b = bookingDAO.getBookingById(boardingId)
                .orElseThrow(() -> new ValidationException("The booking no longer exists."));
        if (!BoardingBookingDAO.isActive(b.getStatus()))
        {
            throw new ValidationException("This booking is already " + b.getStatus() + ".");
        }
        bookingDAO.updateStatusAndRoom(boardingId, "Checked Out");
    }

    public void deleteBooking(int boardingId)
    {
        bookingDAO.deleteBooking(boardingId);
    }

    private void validate(BoardingBooking b)
    {
        if (b.getCustomerId() <= 0)
        {
            throw new ValidationException("Customer", "Please select a customer.");
        }
        if (b.getPetId() <= 0)
        {
            throw new ValidationException("Pet", "Please select a pet.");
        }
        if (b.getRoomId() <= 0)
        {
            throw new ValidationException("Room", "Please select a room.");
        }
        if (b.getCheckIn() == null || b.getCheckOut() == null)
        {
            throw new ValidationException("Dates", "Check-in and check-out dates are required.");
        }
        if (b.getCheckIn().toLocalDate().isBefore(LocalDate.now()))
        {
            throw new ValidationException("Check-in", "Check-in date cannot be in the past.");
        }
        if (!b.getCheckOut().after(b.getCheckIn()))
        {
            throw new ValidationException("Check-out", "Check-out date must be after check-in date.");
        }
    }
}

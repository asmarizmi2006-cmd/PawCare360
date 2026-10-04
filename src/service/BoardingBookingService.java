/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.BoardingBookingDAO;
import model.BoardingBooking;

import java.util.List;

public class BoardingBookingService {

    private final BoardingBookingDAO bookingDAO = new BoardingBookingDAO();
    private final BoardingRoomService roomService = new BoardingRoomService();

    public void bookRoom(BoardingBooking b) {
        validate(b);
        bookingDAO.addBooking(b);
        roomService.updateRoomStatus(b.getRoomId(), "Occupied");
    }

    public List<BoardingBooking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }

    public void updateStatus(int boardingId, String status) {
        bookingDAO.updateStatus(boardingId, status);
    }

    public void checkOut(int boardingId, int roomId) {
        bookingDAO.updateStatus(boardingId, "Checked Out");
        roomService.updateRoomStatus(roomId, "Available");
    }

    public void deleteBooking(int boardingId) {
        bookingDAO.deleteBooking(boardingId);
    }

    private void validate(BoardingBooking b) {
        if (b.getCustomerId() <= 0) {
            throw new IllegalArgumentException("Please select a customer.");
        }
        if (b.getPetId() <= 0) {
            throw new IllegalArgumentException("Please select a pet.");
        }
        if (b.getRoomId() <= 0) {
            throw new IllegalArgumentException("Please select a room.");
        }
        if (b.getCheckIn() == null || b.getCheckOut() == null) {
            throw new IllegalArgumentException("Check-in and check-out dates are required.");
        }
        if (!b.getCheckOut().after(b.getCheckIn())) {
            throw new IllegalArgumentException("Check-out date must be after check-in date.");
        }
        if (b.getNumberOfDays() <= 0) {
            throw new IllegalArgumentException("Number of days must be greater than 0.");
        }
    }
}

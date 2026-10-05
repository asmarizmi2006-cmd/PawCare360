package service;

import dao.BoardingRoomDAO;
import model.BoardingRoom;

import java.util.List;
import java.util.Optional;

public class BoardingRoomService
{
    private final BoardingRoomDAO roomDAO = new BoardingRoomDAO();

    public List<BoardingRoom> getAllRooms()
    {
        return roomDAO.getAllRooms();
    }

    public List<BoardingRoom> getAvailableRooms()
    {
        return roomDAO.getAvailableRooms();
    }

    public Optional<BoardingRoom> getRoomById(int roomId)
    {
        return roomDAO.getRoomById(roomId);
    }

    public void updateRoomStatus(int roomId, String status)
    {
        roomDAO.updateRoomStatus(roomId, status);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.BoardingRoomDAO;
import model.BoardingRoom;

import java.util.List;

public class BoardingRoomService {

    private final BoardingRoomDAO roomDAO = new BoardingRoomDAO();

    public List<BoardingRoom> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public List<BoardingRoom> getAvailableRooms() {
        return roomDAO.getAvailableRooms();
    }

    public void updateRoomStatus(int roomId, String status) {
        roomDAO.updateRoomStatus(roomId, status);
    }
}
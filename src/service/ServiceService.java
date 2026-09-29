/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.ServiceDAO;
import model.ServiceItem;

import java.util.List;

public class ServiceService 
{

    private final ServiceDAO serviceDAO;

    public ServiceService() 
    {
        serviceDAO = new ServiceDAO();
    }

    public List<ServiceItem> getAllServices() 
    {
        return serviceDAO.getAllServices();
    }
}

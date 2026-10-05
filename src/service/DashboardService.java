/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.DashboardDAO;
import model.Customer;
import model.Pet;

import java.util.List;

public class DashboardService 
{

    private final DashboardDAO dashboardDAO;

    public DashboardService() 
    {
        dashboardDAO = new DashboardDAO();
    }

    public int getCustomerCount() { return dashboardDAO.countCustomers(); }
    public int getPetCount() { return dashboardDAO.countPets(); }
    public int getStaffCount() { return dashboardDAO.countStaff(); }
    public int getActiveCustomerCount() { return dashboardDAO.countActiveCustomers(); }
    public int getActiveStaffCount() { return dashboardDAO.countActiveStaff(); }
    public List<String> getRecentAppointments() { return dashboardDAO.getRecentAppointments(4); }
    public List<String> getRecentStaff() { return dashboardDAO.getRecentStaffNames(4); }
    public int[] getAppointmentsPerDayLast7() { return dashboardDAO.getAppointmentsPerDayLast7(); }
    public java.util.Map<String, Integer> getPetSpeciesBreakdown() { return dashboardDAO.getPetSpeciesBreakdown(); }

    public List<Customer> getRecentCustomers() { return dashboardDAO.getRecentCustomers(4); }
    public List<Pet> getRecentPets() { return dashboardDAO.getRecentPets(4); }

    // Billing totals
    public java.math.BigDecimal getRevenue() { return dashboardDAO.totalCollected(); }
    public java.math.BigDecimal getOutstanding() { return dashboardDAO.totalOutstanding(); }
}

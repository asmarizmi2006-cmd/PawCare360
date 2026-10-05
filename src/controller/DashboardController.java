package controller;

import model.Customer;
import model.Pet;
import service.DashboardService;
import util.Session;
import view.DashboardForm;

import javax.swing.Timer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

// Dashboard screen logic
public class DashboardController extends BaseController<DashboardForm>
{
    private final DashboardService dashboardService = new DashboardService();

    public static DashboardForm open()
    {
        return new DashboardController(new DashboardForm()).getView();
    }

    private DashboardController(DashboardForm view)
    {
        super(view);
        attachSidebar("DASHBOARD");
        wire();
        showUser();
        guard(this::loadCounts);
        guard(this::loadRecentLists);
        guard(this::loadCharts);
    }

    // Quick actions
    private void wire()
    {
        view.getBtnNewAppointment().addActionListener(e -> NavigationController.navigate(view, "APPOINTMENTS"));
        view.getBtnRegisterCustomer().addActionListener(e -> NavigationController.navigate(view, "CUSTOMERS"));
        view.getBtnAddPet().addActionListener(e -> NavigationController.navigate(view, "PET PATIENTS"));
        view.getBtnRecordPayment().addActionListener(e -> NavigationController.navigate(view, "BILLING"));
    }

    private void showUser()
    {
        Session session = Session.getInstance();
        String name = session.getCurrentUser() != null ? session.getCurrentUser().getUsername() : "Guest";
        view.setUserInfo("Welcome back, " + name + "  ·  " + session.getCurrentRole());
    }

    private void loadCounts()
    {
        animateCount(0, dashboardService.getCustomerCount());
        animateCount(1, dashboardService.getPetCount());
        animateCount(2, dashboardService.getStaffCount());
        showMoney();
    }

    // Billing figures
    private void showMoney()
    {
        DecimalFormat money = new DecimalFormat("#,##0.00");
        try
        {
            view.setStat(3, money.format(dashboardService.getRevenue()));
            view.setStat(4, money.format(dashboardService.getOutstanding()));
        }
        catch (exception.DatabaseException e)
        {
            view.setStat(3, "-");
            view.setStat(4, "-");
        }
    }

    // Count-up animation
    private void animateCount(int index, int target)
    {
        final int[] current = {0};
        Timer timer = new Timer(25, null);
        timer.addActionListener(e ->
        {
            current[0] += Math.max(1, target / 20);
            if (current[0] >= target)
            {
                current[0] = target;
                ((Timer) e.getSource()).stop();
            }
            view.setStat(index, String.valueOf(current[0]));
        });
        timer.start();
    }

    private void loadRecentLists()
    {
        List<String> customers = new ArrayList<>();
        for (Customer c : dashboardService.getRecentCustomers())
        {
            customers.add(c.getFullName() + "  (" + c.getStatus() + ")");
        }
        view.setRecentCustomers(customers);

        List<String> pets = new ArrayList<>();
        for (Pet p : dashboardService.getRecentPets())
        {
            pets.add(p.getPetName() + "  -  " + p.getSpecies());
        }
        view.setRecentPets(pets);

        view.setRecentAppointments(dashboardService.getRecentAppointments());
        view.setRecentStaff(dashboardService.getRecentStaff());
    }

    private void loadCharts()
    {
        view.setBarData(dashboardService.getAppointmentsPerDayLast7());
        view.setDonutData(dashboardService.getPetSpeciesBreakdown());
    }
}

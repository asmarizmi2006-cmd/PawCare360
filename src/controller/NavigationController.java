package controller;

import util.AccessControl;
import util.Session;
import view.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

// Factory + controller for screens
public final class NavigationController
{
    // Screen name to new form
    private static final Map<String, Supplier<JFrame>> SCREENS = new LinkedHashMap<>();

    // Screen name to form class
    private static final Map<String, Class<? extends JFrame>> TYPES = new LinkedHashMap<>();

    static
    {
        register("DASHBOARD", DashboardForm.class, DashboardController::open);
        register("CUSTOMERS", CustomerForm.class, CustomerController::open);
        register("PET PATIENTS", PetForm.class, PetController::open);
        register("STAFF", StaffForm.class, StaffController::open);
        register("SERVICES", ServiceForm.class, ServiceController::open);
        register("APPOINTMENTS", AppointmentForm.class, AppointmentController::open);
        register("TREATMENTS", TreatmentForm.class, TreatmentController::open);
        register("GROOMING", GroomingForm.class, GroomingController::open);
        register("BOARDING", BoardingForm.class, BoardingController::open);
        register("BILLING", BillingForm.class, BillingController::open);
        register("REPORTS", ReportsForm.class, ReportsController::open);
    }

    private NavigationController()
    {
    }

    private static void register(String name, Class<? extends JFrame> type, Supplier<JFrame> factory)
    {
        SCREENS.put(name, factory);
        TYPES.put(name, type);
    }

    // Sidebar click handler
    public static void navigate(JFrame current, String screen)
    {
        if ("LOGOUT".equals(screen))
        {
            logout(current);
            return;
        }

        // Already here
        if (TYPES.get(screen) == current.getClass())
        {
            return;
        }

        // Role check
        String role = Session.getInstance().getCurrentRole();
        if (!AccessControl.canAccess(role, screen))
        {
            JOptionPane.showMessageDialog(current,
                    "Your role (" + role + ") does not have access to " + screen + ".",
                    "Access Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Supplier<JFrame> factory = SCREENS.get(screen);
        if (factory == null)
        {
            JOptionPane.showMessageDialog(current, screen + " module coming soon.",
                    "Not available yet", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        factory.get().setVisible(true);
        current.dispose();
    }

    // Logout with confirm
    private static void logout(JFrame current)
    {
        int confirm = JOptionPane.showConfirmDialog(current,
                "Are you sure you want to log out?", "Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION)
        {
            Session.getInstance().clear();
            LoginController.open().setVisible(true);
            current.dispose();
        }
    }
}

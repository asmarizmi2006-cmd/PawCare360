package controller;

import exception.DatabaseException;
import exception.ValidationException;
import view.SidebarPanel;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

// Template for controllers
public abstract class BaseController<V extends JFrame>
{
    protected static final Logger LOG = Logger.getLogger(BaseController.class.getName());

    protected final V view;

    protected BaseController(V view)
    {
        this.view = view;

        // Apply the common keyboard behaviour to every MVC screen.
        // Pressing ENTER moves to the next editable field.
        util.UIHelper.installEnterNavigation(view.getContentPane());
    }

    // Open window (call after wiring)
    public V getView()
    {
        return view;
    }

    // Sidebar wiring
    protected void attachSidebar(String activeItem)
    {
        SidebarPanel.attach(view, activeItem, screen -> NavigationController.navigate(view, screen));
    }

    // Run with error handling
    protected void guard(Runnable action)
    {
        try
        {
            action.run();
        }
        catch (ValidationException e)
        {
            // Show the field name when available so the user immediately knows
            // which input must be corrected.
            String message = e.getField() == null || e.getField().trim().isEmpty()
                    ? e.getMessage()
                    : e.getField() + ": " + e.getMessage();
            warn(message);
        }
        catch (DatabaseException e)
        {
            error(e.getMessage());
        }
        catch (RuntimeException e)
        {
            LOG.log(Level.SEVERE, "Unexpected error", e);
            error("Something went wrong: " + e.getMessage());
        }
    }

    // Enter moves focus
    protected void focusNext(JComponent from, JComponent to)
    {
        if (from instanceof javax.swing.JTextField)
        {
            ((javax.swing.JTextField) from).addActionListener(e -> to.requestFocusInWindow());
        }
    }

    protected void info(String message)
    {
        JOptionPane.showMessageDialog(view, message, "PawCare 360", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void warn(String message)
    {
        JOptionPane.showMessageDialog(view, message, "Check input", JOptionPane.WARNING_MESSAGE);
    }

    protected void error(String message)
    {
        JOptionPane.showMessageDialog(view, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected boolean confirm(String message)
    {
        return JOptionPane.showConfirmDialog(view, message, "Please confirm",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}

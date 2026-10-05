package controller;

import exception.InvalidRoleException;
import model.User;
import service.UserService;
import util.Session;
import view.LoginForm;

import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

// Login screen logic
public class LoginController
{
    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());

    private final LoginForm view;
    private final UserService userService = new UserService();

    public static LoginForm open()
    {
        return new LoginController(new LoginForm()).view;
    }

    private LoginController(LoginForm view)
    {
        this.view = view;
        wire();
    }

    // Event wiring
    private void wire()
    {
        view.getTxtUsername().addActionListener(e -> view.getTxtPassword().requestFocusInWindow());
        view.getTxtPassword().addActionListener(e -> view.getCmbRole().requestFocusInWindow());
        view.getBtnLogin().addActionListener(e -> handleLogin());
        view.getRootPane().setDefaultButton(view.getBtnLogin());
    }

    private void handleLogin()
    {
        String username = view.getUsername();
        String password = new String(view.getPassword()).trim();
        String role = view.getRole();
        if (role.isEmpty())
        {
            role = null; // No role check
        }
        try
        {
            User user = userService.login(username, password, role);
            if (user == null)
            {
                view.showError("Invalid username or password.");
                view.clearPassword();
                return;
            }
            Session.getInstance().setCurrentUser(user);
            DashboardController.open().setVisible(true);
            view.dispose();
        }
        catch (InvalidRoleException | IllegalArgumentException ex)
        {
            view.showError(ex.getMessage());
        }
        catch (Exception ex)
        {
            LOG.log(Level.SEVERE, "Login failed", ex);
            JOptionPane.showMessageDialog(view,
                    "Unable to connect to the database. Check your XAMPP MySQL is running.",
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

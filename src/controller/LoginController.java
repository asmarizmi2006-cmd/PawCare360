package controller;

import exception.InvalidRoleException;
import model.User;
import service.UserService;
import util.Session;
import view.LoginForm;

import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for PawCare360 Login.
 *
 * Responsibilities:
 * - Validate login input
 * - Authenticate username/password/role
 * - Store logged-in user in Session
 * - Start loading animation only after successful login
 * - Open dashboard after loading completes
 */
public class LoginController
{
    private static final Logger LOG =
            Logger.getLogger(
                    LoginController.class.getName()
            );

    private final LoginForm view;

    private final UserService userService =
            new UserService();

    // =========================================================
    // OPEN LOGIN
    // =========================================================

    public static LoginForm open()
    {
        LoginForm form =
                new LoginForm();

        new LoginController(form);

        return form;
    }

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    private LoginController(
            LoginForm view)
    {
        this.view = view;

        wire();
    }

    // =========================================================
    // EVENT WIRING
    // =========================================================

    private void wire()
    {
        // Username → Password
        view.getTxtUsername()
                .addActionListener(
                        e ->
                        view.getTxtPassword()
                                .requestFocusInWindow()
                );

        // Password → Role
        view.getTxtPassword()
                .addActionListener(
                        e ->
                        view.getCmbRole()
                                .requestFocusInWindow()
                );

        // Login button
        view.getBtnLogin()
                .addActionListener(
                        e -> handleLogin()
                );

        // Pressing ENTER triggers login
        view.getRootPane()
                .setDefaultButton(
                        view.getBtnLogin()
                );
    }

    // =========================================================
    // LOGIN PROCESS
    // =========================================================

    private void handleLogin()
    {
        // -----------------------------------------------------
        // GET INPUT
        // -----------------------------------------------------

        String username =
                view.getUsername();

        String password =
                new String(
                        view.getPassword()
                );

        String selectedRole =
                view.getRole();

        // -----------------------------------------------------
        // VALIDATION
        // -----------------------------------------------------

        if (username.isEmpty())
        {
            showValidationError(
                    "Please enter your username."
            );

            view.getTxtUsername()
                    .requestFocusInWindow();

            return;
        }

        if (password.isEmpty())
        {
            showValidationError(
                    "Please enter your password."
            );

            view.getTxtPassword()
                    .requestFocusInWindow();

            return;
        }

        if (selectedRole.isEmpty())
        {
            showValidationError(
                    "Please select your role."
            );

            view.getCmbRole()
                    .requestFocusInWindow();

            return;
        }

        // -----------------------------------------------------
        // CONVERT DISPLAY ROLE TO DATABASE ROLE
        // -----------------------------------------------------

        String databaseRole =
                normalizeRole(
                        selectedRole
                );

        if (databaseRole == null)
        {
            showValidationError(
                    "Invalid role selected."
            );

            return;
        }

        // -----------------------------------------------------
        // AUTHENTICATION
        // -----------------------------------------------------

        try
        {
            User user =
                    userService.login(
                            username,
                            password,
                            databaseRole
                    );

            // -------------------------------------------------
            // INVALID LOGIN
            // -------------------------------------------------

            if (user == null)
            {
                view.showError(
                        "Invalid username, password, or role."
                );

                view.clearPassword();

                view.getTxtPassword()
                        .requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // EXTRA ROLE SECURITY CHECK
            // -------------------------------------------------

            String actualUserRole =
                    normalizeRole(
                            user.getRole()
                    );

            if (actualUserRole == null)
            {
                view.showError(
                        "This account has an invalid role."
                );

                view.clearPassword();

                return;
            }

            if (!databaseRole.equals(actualUserRole))
            {
                view.showError(
                        "The selected role does not match this account."
                );

                view.clearPassword();

                view.getCmbRole()
                        .requestFocusInWindow();

                return;
            }

            // -------------------------------------------------
            // LOGIN SUCCESS
            // -------------------------------------------------

            Session.getInstance()
                    .setCurrentUser(user);

            // -------------------------------------------------
            // START LOADING ONLY AFTER SUCCESS
            // -------------------------------------------------

            view.startLoading(
                    () ->
                    {
                        try
                        {
                            DashboardController
                                    .open()
                                    .setVisible(true);

                            view.dispose();
                        }
                        catch (Exception ex)
                        {
                            LOG.log(
                                    Level.SEVERE,
                                    "Unable to open dashboard",
                                    ex
                            );

                            JOptionPane.showMessageDialog(
                                    view,
                                    "Login was successful, "
                                    + "but the dashboard could not be opened.",
                                    "Dashboard Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
            );
        }
        catch (InvalidRoleException ex)
        {
            view.showError(
                    ex.getMessage()
            );

            view.clearPassword();
        }
        catch (IllegalArgumentException ex)
        {
            view.showError(
                    ex.getMessage()
            );

            view.clearPassword();
        }
        catch (Exception ex)
        {
            LOG.log(
                    Level.SEVERE,
                    "Login failed",
                    ex
            );

            JOptionPane.showMessageDialog(
                    view,
                    "Unable to connect to the database.\n\n"
                    + "Please make sure XAMPP MySQL is running.",
                    "Connection Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // ROLE NORMALIZATION
    // =========================================================

    /**
     * Converts the role displayed in the LoginForm
     * into the role used by the database and AccessControl.
     */
    private String normalizeRole(
            String role)
    {
        if (role == null)
        {
            return null;
        }

        String value =
                role.trim()
                        .toUpperCase();

        switch (value)
        {
            case "ADMIN":
                return "ADMIN";

            case "MANAGER":
                return "MANAGER";

            case "VETERINARIAN":
            case "VET":
                return "VETERINARIAN";

            case "NURSE":
                return "NURSE";

            case "GROOMER":
                return "GROOMER";

            case "RECEPTIONIST":
            case "RECEPTION":
                return "RECEPTIONIST";

            default:
                return null;
        }
    }

    // =========================================================
    // VALIDATION ERROR
    // =========================================================

    private void showValidationError(
            String message)
    {
        view.showError(
                message
        );
    }
}
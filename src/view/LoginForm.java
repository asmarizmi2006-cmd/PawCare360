package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.AbstractBorder;

/**
 * PawCare360 Login Form
 *
 * Passive View:
 * Login logic remains inside LoginController.
 */
public class LoginForm extends JFrame
{
    // =========================================================
    // PAWCARE360 BRAND COLORS
    // =========================================================

    // Main navy
    private static final Color NAVY =
            new Color(15, 39, 64);

    // Dark navy
    private static final Color DARK_NAVY =
            new Color(7, 22, 38);

    // YOUR ACTUAL BRAND GREEN
    // RGB(157, 201, 163)
    private static final Color BRAND_GREEN =
            new Color(157, 201, 163);

    // Soft beige
    private static final Color BEIGE =
            new Color(232, 226, 211);

    // White
    private static final Color WHITE =
            new Color(255, 255, 255);

    // Main text
    private static final Color TEXT =
            new Color(35, 48, 61);

    // Secondary text
    private static final Color MUTED =
            new Color(105, 120, 135);

    // Error
    private static final Color ERROR =
            new Color(190, 55, 55);

    // =========================================================
    // COMPONENTS
    // =========================================================

    private JLabel lblTitle;
    private JLabel lblSubtitle;
    private JLabel lblUsernameLabel;
    private JLabel lblPassword;
    private JLabel lblRole;
    private JLabel lblError;
    private JLabel lblLoading;

    private JTextField txtUsername;
    private JPasswordField txtPassword;

    private JComboBox<String> cmbRole;

    private JCheckBox chkShowPassword;

    private JButton btnLogin;

    private JProgressBar progressBar;

    private JPanel backgroundPanel;
    private JPanel loginCard;

    private char passwordEchoChar;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public LoginForm()
    {
        initComponents();
        
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        lblError.setText(" ");

        lblLoading.setVisible(false);
        progressBar.setVisible(false);

        passwordEchoChar =
                txtPassword.getEchoChar();
    }

    // =========================================================
    // GET USERNAME
    // =========================================================

    public String getUsername()
    {
        return txtUsername.getText().trim();
    }

    // =========================================================
    // GET PASSWORD
    // =========================================================

    public char[] getPassword()
    {
        return txtPassword.getPassword();
    }

    // =========================================================
    // GET ROLE
    // =========================================================

    public String getRole()
    {
        Object item =
                cmbRole.getSelectedItem();

        if (item == null)
        {
            return "";
        }

        return item.toString().trim();
    }

    // =========================================================
    // SHOW ERROR
    // =========================================================

    public void showError(String message)
    {
        lblError.setForeground(ERROR);
        lblError.setText(message);
    }

    // =========================================================
    // CLEAR PASSWORD
    // =========================================================

    public void clearPassword()
    {
        txtPassword.setText("");
    }

    // =========================================================
    // GET LOGIN BUTTON
    // =========================================================

    public JButton getBtnLogin()
    {
        return btnLogin;
    }

    // =========================================================
    // GET USERNAME FIELD
    // =========================================================

    public JTextField getTxtUsername()
    {
        return txtUsername;
    }

    // =========================================================
    // GET PASSWORD FIELD
    // =========================================================

    public JPasswordField getTxtPassword()
    {
        return txtPassword;
    }

    // =========================================================
    // GET ROLE COMBO
    // =========================================================

    public JComboBox<String> getCmbRole()
    {
        return cmbRole;
    }

    // =========================================================
    // START GREEN LOADING BAR
    // =========================================================

    /**
     * This method should only be called after
     * username + password + role are successfully verified.
     */
    public void startLoading(Runnable afterLoading)
    {
        lblError.setText(" ");

        lblLoading.setText(
                "Loading PawCare360..."
        );

        lblLoading.setForeground(
                NAVY
        );

        lblLoading.setVisible(true);

        progressBar.setValue(0);
        progressBar.setVisible(true);

        btnLogin.setEnabled(false);
        txtUsername.setEnabled(false);
        txtPassword.setEnabled(false);
        cmbRole.setEnabled(false);
        chkShowPassword.setEnabled(false);

        Timer timer =
                new Timer(25, null);

        timer.addActionListener(e ->
        {
            int value =
                    progressBar.getValue();

            if (value < 100)
            {
                progressBar.setValue(
                        value + 2
                );
            }
            else
            {
                timer.stop();

                lblLoading.setText(
                        "Login successful"
                );

                lblLoading.setForeground(
                        new Color(
                                57,
                                125,
                                76
                        )
                );

                Timer finishTimer =
                        new Timer(
                                400,
                                event ->
                                {
                                    ((Timer) event.getSource())
                                            .stop();

                                    if (afterLoading != null)
                                    {
                                        afterLoading.run();
                                    }
                                }
                        );

                finishTimer.setRepeats(false);
                finishTimer.start();
            }
        });

        timer.start();
    }

    // =========================================================
    // COMPONENT POSITION HELPER
    // =========================================================

    private void addAt(
            Container parent,
            Component component,
            int x,
            int y,
            int width,
            int height)
    {
        component.setBounds(
                x,
                y,
                width,
                height
        );

        parent.add(component);
    }

    // =========================================================
    // INITIALIZE COMPONENTS
    // =========================================================

    private void initComponents()
    {
        setDefaultCloseOperation(
                WindowConstants.EXIT_ON_CLOSE
        );

        setTitle(
                "PawCare 360 - Login"
        );

        setSize(
                1500,
                900
        );

        setMinimumSize(
                new Dimension(
                        1500,
                        900
                )
        );

        // =====================================================
        // BACKGROUND
        // =====================================================

        backgroundPanel =
                new GradientPanel();

        backgroundPanel.setLayout(null);

        setContentPane(
                backgroundPanel
        );

        // =====================================================
        // LEFT SIDE BRANDING
        // =====================================================

        JLabel lblBrand =
                new JLabel(
                        "PAWCARE 360"
                );

        lblBrand.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        42
                )
        );

        lblBrand.setForeground(
                WHITE
        );

        addAt(
                backgroundPanel,
                lblBrand,
                90,
                170,
                500,
                60
        );

        // =====================================================
        // CLEAN GREEN BRAND LINE
        // =====================================================

        JPanel brandLine =
                new JPanel();

        brandLine.setBackground(
                BRAND_GREEN
        );

        addAt(
                backgroundPanel,
                brandLine,
                95,
                235,
                90,
                4
        );

        // =====================================================
        // CLINIC TITLE
        // =====================================================

        JLabel lblClinic =
                new JLabel(
                        "<html>"
                        + "<div style='text-align:left;'>"
                        + "Veterinary Clinic<br>"
                        + "Management System"
                        + "</div>"
                        + "</html>"
                );

        lblClinic.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        22
                )
        );

        lblClinic.setForeground(
                WHITE
        );

        addAt(
                backgroundPanel,
                lblClinic,
                95,
                275,
                400,
                80
        );

        // =====================================================
        // TAGLINE
        // =====================================================

        JLabel lblTagline =
                new JLabel(
                        "<html>"
                        + "<div style='text-align:left;'>"
                        + "Caring for pets.<br>"
                        + "Managing with care."
                        + "</div>"
                        + "</html>"
                );

        lblTagline.setFont(
                new Font(
                        "Segoe UI",
                        Font.ITALIC,
                        17
                )
        );

        lblTagline.setForeground(
                new Color(
                        220,
                        225,
                        220
                )
        );

        addAt(
                backgroundPanel,
                lblTagline,
                95,
                400,
                400,
                70
        );

        // =====================================================
        // GREEN PAW
        // =====================================================

        JLabel lblPaw =
                new JLabel("🐾");

        lblPaw.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        70
                )
        );

        lblPaw.setForeground(
                BRAND_GREEN
        );

        addAt(
                backgroundPanel,
                lblPaw,
                90,
                515,
                120,
                100
        );

        // =====================================================
        // LOGIN CARD
        // =====================================================

        loginCard =
                new RoundedPanel(
                        30,
                        WHITE
                );

        loginCard.setLayout(null);

        addAt(
                backgroundPanel,
                loginCard,
                720,
                125,
                600,
                620
        );

        // =====================================================
        // CARD TITLE
        // =====================================================

        lblTitle =
                new JLabel(
                        "Welcome Back"
                );

        lblTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        lblTitle.setForeground(
                NAVY
        );

        addAt(
                loginCard,
                lblTitle,
                55,
                38,
                490,
                45
        );

        // =====================================================
        // CARD SUBTITLE
        // =====================================================

        lblSubtitle =
                new JLabel(
                        "Sign in to continue to PawCare360"
                );

        lblSubtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        15
                )
        );

        lblSubtitle.setForeground(
                MUTED
        );

        addAt(
                loginCard,
                lblSubtitle,
                58,
                78,
                490,
                30
        );

        // =====================================================
        // USERNAME LABEL
        // =====================================================

        lblUsernameLabel =
                createLabel(
                        "Username"
                );

        addAt(
                loginCard,
                lblUsernameLabel,
                55,
                125,
                490,
                25
        );

        // =====================================================
        // USERNAME FIELD
        // =====================================================

        txtUsername =
                createTextField();

        addAt(
                loginCard,
                txtUsername,
                55,
                152,
                490,
                46
        );

        // =====================================================
        // PASSWORD LABEL
        // =====================================================

        lblPassword =
                createLabel(
                        "Password"
                );

        addAt(
                loginCard,
                lblPassword,
                55,
                215,
                490,
                25
        );

        // =====================================================
        // PASSWORD FIELD
        // =====================================================

        txtPassword =
                new JPasswordField();

        stylePasswordField(
                txtPassword
        );
        
        txtPassword.addActionListener(e -> btnLogin.doClick());

        addAt(
                loginCard,
                txtPassword,
                55,
                242,
                490,
                46
        );

        // =====================================================
        // SHOW PASSWORD
        // =====================================================

        chkShowPassword =
                new JCheckBox(
                        "Show Password"
                );

        chkShowPassword.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        chkShowPassword.setForeground(
                NAVY
        );

        chkShowPassword.setBackground(
                WHITE
        );

        chkShowPassword.setFocusPainted(
                false
        );

        chkShowPassword.addActionListener(
                e ->
                {
                    if (chkShowPassword.isSelected())
                    {
                        txtPassword.setEchoChar(
                                (char) 0
                        );
                    }
                    else
                    {
                        txtPassword.setEchoChar(
                                passwordEchoChar
                        );
                    }
                }
        );

        addAt(
                loginCard,
                chkShowPassword,
                52,
                294,
                180,
                28
        );

        // =====================================================
        // ROLE LABEL
        // =====================================================

        lblRole =
                createLabel(
                        "Role"
                );

        addAt(
                loginCard,
                lblRole,
                55,
                330,
                490,
                25
        );

        // =====================================================
        // ROLE COMBO BOX
        // =====================================================

        cmbRole =
                new JComboBox<>();

        cmbRole.setModel(
                new DefaultComboBoxModel<>(
                        new String[]
                        {
                            "Admin",
                            "Manager",
                            "Veterinarian",
                            "Nurse",
                            "Groomer",
                            "Receptionist"
                        }
                )
        );

        cmbRole.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        cmbRole.setBackground(
                WHITE
        );

        cmbRole.setForeground(
                TEXT
        );

        cmbRole.setFocusable(
                false
        );

        addAt(
                loginCard,
                cmbRole,
                55,
                357,
                490,
                44
        );

        // =====================================================
        // ERROR LABEL
        // =====================================================

        lblError =
                new JLabel(" ");

        lblError.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblError.setForeground(
                ERROR
        );

        addAt(
                loginCard,
                lblError,
                55,
                405,
                490,
                22
        );

        // =====================================================
        // LOGIN BUTTON
        // =====================================================

        btnLogin =
                new RoundedButton(
                        "LOGIN",
                        BRAND_GREEN,
                        BRAND_GREEN
                );

        btnLogin.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        addAt(
                loginCard,
                btnLogin,
                55,
                445,
                490,
                50
        );

        // =====================================================
        // LOADING LABEL
        // =====================================================

        lblLoading =
                new JLabel(
                        "Loading PawCare360..."
                );

        lblLoading.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        lblLoading.setForeground(
                NAVY
        );

        lblLoading.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        addAt(
                loginCard,
                lblLoading,
                55,
                505,
                490,
                24
        );

        // =====================================================
        // GREEN PROGRESS BAR
        // =====================================================

        progressBar =
                new JProgressBar(
                        0,
                        100
                );

        progressBar.setValue(0);

        progressBar.setStringPainted(
                true
        );

        progressBar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );
        progressBar.setUI(new BasicProgressBarUI());

        progressBar.setForeground(
                NAVY
        );

        progressBar.setBackground(
                new Color(
                        225,
                        230,
                        225
                )
        );

        progressBar.setBorderPainted(
                false
        );

        addAt(
                loginCard,
                progressBar,
                55,
                535,
                490,
                18
        );

        // =====================================================
        // HIDE LOADING INITIALLY
        // =====================================================

        lblLoading.setVisible(
                false
        );

        progressBar.setVisible(
                false
        );
    }

    // =========================================================
    // LABEL CREATOR
    // =========================================================

    private JLabel createLabel(
            String text)
    {
        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        label.setForeground(
                TEXT
        );

        return label;
    }

    // =========================================================
    // TEXT FIELD
    // =========================================================

    private JTextField createTextField()
    {
        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        field.setForeground(
                TEXT
        );

        field.setBackground(
                WHITE
        );

        field.setBorder(
                new RoundedBorder(
                        new Color(
                                205,
                                212,
                                220
                        ),
                        12
                )
        );

        return field;
    }

    // =========================================================
    // PASSWORD FIELD
    // =========================================================

    private void stylePasswordField(
            JPasswordField field)
    {
        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        field.setForeground(
                TEXT
        );

        field.setBackground(
                WHITE
        );

        field.setBorder(
                new RoundedBorder(
                        new Color(
                                205,
                                212,
                                220
                        ),
                        12
                )
        );
    }

    // =========================================================
    // CUSTOM BACKGROUND
    // =========================================================

    private static class GradientPanel
            extends JPanel
    {
        @Override
        protected void paintComponent(
                Graphics g)
        {
            super.paintComponent(g);

            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // =================================================
            // NAVY → BEIGE GRADIENT
            // =================================================

            GradientPaint gradient =
                    new GradientPaint(
                            0,
                            0,
                            DARK_NAVY,
                            getWidth(),
                            getHeight(),
                            BEIGE
                    );

            g2.setPaint(
                    gradient
            );

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            // =================================================
            // SOFT GREEN CIRCLE
            // =================================================

            g2.setColor(
                    new Color(
                            157,
                            201,
                            163,
                            80
                    )
            );

            g2.fillOval(
                    -120,
                    610,
                    350,
                    350
            );

            // =================================================
            // SOFT BEIGE CIRCLE
            // =================================================

            g2.setColor(
                    new Color(
                            232,
                            226,
                            211,
                            100
                    )
            );

            g2.fillOval(
                    420,
                    -170,
                    330,
                    330
            );

            // =================================================
            // GREEN DECORATIVE CIRCLE
            // =================================================

            g2.setColor(
                    new Color(
                            157,
                            201,
                            163,
                            45
                    )
            );

            g2.fillOval(
                    250,
                    680,
                    180,
                    180
            );

            g2.dispose();
        }
    }

    // =========================================================
    // ROUNDED PANEL
    // =========================================================

    private static class RoundedPanel
            extends JPanel
    {
        private final int radius;
        private final Color background;

        RoundedPanel(
                int radius,
                Color background)
        {
            this.radius =
                    radius;

            this.background =
                    background;

            setOpaque(
                    false
            );
        }

        @Override
        protected void paintComponent(
                Graphics g)
        {
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            // =================================================
            // SHADOW
            // =================================================

            g2.setColor(
                    new Color(
                            0,
                            0,
                            0,
                            45
                    )
            );

            g2.fillRoundRect(
                    5,
                    7,
                    getWidth() - 10,
                    getHeight() - 10,
                    radius,
                    radius
            );

            // =================================================
            // CARD
            // =================================================

            g2.setColor(
                    background
            );

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 8,
                    getHeight() - 8,
                    radius,
                    radius
            );

            g2.dispose();

            super.paintComponent(
                    g
            );
        }
    }

    // =========================================================
    // ROUNDED BORDER
    // =========================================================

    private static class RoundedBorder
            extends AbstractBorder
    {
        private final Color color;
        private final int radius;

        RoundedBorder(
                Color color,
                int radius)
        {
            this.color =
                    color;

            this.radius =
                    radius;
        }

        @Override
        public void paintBorder(
                Component c,
                Graphics g,
                int x,
                int y,
                int width,
                int height)
        {
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(
                    color
            );

            g2.drawRoundRect(
                    x,
                    y,
                    width - 1,
                    height - 1,
                    radius,
                    radius
            );

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(
                Component c)
        {
            return new Insets(
                    8,
                    12,
                    8,
                    12
            );
        }
    }

    // =========================================================
    // ROUNDED BUTTON
    // =========================================================

    private static class RoundedButton
            extends JButton
    {
        private final Color buttonColor;
        private final Color borderColor;

        RoundedButton(
                String text,
                Color buttonColor,
                Color borderColor)
        {
            super(text);

            this.buttonColor =
                    buttonColor;

            this.borderColor =
                    borderColor;

            setForeground(
                    WHITE
            );

            setBackground(
                    buttonColor
            );

            setFocusPainted(
                    false
            );

            setBorderPainted(
                    false
            );

            setContentAreaFilled(
                    false
            );

            setOpaque(
                    false
            );

            setCursor(
                    new Cursor(
                            Cursor.HAND_CURSOR
                    )
            );
        }

        @Override
        protected void paintComponent(
                Graphics g)
        {
            Graphics2D g2 =
                    (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            Color fill =
                    buttonColor;

            if (getModel().isPressed())
            {
                fill =
                        buttonColor.darker();
            }
            else if (getModel().isRollover())
            {
                fill =
                        new Color(
                                25,
                                58,
                                88
                        );
            }

            // =================================================
            // BUTTON
            // =================================================

            g2.setColor(
                    fill
            );

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    14,
                    14
            );

            // =================================================
            // GREEN BORDER
            // =================================================

            g2.setColor(
                    borderColor
            );

            g2.setStroke(
                    new BasicStroke(
                            1.5f
                    )
            );

            g2.drawRoundRect(
                    1,
                    1,
                    getWidth() - 3,
                    getHeight() - 3,
                    14,
                    14
            );

            g2.dispose();

            super.paintComponent(
                    g
            );
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

    /*
     * Temporary direct test.
     *
     * This directly opens LoginForm.
     * After the UI is confirmed, LoginController
     * will be connected to it.
     */
    public static void main(
            String[] args)
    {
        EventQueue.invokeLater(
                () ->
                {
                    LoginForm form =
                            new LoginForm();

                    form.setVisible(
                            true
                    );
                }
        );
    }
}
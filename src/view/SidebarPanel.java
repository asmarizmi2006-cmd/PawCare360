package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


public class SidebarPanel extends JPanel implements Serializable {

    // Navigation callback
    public interface NavigationListener {
        void onNavigate(String screenName);
    }

    public static final Color NAVY = new Color(24, 37, 46);
    public static final Color NAVY_LIGHT = new Color(34, 51, 61);
    public static final Color SAGE = new Color(164, 190, 143);

    private static final String[] ITEMS = {
            "DASHBOARD", "CUSTOMERS", "PET PATIENTS", "STAFF", "SERVICES", "APPOINTMENTS",
            "TREATMENTS", "GROOMING", "BOARDING", "BILLING", "REPORTS"
    };

    private final Map<String, JButton> navButtons = new LinkedHashMap<>();
    private JButton logoutButton;
    private String activeItem = "DASHBOARD";
    private NavigationListener navigationListener;

    // Designer constructor
    public SidebarPanel() {
        this("DASHBOARD");
    }

    // Active item constructor
    public SidebarPanel(String activeItem) {
        this.activeItem = activeItem;
        setLayout(null);
        setBackground(NAVY);
        setPreferredSize(new Dimension(250, 900));
        buildUI();
        highlightActive();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionLogout();
            }
        });
    }

    // ---- Bean properties -------------------------------------------------

    // Find or add sidebar
    public static SidebarPanel attach(JFrame frame, String active, NavigationListener listener) {
        SidebarPanel found = find(frame.getContentPane());
        if (found == null) {
            found = new SidebarPanel();
            Container root = frame.getContentPane();
            if (root.getLayout() instanceof BorderLayout) {
                root.add(found, BorderLayout.WEST);
            } else {
                Component[] old = root.getComponents();
                JPanel wrap = new JPanel(new BorderLayout());
                JPanel body = new JPanel(root.getLayout());
                for (Component c : old) {
                    body.add(c);
                }
                wrap.add(found, BorderLayout.WEST);
                wrap.add(body, BorderLayout.CENTER);
                root.removeAll();
                root.setLayout(new BorderLayout());
                root.add(wrap, BorderLayout.CENTER);
            }
        }
        found.setActiveItem(active);
        found.setNavigationListener(listener);
        return found;
    }

    private static SidebarPanel find(Container c) {
        for (Component child : c.getComponents()) {
            if (child instanceof SidebarPanel) {
                return (SidebarPanel) child;
            }
            if (child instanceof Container) {
                SidebarPanel r = find((Container) child);
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
    }

    public String getActiveItem() {
        return activeItem;
    }

    public void setActiveItem(String activeItem) {
        this.activeItem = activeItem;
        highlightActive();
    }

    public void setNavigationListener(NavigationListener listener) {
        this.navigationListener = listener;
    }

    // ---- UI construction ---------------------------------------------------

    private void buildUI() {
        PawLogoPanel logo = new PawLogoPanel();
        logo.setBounds(28, 30, 55, 55);
        add(logo);

        JLabel brand = new JLabel("PAWCARE");
        brand.setBounds(94, 28, 140, 30);
        brand.setFont(new Font("Segoe UI", Font.BOLD, 23));
        brand.setForeground(Color.WHITE);
        add(brand);

        JLabel number = new JLabel("360");
        number.setBounds(96, 56, 100, 20);
        number.setFont(new Font("Segoe UI", Font.BOLD, 12));
        number.setForeground(SAGE);
        add(number);

        JLabel clinic = new JLabel("VETERINARY • PET CARE");
        clinic.setBounds(30, 105, 190, 18);
        clinic.setFont(new Font("Segoe UI", Font.BOLD, 9));
        clinic.setForeground(new Color(139, 157, 164));
        add(clinic);

        JPanel line = new JPanel();
        line.setBounds(30, 132, 190, 1);
        line.setBackground(new Color(57, 72, 81));
        add(line);

        JLabel workspace = new JLabel("CLINIC WORKSPACE");
        workspace.setBounds(30, 157, 180, 18);
        workspace.setFont(new Font("Segoe UI", Font.BOLD, 9));
        workspace.setForeground(new Color(125, 145, 153));
        add(workspace);

        for (int i = 0; i < ITEMS.length; i++) {
            String item = ITEMS[i];
            JButton button = createNavButton(item, 190 + (i * 48));
            button.addActionListener(e -> {
                if (navigationListener != null) {
                    navigationListener.onNavigate(item);
                }
            });
            navButtons.put(item, button);
            add(button);
        }

        // Logout button
        logoutButton = new LogoutButton("LOGOUT");
        logoutButton.setBounds(25, 790, 200, 46);
        logoutButton.addActionListener(e -> {
            if (navigationListener != null) {
                navigationListener.onNavigate("LOGOUT");
            }
        });
        add(logoutButton);
    }

    private void repositionLogout() {
        if (logoutButton == null) return;
        int h = getHeight();
        if (h <= 0) return;
        int y = Math.max(h - 110, 745);
        logoutButton.setBounds(25, y, 200, 46);
    }

    private void highlightActive() {
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            boolean active = entry.getKey().equals(activeItem);
            styleNavButton(entry.getValue(), active);
        }
    }

    private JButton createNavButton(String text, int y) {
        JButton button = new JButton(text);
        button.setBounds(15, y, 220, 40);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        button.setFont(new Font("Segoe UI", Font.BOLD, 10));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setBorderPainted(false);
        return button;
    }

    private void styleNavButton(JButton button, boolean active) {
        Color normalBg = active ? SAGE : NAVY;
        Color hoverBg = active ? new Color(177, 200, 158) : NAVY_LIGHT;
        Color normalFg = active ? new Color(31, 48, 56) : new Color(205, 216, 220);

        button.setBackground(normalBg);
        button.setForeground(normalFg);

        // Remove old hover
        for (var listener : button.getMouseListeners()) {
            if (listener instanceof NavHoverListener) {
                button.removeMouseListener(listener);
            }
        }

        NavHoverListener hoverListener = new NavHoverListener(button, normalBg, hoverBg);
        button.addMouseListener(hoverListener);
    }

    private static class NavHoverListener extends MouseAdapter {
        private final JButton button;
        private final Color normal;
        private final Color hover;

        NavHoverListener(JButton button, Color normal, Color hover) {
            this.button = button;
            this.normal = normal;
            this.hover = hover;
        }

        @Override
        public void mouseEntered(MouseEvent e) { button.setBackground(hover); }

        @Override
        public void mouseExited(MouseEvent e) { button.setBackground(normal); }
    }

    // Black outlined logout
    private static class LogoutButton extends JButton {
        private boolean hovering = false;

        LogoutButton(String text) {
            super(text);
            setHorizontalAlignment(SwingConstants.LEFT);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(SAGE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(0, 44, 0, 10));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { hovering = true; repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { hovering = false; repaint(); }
            });
        }

        // Hand-painted look
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(hovering ? new Color(28, 28, 28) : Color.BLACK);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));

            g2.setColor(SAGE);
            g2.setStroke(new BasicStroke(1.8f));
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 10, 10));

            // Exit icon
            g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int ix = 14, iy = getHeight() / 2 - 10;
            g2.drawPolyline(
                    new int[]{ix + 9, ix + 9, ix + 22, ix + 22, ix + 9, ix + 9},
                    new int[]{iy + 6, iy, iy, iy + 20, iy + 20, iy + 14}, 6);
            g2.fillPolygon(
                    new int[]{ix, ix + 7, ix + 7, ix + 15, ix + 15, ix + 7, ix + 7},
                    new int[]{iy + 10, iy + 4, iy + 8, iy + 8, iy + 12, iy + 12, iy + 16}, 7);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    private static class PawLogoPanel extends JPanel {
        PawLogoPanel() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(SAGE);
            g2.fill(new Ellipse2D.Double(16, 25, 25, 22));
            g2.fill(new Ellipse2D.Double(3, 15, 11, 13));
            g2.fill(new Ellipse2D.Double(13, 5, 11, 13));
            g2.fill(new Ellipse2D.Double(27, 5, 11, 13));
            g2.fill(new Ellipse2D.Double(38, 15, 11, 13));
            g2.dispose();
            super.paintComponent(graphics);
        }
    }
}
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.BeanInfo;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


public class SidebarPanel extends JPanel implements Serializable {

    /** Fired when the user clicks a nav item; screenName is its label, e.g. "CUSTOMERS". */
    public interface NavigationListener {
        void onNavigate(String screenName);
    }

    public static final Color NAVY = new Color(24, 37, 46);
    public static final Color NAVY_LIGHT = new Color(34, 51, 61);
    public static final Color SAGE = new Color(164, 190, 143);

    private static final String[] ITEMS = {
            "DASHBOARD", "CUSTOMERS", "PET PATIENTS", "APPOINTMENTS",
            "TREATMENTS", "GROOMING", "BOARDING", "BILLING", "REPORTS"
    };

    private final Map<String, JButton> navButtons = new LinkedHashMap<>();
    private JPanel staffCard;
    private String activeItem = "DASHBOARD";
    private NavigationListener navigationListener;

    /** Required no-arg constructor so the NetBeans Palette/GUI Builder can instantiate this bean. */
    public SidebarPanel() {
        this("DASHBOARD");
    }

    /** Convenience constructor for hand-coded screens that know their active item up front. */
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
                repositionStaffCard();
            }
        });
    }

    // ---- Bean properties -------------------------------------------------

    /** Bean property so it can also be set from the NetBeans Properties panel at design time. */
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

        JLabel clinic = new JLabel("VETERINARY \u2022 PET CARE");
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

        staffCard = new RoundedPanel(NAVY_LIGHT, 16);
        staffCard.setLayout(null);
        staffCard.setBounds(25, 735, 200, 100);
        add(staffCard);

        JLabel userTitle = new JLabel("CURRENT USER");
        userTitle.setBounds(15, 13, 150, 15);
        userTitle.setFont(new Font("Segoe UI", Font.BOLD, 8));
        userTitle.setForeground(new Color(130, 149, 156));
        staffCard.add(userTitle);

        JLabel user = new JLabel("Reception Desk");
        user.setBounds(15, 34, 160, 20);
        user.setFont(new Font("Segoe UI", Font.BOLD, 11));
        user.setForeground(Color.WHITE);
        staffCard.add(user);

        JLabel status = new JLabel("\u25CF Online");
        status.setBounds(15, 61, 100, 18);
        status.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        status.setForeground(SAGE);
        staffCard.add(status);
    }

    private void repositionStaffCard() {
        if (staffCard == null) return;
        int h = getHeight();
        if (h <= 0) return;
        int y = Math.max(h - 165, 400);
        staffCard.setBounds(25, y, 200, 100);
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

        // Clear any previously-added hover listeners before re-adding, so
        // switching the active item doesn't stack duplicate listeners.
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

    private static class RoundedPanel extends JPanel {
        private final Color backgroundColor;
        private final int radius;

        RoundedPanel(Color backgroundColor, int radius) {
            this.backgroundColor = backgroundColor;
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g2 = (Graphics2D) graphics.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(backgroundColor);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            g2.dispose();
            super.paintComponent(graphics);
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

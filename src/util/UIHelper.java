package util;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Common Swing UI behaviour used by PawCare360.
 *
 * Main purpose:
 * 1. Press ENTER to move to the next editable input field.
 * 2. Keep the behaviour consistent across every MVC screen.
 *
 * The method intentionally leaves the last field alone. This means an
 * existing ENTER action such as Save/Add/Login can still execute.
 */
public final class UIHelper {

    private UIHelper() {
        // Utility class; no objects are required.
    }

    /**
     * Adds ENTER-key navigation to all editable text/combo inputs in a form.
     *
     * Components are ordered using their visual position (top-to-bottom,
     * left-to-right), which works well with NetBeans AbsoluteLayout forms.
     */
    public static void installEnterNavigation(Container root) {
        List<JComponent> fields = new ArrayList<>();
        collectInputFields(root, fields);

        // Sort by the component's position in the form.
        fields.sort(Comparator
                .comparingInt(UIHelper::absoluteY)
                .thenComparingInt(UIHelper::absoluteX));

        for (int i = 0; i < fields.size(); i++) {
            JComponent current = fields.get(i);
            JComponent next = (i + 1 < fields.size()) ? fields.get(i + 1) : null;

            current.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() != KeyEvent.VK_ENTER || next == null) {
                        return;
                    }

                    // Do not steal ENTER while a combo-box popup is open.
                    if (current instanceof JComboBox
                            && ((JComboBox<?>) current).isPopupVisible()) {
                        return;
                    }

                    // Shift+Enter is kept available for text editing.
                    if (e.isShiftDown()) {
                        return;
                    }

                    e.consume();
                    next.requestFocusInWindow();
                }
            });
        }
    }

    private static void collectInputFields(Component component, List<JComponent> result) {
        if (component instanceof JTextField
                || component instanceof JPasswordField
                || component instanceof JTextArea
                || component instanceof JComboBox) {

            JComponent input = (JComponent) component;

            // Read-only IDs and disabled/hidden controls should not be
            // included in keyboard navigation.
            if (input.isEnabled() && input.isVisible()
                    && (!(input instanceof JTextField)
                    || ((JTextField) input).isEditable())) {
                result.add(input);
            }
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                collectInputFields(child, result);
            }
        }
    }

    private static int absoluteX(Component component) {
        int x = component.getX();
        Component parent = component.getParent();

        while (parent != null) {
            x += parent.getX();
            parent = parent.getParent();
        }
        return x;
    }

    private static int absoluteY(Component component) {
        int y = component.getY();
        Component parent = component.getParent();

        while (parent != null) {
            y += parent.getY();
            parent = parent.getParent();
        }
        return y;
    }
}

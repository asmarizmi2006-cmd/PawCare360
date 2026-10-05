package view;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;

// Donut chart bean
public class DonutChartPanel extends JPanel
{
    private static final Color NAVY = new Color(44, 62, 80);
    private static final Color[] PIE_COLORS = {
        new Color(90, 140, 220), new Color(95, 185, 130), new Color(230, 160, 70),
        new Color(150, 110, 210), new Color(60, 175, 170), new Color(220, 100, 120)
    };

    private Map<String, Integer> data = new LinkedHashMap<>();
    private double progress = 1.0;

    public DonutChartPanel()
    {
        setOpaque(false);
    }

    public void setData(Map<String, Integer> data)
    {
        this.data = data == null ? new LinkedHashMap<>() : data;
        progress = 0.0;
        Timer timer = new Timer(16, null);
        timer.addActionListener(e ->
        {
            progress += 0.05;
            if (progress >= 1.0)
            {
                progress = 1.0;
                ((Timer) e.getSource()).stop();
            }
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();

        g2.setColor(new Color(0, 0, 0, 15));
        g2.fill(new RoundRectangle2D.Double(3, 4, w - 4, h - 4, 16, 16));
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(0, 0, w - 4, h - 4, 16, 16));

        int total = 0;
        for (int v : data.values())
        {
            total += v;
        }
        if (total == 0)
        {
            g2.dispose();
            return;
        }

        int size = Math.min(h - 30, 140);
        int cx = 30, cy = (h - size) / 2;

        double startAngle = 90;
        int colorIndex = 0;
        int legendY = 20;
        for (Map.Entry<String, Integer> entry : data.entrySet())
        {
            double sweep = (entry.getValue() / (double) total) * 360.0 * progress;
            Color color = PIE_COLORS[colorIndex % PIE_COLORS.length];
            g2.setColor(color);
            g2.fill(new Arc2D.Double(cx, cy, size, size, startAngle, -sweep, Arc2D.PIE));
            startAngle -= sweep;

            g2.fillRect(cx + size + 25, legendY, 10, 10);
            g2.setColor(NAVY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString(entry.getKey() + "  (" + entry.getValue() + ")", cx + size + 40, legendY + 9);
            legendY += 22;
            colorIndex++;
        }

        int holeSize = (int) (size * 0.55);
        g2.setColor(Color.WHITE);
        g2.fill(new Ellipse2D.Double(cx + (size - holeSize) / 2.0, cy + (size - holeSize) / 2.0, holeSize, holeSize));
        g2.setColor(NAVY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String totalText = String.valueOf(total);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(totalText, cx + size / 2 - fm.stringWidth(totalText) / 2, cy + size / 2 + 5);

        g2.dispose();
        super.paintComponent(g);
    }
}

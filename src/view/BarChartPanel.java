package view;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

// Bar chart bean
public class BarChartPanel extends JPanel
{
    private static final Color NAVY = new Color(44, 62, 80);
    private static final Color GREY = new Color(130, 140, 150);
    private static final Color BLUE = new Color(90, 140, 220);
    private static final Color PURPLE = new Color(150, 110, 210);

    private int[] data = new int[7];
    private double progress = 1.0;

    public BarChartPanel()
    {
        setOpaque(false);
    }

    public void setData(int[] data)
    {
        this.data = data == null ? new int[7] : data;
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

        int padding = 24;
        int chartH = h - padding - 28;
        int barGap = 16;
        int barW = (w - padding * 2 - barGap * (data.length - 1)) / data.length;

        int max = 1;
        for (int v : data)
        {
            max = Math.max(max, v);
        }

        java.time.LocalDate today = java.time.LocalDate.now();
        for (int i = 0; i < data.length; i++)
        {
            int barHeight = (int) ((data[i] / (double) max) * chartH * progress);
            int x = padding + i * (barW + barGap);
            int y = padding + chartH - barHeight;

            g2.setPaint(new GradientPaint(x, y, BLUE, x, y + Math.max(barHeight, 2), PURPLE));
            g2.fill(new RoundRectangle2D.Double(x, y, barW, Math.max(barHeight, 2), 6, 6));

            g2.setColor(NAVY);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            String valueText = String.valueOf(data[i]);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(valueText, x + (barW - fm.stringWidth(valueText)) / 2, y - 5);

            String label = today.minusDays(data.length - 1 - i).getDayOfWeek()
                    .getDisplayName(java.time.format.TextStyle.SHORT, java.util.Locale.ENGLISH);
            g2.setColor(GREY);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            FontMetrics fmLabel = g2.getFontMetrics();
            g2.drawString(label, x + (barW - fmLabel.stringWidth(label)) / 2, padding + chartH + 18);
        }
        g2.dispose();
        super.paintComponent(g);
    }
}

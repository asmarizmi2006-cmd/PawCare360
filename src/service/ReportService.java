package service;

import dao.ReportDAO;
import exception.ValidationException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Jasper report builder
public class ReportService
{
    private final ReportDAO dao = new ReportDAO();
    private final SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat stampFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    static
    {
        System.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
        System.setProperty("net.sf.jasperreports.default.font.name", "SansSerif");
    }

    // Load compiled report
    private JasperReport load(String name) throws JRException
    {
        String path = "/reports/" + name + ".jasper";
        InputStream in = ReportService.class.getResourceAsStream(path);
        if (in == null)
        {
            throw new JRException("Report file missing: " + path);
        }
        return (JasperReport) JRLoader.loadObject(in);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private JRDataSource source(List<Map<String, ?>> rows)
    {
        return new JRMapCollectionDataSource((Collection) rows);
    }

    private Timestamp startOf(Date d)
    {
        return Timestamp.valueOf(dateFmt.format(d) + " 00:00:00");
    }

    private Timestamp endOf(Date d)
    {
        return Timestamp.valueOf(dateFmt.format(d) + " 23:59:59");
    }

    private void checkRange(Date from, Date to)
    {
        if (from == null || to == null)
        {
            throw new ValidationException("Pick both dates.");
        }
        if (from.after(to))
        {
            throw new ValidationException("'From' is after 'To'.");
        }
    }

    private Map<String, Object> rangeParams(Date from, Date to)
    {
        Map<String, Object> p = new HashMap<>();
        p.put("RANGE", dateFmt.format(from) + " to " + dateFmt.format(to));
        p.put("GENERATED", stampFmt.format(new Date()));
        return p;
    }

    // Chart data by month
    private JRDataSource periodChart(List<Map<String, ?>> rows)
    {
        Map<String, BigDecimal[]> sums = new TreeMap<>();
        rows.forEach(r -> {
            BigDecimal[] a = sums.computeIfAbsent(String.valueOf(r.get("period")),
                    k -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
            a[0] = a[0].add(money(r.get("total_amount")));
            a[1] = a[1].add(money(r.get("paid_amount")));
        });
        List<Map<String, ?>> out = new ArrayList<>();
        sums.forEach((k, a) -> {
            Map<String, Object> m = new HashMap<>();
            m.put("label", k);
            m.put("billed", a[0]);
            m.put("collected", a[1]);
            out.add(m);
        });
        return source(out);
    }

    // Chart data by count
    private JRDataSource countChart(List<Map<String, ?>> rows, String column)
    {
        Map<String, Long> counts = rows.stream().collect(
                Collectors.groupingBy(r -> String.valueOf(r.get(column)), TreeMap::new, Collectors.counting()));
        List<Map<String, ?>> out = new ArrayList<>();
        counts.forEach((k, v) -> {
            Map<String, Object> m = new HashMap<>();
            m.put("label", k);
            m.put("total", v.intValue());
            out.add(m);
        });
        return source(out);
    }

    // Revenue report
    public JasperPrint revenue(Date from, Date to) throws JRException
    {
        checkRange(from, to);
        List<Map<String, ?>> rows = dao.revenue(startOf(from), endOf(to));
        if (rows.isEmpty())
        {
            throw new ValidationException("No invoices in this period.");
        }
        return fillRevenue(rows, from, to);
    }

    // Fill from rows
    JasperPrint fillRevenue(List<Map<String, ?>> rows, Date from, Date to) throws JRException
    {
        Map<String, Object> p = rangeParams(from, to);
        p.put("PERIOD_DS", periodChart(rows));
        return JasperFillManager.fillReport(load("revenue"), p, source(rows));
    }

    // Service report
    public JasperPrint services(Date from, Date to) throws JRException
    {
        checkRange(from, to);
        List<Map<String, ?>> rows = dao.servicePerformance(startOf(from), endOf(to));
        if (rows.isEmpty())
        {
            throw new ValidationException("No appointments in this period.");
        }
        return fillServices(rows, from, to);
    }

    // Fill from rows
    JasperPrint fillServices(List<Map<String, ?>> rows, Date from, Date to) throws JRException
    {
        Map<String, Object> p = rangeParams(from, to);
        p.put("CATEGORY_DS", countChart(rows, "category"));
        p.put("STATUS_DS", countChart(rows, "status"));
        return JasperFillManager.fillReport(load("services"), p, source(rows));
    }

    // Invoice report
    public JasperPrint invoice(int invoiceId) throws JRException
    {
        Map<String, ?> h = dao.invoiceHeader(invoiceId);
        if (h == null)
        {
            throw new ValidationException("Invoice not found.");
        }
        return invoiceFrom(h, dao.invoiceLines(invoiceId), dao.invoicePayments(invoiceId));
    }

    // Build from data
    public JasperPrint invoiceFrom(Map<String, ?> h, List<Map<String, ?>> lines, List<Map<String, ?>> pays)
            throws JRException
    {
        Map<String, Object> p = new HashMap<>();
        p.put("INV_NO", String.valueOf(h.get("invoice_id")));
        Object d = h.get("invoice_date");
        p.put("INV_DATE", d == null ? "" : stampFmt.format(d));
        p.put("CUSTOMER", h.get("customer_name"));
        p.put("PHONE", h.get("customer_phone"));
        p.put("PET", h.get("pet_name"));
        p.put("STATUS", h.get("status"));
        BigDecimal total = money(h.get("total_amount"));
        BigDecimal paid = money(h.get("paid_amount"));
        p.put("SUBTOTAL", money(h.get("subtotal")));
        p.put("DISCOUNT", money(h.get("discount")));
        p.put("TAX", money(h.get("tax")));
        p.put("TOTAL", total);
        p.put("PAID", paid);
        p.put("BALANCE", total.subtract(paid));

        StringBuilder sb = new StringBuilder();
        for (Map<String, ?> pay : pays)
        {
            sb.append(stampFmt.format(pay.get("payment_date"))).append("  ")
              .append(pay.get("payment_method")).append("  ")
              .append(money(pay.get("amount")).toPlainString()).append('\n');
        }
        p.put("PAY_TEXT", sb.length() == 0 ? "No payments yet" : sb.toString());
        return JasperFillManager.fillReport(load("invoice"), p, source(lines));
    }

    private BigDecimal money(Object o)
    {
        return o == null ? BigDecimal.ZERO : new BigDecimal(o.toString());
    }

    // Export to PDF
    public void exportPdf(JasperPrint print, String file) throws JRException
    {
        JasperExportManager.exportReportToPdfFile(print, file);
    }

    // Show in window
    public void preview(JasperPrint print, String title)
    {
        JFrame f = new JFrame(title);
        f.setContentPane(new JRViewer(print));
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    // Open invoice window
    public static void showInvoice(int invoiceId)
    {
        try
        {
            ReportService rs = new ReportService();
            rs.preview(rs.invoice(invoiceId), "Invoice #" + invoiceId);
        }
        catch (ValidationException | exception.DatabaseException e)
        {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Report", JOptionPane.WARNING_MESSAGE);
        }
        catch (JRException e)
        {
            JOptionPane.showMessageDialog(null, "Report failed: " + e.getMessage(), "Report", JOptionPane.ERROR_MESSAGE);
        }
    }
}

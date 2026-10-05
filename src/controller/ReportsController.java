package controller;

import exception.DatabaseException;
import exception.ValidationException;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.Invoice;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import service.InvoiceService;
import service.ReportService;
import util.ComboItem;
import view.ReportsForm;

// Reports screen controller
public class ReportsController extends BaseController<ReportsForm>
{
    private final ReportService reportService = new ReportService();
    private final InvoiceService invoiceService = new InvoiceService();

    public ReportsController(ReportsForm view)
    {
        super(view);
        attachSidebar("REPORTS");
        view.setDateRange(daysFromToday(-30), daysFromToday(0));
        wire();
        loadInvoices();
    }

    // Factory entry
    public static ReportsForm open()
    {
        ReportsForm v = new ReportsForm();
        new ReportsController(v);
        return v;
    }

    private static Date daysFromToday(int offset)
    {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, offset);
        return c.getTime();
    }

    // Register listeners
    private void wire()
    {
        view.getBtnRevenuePdf().addActionListener(e -> revenue(true));
        view.getBtnRevenuePreview().addActionListener(e -> revenue(false));
        view.getBtnServicesPdf().addActionListener(e -> services(true));
        view.getBtnServicesPreview().addActionListener(e -> services(false));
        view.getBtnInvoicePdf().addActionListener(e -> runInvoice(true));
        view.getBtnInvoicePreview().addActionListener(e -> runInvoice(false));
    }

    private void revenue(boolean pdf)
    {
        Date from = requireDateRange();
        Date to = view.getToDate();
        run(() -> reportService.revenue(from, to), "Revenue Report", "revenue-report.pdf", pdf);
    }

    private void services(boolean pdf)
    {
        Date from = requireDateRange();
        Date to = view.getToDate();
        run(() -> reportService.services(from, to), "Service Report", "service-report.pdf", pdf);
    }

    // Reports require a valid date range; end date cannot precede start date.
    private Date requireDateRange()
    {
        Date from = view.getFromDate();
        Date to = view.getToDate();

        if (from == null)
        {
            throw new ValidationException("From date", "Please select a start date.");
        }
        if (to == null)
        {
            throw new ValidationException("To date", "Please select an end date.");
        }
        if (from.after(to))
        {
            throw new ValidationException("Date range",
                    "From date cannot be after the To date.");
        }
        return from;
    }

    private void loadInvoices()
    {
        try
        {
            List<ComboItem> items = new ArrayList<>();
            for (Invoice i : invoiceService.getAllInvoices())
            {
                items.add(new ComboItem(i.getInvoiceId(),
                        "#" + i.getInvoiceId() + " - " + i.getCustomerName() + " (" + i.getStatus() + ")"));
            }
            view.setInvoices(items);
        }
        catch (DatabaseException e)
        {
            view.setMessage(e.getMessage());
        }
    }

    private void runInvoice(boolean pdf)
    {
        ComboItem item = view.getSelectedInvoice();
        if (item == null)
        {
            reportWarn("Pick an invoice first.");
            return;
        }
        int id = item.getId();
        run(() -> reportService.invoice(id), "Invoice #" + id, "invoice-" + id + ".pdf", pdf);
    }

    // Build, then show or save
    private void run(Callable<JasperPrint> builder, String title, String fileName, boolean pdf)
    {
        try
        {
            JasperPrint print = builder.call();
            if (pdf)
            {
                savePdf(print, fileName);
            }
            else
            {
                reportService.preview(print, title);
                view.setMessage(title + " opened.");
            }
        }
        catch (ValidationException | DatabaseException e)
        {
            reportWarn(e.getMessage());
        }
        catch (JRException e)
        {
            reportError("Report failed: " + e.getMessage());
        }
        catch (Exception e)
        {
            reportError("Unexpected error: " + e.getMessage());
        }
    }

    private void savePdf(JasperPrint print, String fileName) throws JRException
    {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(fileName));
        if (fc.showSaveDialog(view) == JFileChooser.APPROVE_OPTION)
        {
            reportService.exportPdf(print, fc.getSelectedFile().getPath());
            view.setMessage("Saved: " + fc.getSelectedFile().getPath());
        }
    }

    private void reportWarn(String msg)
    {
        JOptionPane.showMessageDialog(view, msg, "Reports", JOptionPane.WARNING_MESSAGE);
    }

    private void reportError(String msg)
    {
        JOptionPane.showMessageDialog(view, msg, "Reports", JOptionPane.ERROR_MESSAGE);
    }
}

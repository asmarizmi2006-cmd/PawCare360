package controller;

import exception.ValidationException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import model.ServiceItem;
import service.ServiceService;
import util.Validator;
import view.ServiceForm;

// Service screen controller
public class ServiceController extends BaseController<ServiceForm>
{
    private final ServiceService serviceItemService = new ServiceService(); // Facade
    private int selectedServiceId = 0;

    public ServiceController(ServiceForm view)
    {
        super(view);
        attachSidebar("SERVICES");
        view.getCmbStatus().setModel(new DefaultComboBoxModel<>(new String[]{"Active", "Inactive"}));
        wire();
        guard(this::loadServices);
    }

    // Factory entry
    public static ServiceForm open()
    {
        ServiceForm v = new ServiceForm();
        new ServiceController(v);
        return v;
    }

    private void wire()
    {
        view.getBtnAdd().addActionListener(e -> guard(this::addService));
        view.getBtnUpdate().addActionListener(e -> guard(this::updateService));
        view.getBtnDelete().addActionListener(e -> guard(this::deleteService));
        view.getBtnClear().addActionListener(e ->
        {
            clearForm();
            view.getTxtServiceName().requestFocusInWindow();
        });
        view.getTable().addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                guard(ServiceController.this::fillFromRow);
            }
        });
        // Enter key chain
        focusNext(view.getTxtServiceName(), view.getTxtCategory());
        focusNext(view.getTxtCategory(), view.getTxtPrice());
        focusNext(view.getTxtPrice(), view.getTxtDuration());
        focusNext(view.getTxtDuration(), view.getTxtDescription());
        view.getTxtDescription().addActionListener(e -> view.getBtnAdd().doClick());
    }

    private void fillFromRow()
    {
        int r = view.getTable().getSelectedRow();
        if (r < 0 || view.cellText(r, 0).isEmpty())
        {
            return;
        }
        selectedServiceId = Integer.parseInt(view.cellText(r, 0));
        view.fillForm(view.cellText(r, 1), view.cellText(r, 2), view.cellText(r, 3),
                view.cellText(r, 4), view.cellText(r, 5), view.cellText(r, 6));
    }

    private void addService()
    {
        serviceItemService.addService(readService());
        info("Service added successfully!");
        clearForm();
        loadServices();
    }

    private void updateService()
    {
        if (selectedServiceId <= 0)
        {
            throw new ValidationException("Please select a service from the table first.");
        }
        serviceItemService.updateService(readService());
        info("Service updated successfully!");
        clearForm();
        loadServices();
    }

    private void deleteService()
    {
        if (selectedServiceId <= 0)
        {
            throw new ValidationException("Please select a service to delete.");
        }
        if (!confirm("Delete this service?"))
        {
            return;
        }
        serviceItemService.deleteService(selectedServiceId);
        info("Service deleted successfully!");
        clearForm();
        loadServices();
    }

    private void clearForm()
    {
        selectedServiceId = 0;
        view.clearForm();
    }

    // Form to model
    private ServiceItem readService()
    {
        // Validate service details before sending them to the service layer.
        ServiceItem s = new ServiceItem();
        s.setServiceId(selectedServiceId);
        s.setServiceName(Validator.maxLength(
                Validator.requireText(view.getServiceNameText(), "Service name"), 100, "Service name"));
        s.setCategory(Validator.maxLength(
                Validator.requireText(view.getCategoryText(), "Category"), 50, "Category"));

        BigDecimal price = Validator.parseMoney(view.getPriceText(), "Price");
        s.setPrice(price);

        int duration = Validator.parsePositiveInt(view.getDurationText(), "Duration");
        s.setDurationMinutes(duration);

        s.setDescription(Validator.maxLength(view.getDescriptionText().trim(), 255, "Description"));

        String status = Validator.requireText(view.getStatusText(), "Status");
        if (!status.equals("Active") && !status.equals("Inactive"))
        {
            throw new ValidationException("Status", "Please select Active or Inactive.");
        }
        s.setStatus(status);

        return s;
    }

    private void loadServices()
    {
        List<ServiceItem> services = serviceItemService.getAllServices();
        List<Object[]> rows = new ArrayList<>();
        for (ServiceItem s : services)
        {
            rows.add(new Object[]{s.getServiceId(), s.getServiceName(), s.getCategory(),
                s.getPrice(), s.getDurationMinutes(), s.getDescription(), s.getStatus()});
        }
        view.setRows(rows);
        long active = services.stream().filter(s -> "Active".equalsIgnoreCase(s.getStatus())).count();
        long categories = services.stream().map(ServiceItem::getCategory).distinct().count();
        view.setStats(services.size(), active, services.size() - active, categories);
    }
}

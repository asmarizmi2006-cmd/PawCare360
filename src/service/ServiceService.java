package service;

import exception.ValidationException;
import dao.ServiceDAO;
import model.ServiceItem;

import java.util.List;

public class ServiceService {

    private final ServiceDAO serviceDAO = new ServiceDAO();

    public void addService(ServiceItem s) {
        validate(s);
        if (s.getStatus() == null || s.getStatus().isEmpty()) {
            s.setStatus("Active");
        }
        serviceDAO.addService(s);
    }

    public List<ServiceItem> getAllServices() {
        return serviceDAO.getAllServices();
    }

    public List<ServiceItem> getAllServicesForManagement() {
        return serviceDAO.getAllServicesForManagement();
    }

    public void updateService(ServiceItem s) {
        validate(s);
        serviceDAO.updateService(s);
    }

    public void deleteService(int serviceId) {
        serviceDAO.deleteService(serviceId);
    }

    private void validate(ServiceItem s) {
        if (s.getServiceName() == null || s.getServiceName().trim().isEmpty()) {
            throw new ValidationException("Service name is required.");
        }
        if (s.getPrice() == null || s.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new ValidationException("Price cannot be negative.");
        }
        if (s.getDurationMinutes() <= 0) {
            throw new ValidationException("Duration must be greater than 0.");
        }
    }
}
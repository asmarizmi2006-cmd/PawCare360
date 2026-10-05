package service;

import dao.MedicineDAO;
import exception.ValidationException;
import model.Medicine;
import util.Validator;

import java.util.List;

// Medicine business rules
public class MedicineService
{
    private final MedicineDAO medicineDAO = new MedicineDAO();

    public List<Medicine> getAllMedicines()
    {
        return medicineDAO.getAllMedicines();
    }

    public List<Medicine> getActiveMedicines()
    {
        return medicineDAO.getActiveMedicines();
    }

    public void addMedicine(Medicine m)
    {
        validate(m);
        medicineDAO.addMedicine(m);
    }

    public void updateMedicine(Medicine m)
    {
        Validator.requireSelected(m.getMedicineId(), "Medicine");
        validate(m);
        medicineDAO.updateMedicine(m);
    }

    public void deleteMedicine(int id)
    {
        Validator.requireSelected(id, "Medicine");
        medicineDAO.deleteMedicine(id);
    }

    private void validate(Medicine m)
    {
        m.setMedicineName(Validator.requireText(m.getMedicineName(), "Medicine name"));
        Validator.maxLength(m.getMedicineName(), 100, "Medicine name");
        if (m.getUnitPrice() == null || m.getUnitPrice().signum() < 0)
        {
            throw new ValidationException("Unit price", "Unit price cannot be negative.");
        }
        if (m.getStockQuantity() < 0)
        {
            throw new ValidationException("Stock", "Stock cannot be negative.");
        }
        if (m.getStatus() == null || m.getStatus().isBlank())
        {
            m.setStatus("Active");
        }
    }
}

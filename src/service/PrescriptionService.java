package service;

import dao.TreatmentMedicineDAO;
import model.TreatmentMedicine;
import util.Validator;

import java.math.BigDecimal;
import java.util.List;

// Prescription business rules
public class PrescriptionService
{
    private final TreatmentMedicineDAO dao = new TreatmentMedicineDAO();

    public List<TreatmentMedicine> getByTreatment(int treatmentId)
    {
        return dao.findByTreatment(treatmentId);
    }

    // Validate and add
    public void addMedicine(int treatmentId, int medicineId, String quantityText, String dosage, String instructions)
    {
        TreatmentMedicine tm = new TreatmentMedicine();
        tm.setTreatmentId(Validator.requireSelected(treatmentId, "Treatment"));
        tm.setMedicineId(Validator.requireSelected(medicineId, "Medicine"));
        tm.setQuantity(Validator.parsePositiveInt(quantityText, "Quantity"));
        tm.setDosage(Validator.maxLength(Validator.requireText(dosage, "Dosage"), 100, "Dosage"));
        String note = instructions == null ? "" : instructions.trim();
        tm.setInstructions(Validator.maxLength(note, 255, "Instructions"));
        dao.add(tm);
    }

    public void removeLine(int lineId)
    {
        Validator.requireSelected(lineId, "Prescription line");
        dao.remove(lineId);
    }

    // Total medicine cost
    public BigDecimal totalForTreatment(int treatmentId)
    {
        return dao.findByTreatment(treatmentId).stream()
                .map(TreatmentMedicine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

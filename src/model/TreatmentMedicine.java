package model;

import java.math.BigDecimal;

// Prescription line
public class TreatmentMedicine
{
    private int id;
    private int treatmentId;
    private int medicineId;
    private String medicineName;
    private int quantity;
    private String dosage;
    private String instructions;
    private BigDecimal unitPrice = BigDecimal.ZERO;

    public TreatmentMedicine()
    {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTreatmentId() { return treatmentId; }
    public void setTreatmentId(int treatmentId) { this.treatmentId = treatmentId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    // Price times quantity
    public BigDecimal getLineTotal()
    {
        return unitPrice == null ? BigDecimal.ZERO : unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}

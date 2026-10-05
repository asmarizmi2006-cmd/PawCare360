package model;

import java.math.BigDecimal;

// Medicine stock item
public class Medicine
{
    private int medicineId;
    private String medicineName;
    private String category;
    private BigDecimal unitPrice = BigDecimal.ZERO;
    private int stockQuantity;
    private String status = "Active";

    public Medicine()
    {
    }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Combo display text
    @Override
    public String toString()
    {
        return medicineName + " (stock " + stockQuantity + ")";
    }
}

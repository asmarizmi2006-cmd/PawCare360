package model;

import java.math.BigDecimal;

// One invoice line
public class InvoiceItem
{
    public static final String SERVICE = "Service";
    public static final String MEDICINE = "Medicine";
    public static final String BOARDING = "Boarding";
    public static final String PRESCRIPTION = "Prescription";
    public static final String OTHER = "Other";

    private int detailId;
    private int invoiceId;
    private String itemType = OTHER;
    private Integer itemId;
    private String description;
    private int quantity = 1;
    private BigDecimal unitPrice = BigDecimal.ZERO;

    public InvoiceItem()
    {
    }

    public InvoiceItem(String itemType, Integer itemId, String description, int quantity, BigDecimal unitPrice)
    {
        this.itemType = itemType;
        this.itemId = itemId;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    // Derived total
    public BigDecimal getLineTotal()
    {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public int getDetailId() { return detailId; }
    public void setDetailId(int detailId) { this.detailId = detailId; }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public String getItemType() { return itemType; }
    public void setItemType(String itemType) { this.itemType = itemType; }

    public Integer getItemId() { return itemId; }
    public void setItemId(Integer itemId) { this.itemId = itemId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}

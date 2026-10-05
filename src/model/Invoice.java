package model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// Invoice header + lines
public class Invoice
{
    public static final String UNPAID = "Unpaid";
    public static final String PARTIAL = "Partial";
    public static final String PAID = "Paid";
    public static final String CANCELLED = "Cancelled";

    private int invoiceId;
    private int customerId;
    private Integer petId;
    private Integer appointmentId;
    private Timestamp invoiceDate;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal tax = BigDecimal.ZERO;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal paidAmount = BigDecimal.ZERO;
    private String status = UNPAID;
    private String notes;

    // Display only
    private String customerName;
    private String petName;

    private List<InvoiceItem> items = new ArrayList<>();

    public Invoice()
    {
    }

    // Amount still owed
    public BigDecimal getBalance()
    {
        return totalAmount.subtract(paidAmount);
    }

    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public Integer getPetId() { return petId; }
    public void setPetId(Integer petId) { this.petId = petId; }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public Timestamp getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(Timestamp invoiceDate) { this.invoiceDate = invoiceDate; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }

    public BigDecimal getTax() { return tax; }
    public void setTax(BigDecimal tax) { this.tax = tax; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public List<InvoiceItem> getItems() { return items; }
    public void setItems(List<InvoiceItem> items) { this.items = items; }

    // Builder pattern
    public static Builder builder()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private final Invoice inv = new Invoice();

        public Builder customer(int customerId) { inv.customerId = customerId; return this; }
        public Builder pet(Integer petId) { inv.petId = petId; return this; }
        public Builder appointment(Integer appointmentId) { inv.appointmentId = appointmentId; return this; }
        public Builder notes(String notes) { inv.notes = notes; return this; }
        public Builder discount(BigDecimal discount) { inv.discount = discount; return this; }
        public Builder items(List<InvoiceItem> items) { inv.items = items; return this; }

        public Invoice build()
        {
            return inv;
        }
    }
}

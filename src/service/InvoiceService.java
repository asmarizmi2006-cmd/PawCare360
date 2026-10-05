package service;

import dao.InvoiceDAO;
import dao.PaymentDAO;
import exception.PaymentException;
import exception.ValidationException;
import model.Invoice;
import model.InvoiceItem;
import model.Payment;
import util.Validator;

import java.math.BigDecimal;
import service.pricing.TaxStrategies;
import service.pricing.TaxStrategy;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

// Billing business rules
public class InvoiceService
{
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();

    // Totals from lines
    public static BigDecimal subtotal(List<InvoiceItem> items)
    {
        return items.stream()
                .map(InvoiceItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Tax on discounted amount
    public static BigDecimal taxAmount(BigDecimal subtotal, BigDecimal discount, BigDecimal ratePercent)
    {
        BigDecimal taxable = subtotal.subtract(discount).max(BigDecimal.ZERO);
        return taxAmount(taxable, TaxStrategies.percentage(ratePercent)); // Strategy
    }

    // Tax by strategy
    public static BigDecimal taxAmount(BigDecimal taxable, TaxStrategy strategy)
    {
        return strategy.taxOn(taxable);
    }

    // Create invoice, optional first payment
    public int createInvoice(Invoice inv, BigDecimal ratePercent, Payment firstPayment)
    {
        Validator.requireSelected(inv.getCustomerId(), "Customer");
        if (inv.getItems() == null || inv.getItems().isEmpty())
        {
            throw new ValidationException("Items", "Add at least one item to the invoice.");
        }
        for (InvoiceItem it : inv.getItems())
        {
            if (it.getQuantity() <= 0)
            {
                throw new ValidationException("Quantity", "Quantity must be greater than 0.");
            }
            if (it.getUnitPrice() == null || it.getUnitPrice().signum() < 0)
            {
                throw new ValidationException("Price", "Unit price cannot be negative.");
            }
        }
        if (ratePercent.signum() < 0 || ratePercent.compareTo(BigDecimal.valueOf(100)) > 0)
        {
            throw new ValidationException("Tax", "Tax rate must be between 0 and 100.");
        }

        BigDecimal sub = subtotal(inv.getItems());
        BigDecimal discount = inv.getDiscount() == null ? BigDecimal.ZERO : inv.getDiscount();
        if (discount.signum() < 0 || discount.compareTo(sub) > 0)
        {
            throw new ValidationException("Discount", "Discount must be between 0 and the subtotal.");
        }
        BigDecimal tax = taxAmount(sub, discount, ratePercent);

        inv.setSubtotal(sub);
        inv.setDiscount(discount);
        inv.setTax(tax);
        inv.setTotalAmount(sub.subtract(discount).add(tax));

        if (firstPayment != null && firstPayment.getAmount().compareTo(inv.getTotalAmount()) > 0)
        {
            throw new PaymentException("Amount paid cannot exceed the invoice total.");
        }
        return invoiceDAO.createInvoice(inv, firstPayment);
    }

    public List<Invoice> getAllInvoices()
    {
        return invoiceDAO.getAllInvoices();
    }

    public Optional<Invoice> findById(int id)
    {
        return invoiceDAO.findById(id);
    }

    public List<InvoiceItem> getItems(int invoiceId)
    {
        return invoiceDAO.getItems(invoiceId);
    }

    public List<Payment> getPayments(int invoiceId)
    {
        return paymentDAO.getByInvoice(invoiceId);
    }

    // Pay against invoice
    public void recordPayment(int invoiceId, BigDecimal amount, String method, String reference)
    {
        Validator.requireSelected(invoiceId, "Invoice");
        if (amount == null || amount.signum() <= 0)
        {
            throw new PaymentException("Payment amount must be greater than 0.");
        }
        Payment p = new Payment();
        p.setInvoiceId(invoiceId);
        p.setAmount(amount);
        p.setPaymentMethod(method);
        p.setReferenceNo(reference == null ? "" : reference.trim());
        paymentDAO.addPayment(p);
    }

    public void cancelInvoice(int invoiceId)
    {
        Validator.requireSelected(invoiceId, "Invoice");
        invoiceDAO.cancelInvoice(invoiceId);
    }
}

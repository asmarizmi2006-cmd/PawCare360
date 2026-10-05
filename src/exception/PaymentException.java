package exception;

// User-defined exception
public class PaymentException extends ValidationException
{
    public PaymentException(String message)
    {
        super("Payment", message);
    }
}

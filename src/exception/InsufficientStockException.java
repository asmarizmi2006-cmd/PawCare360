package exception;

// User-defined exception
public class InsufficientStockException extends ValidationException
{
    public InsufficientStockException(String message)
    {
        super(message);
    }
}

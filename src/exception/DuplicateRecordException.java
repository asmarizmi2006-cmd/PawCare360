package exception;

// User-defined exception
public class DuplicateRecordException extends ValidationException
{
    public DuplicateRecordException(String message)
    {
        super(message);
    }
}

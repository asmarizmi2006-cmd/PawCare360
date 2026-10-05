package exception;

// Overlapping boarding booking
public class BoardingConflictException extends ValidationException
{
    public BoardingConflictException(String message)
    {
        super(message);
    }
}

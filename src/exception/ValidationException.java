package exception;

// User-defined exception 
public class ValidationException extends IllegalArgumentException 
{ 
    private final String field; 
 
    public ValidationException(String message) 
    { 
        this(null, message); 
    } 
 
    public ValidationException(String field, String message) 
    { 
        super(message); 
        this.field = field; 
    } 
 
    // Which input failed 
    public String getField() 
    { 
        return field; 
    } 
}
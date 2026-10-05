package exception;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

// User-defined exception
public class DatabaseException extends RuntimeException
{
    public DatabaseException(String message, Throwable cause)
    {
        super(message, cause);
    }

    // Friendly message from SQL error
    public static DatabaseException wrap(Throwable cause, String action)
    {
        String msg = "Could not " + action + ".";
        Throwable root = cause;
        while (root.getCause() != null && root.getCause() != root)
        {
            root = root.getCause();
        }
        if (root instanceof SQLIntegrityConstraintViolationException)
        {
            String text = String.valueOf(root.getMessage());
            if (text.contains("Duplicate"))
            {
                msg = "Could not " + action + ": this record already exists.";
            }
            else
            {
                msg = "Could not " + action + ": it is linked to other records.";
            }
        }
        else if (root instanceof SQLException sql && sql.getSQLState() != null
                && sql.getSQLState().startsWith("08"))
        {
            msg = "Cannot reach the database. Please start MySQL and try again.";
        }
        else if (root instanceof java.net.ConnectException)
        {
            msg = "Cannot reach the database. Please start MySQL and try again.";
        }
        return new DatabaseException(msg, cause);
    }
}

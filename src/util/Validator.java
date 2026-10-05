package util;

import exception.ValidationException;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

// Reusable input validation
public final class Validator
{
    private static final Pattern PHONE = Pattern.compile("^(07\\d{8}|\\+947\\d{8})$");
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern NAME = Pattern.compile("^[\\p{L} .'-]+$");

    private Validator()
    {
    }

    // Required text
    public static String requireText(String value, String field)
    {
        if (value == null || value.trim().isEmpty())
        {
            throw new ValidationException(field, field + " is required.");
        }
        return value.trim();
    }

    // Max length check
    public static String maxLength(String value, int max, String field)
    {
        if (value != null && value.length() > max)
        {
            throw new ValidationException(field, field + " must be at most " + max + " characters.");
        }
        return value;
    }

    // Letters only name
    public static String requireName(String value, String field)
    {
        String v = requireText(value, field);
        if (!NAME.matcher(v).matches())
        {
            throw new ValidationException(field, field + " can contain only letters, spaces, dots and apostrophes.");
        }
        return v;
    }

    // Sri Lankan mobile
    public static String requirePhone(String value, String field)
    {
        String v = requireText(value, field);
        if (!PHONE.matcher(v).matches())
        {
            throw new ValidationException(field, "Please enter a valid Sri Lankan mobile number (07XXXXXXXX).");
        }
        return v;
    }

    // Optional phone
    public static String optionalPhone(String value, String field)
    {
        return (value == null || value.trim().isEmpty()) ? "" : requirePhone(value, field);
    }

    // Optional email
    public static String optionalEmail(String value, String field)
    {
        if (value == null || value.trim().isEmpty())
        {
            return "";
        }
        if (!EMAIL.matcher(value.trim()).matches())
        {
            throw new ValidationException(field, "Please enter a valid email address.");
        }
        return value.trim();
    }

    // Selected id
    public static int requireSelected(int id, String field)
    {
        if (id <= 0)
        {
            throw new ValidationException(field, "Please select a " + field.toLowerCase() + ".");
        }
        return id;
    }

    // Strict date parse
    public static Date parseDate(String text, String field)
    {
        String v = requireText(text, field);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        f.setLenient(false);
        try
        {
            return new Date(f.parse(v).getTime());
        }
        catch (ParseException e)
        {
            throw new ValidationException(field, field + " must be in YYYY-MM-DD format.");
        }
    }

    // Money amount
    public static BigDecimal parseMoney(String text, String field)
    {
        String v = requireText(text, field).replace(",", "");
        try
        {
            BigDecimal value = new BigDecimal(v);
            if (value.signum() < 0)
            {
                throw new ValidationException(field, field + " cannot be negative.");
            }
            return value.setScale(2, java.math.RoundingMode.HALF_UP);
        }
        catch (NumberFormatException e)
        {
            throw new ValidationException(field, field + " must be a valid number.");
        }
    }

    // Whole number
    public static int parsePositiveInt(String text, String field)
    {
        String v = requireText(text, field);
        try
        {
            int value = Integer.parseInt(v);
            if (value <= 0)
            {
                throw new ValidationException(field, field + " must be greater than 0.");
            }
            return value;
        }
        catch (NumberFormatException e)
        {
            throw new ValidationException(field, field + " must be a whole number.");
        }
    }
}

package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Password hashing
public final class PasswordUtil
{
    private PasswordUtil()
    {
    }

    // SHA-256 hex
    public static String hash(String raw)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
            {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    // Hash or legacy plain
    public static boolean matches(String raw, String stored)
    {
        if (raw == null || stored == null)
        {
            return false;
        }
        return stored.equals(hash(raw)) || stored.equals(raw);
    }
}

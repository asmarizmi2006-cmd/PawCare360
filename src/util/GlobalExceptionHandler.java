package util;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

// Catches unhandled errors
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler
{
    public static void install()
    {
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
    }

    @Override
    public void uncaughtException(Thread t, Throwable e)
    {
        e.printStackTrace();
        String message = (e instanceof exception.DatabaseException || e instanceof IllegalArgumentException)
                ? e.getMessage()
                : "Something went wrong. Please try again.";
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, message,
                "Error", JOptionPane.ERROR_MESSAGE));
    }
}

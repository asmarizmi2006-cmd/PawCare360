package view;

import java.awt.Component;
import java.awt.Container;

// Find child component
public final class BeanFinder
{
    private BeanFinder()
    {
    }

    // First match by type
    public static <T> T find(Container root, Class<T> type)
    {
        for (Component c : root.getComponents())
        {
            if (type.isInstance(c))
            {
                return type.cast(c);
            }
            if (c instanceof Container)
            {
                T found = find((Container) c, type);
                if (found != null)
                {
                    return found;
                }
            }
        }
        return null;
    }
}

package service.pricing;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Ready tax rules
public final class TaxStrategies
{
    private TaxStrategies()
    {
    }

    // Percent of amount
    public static TaxStrategy percentage(BigDecimal ratePercent)
    {
        return amount -> amount.multiply(ratePercent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    // No tax
    public static TaxStrategy exempt()
    {
        return amount -> BigDecimal.ZERO;
    }
}

package service.pricing;

import java.math.BigDecimal;

// Strategy: tax rule
@FunctionalInterface
public interface TaxStrategy
{
    BigDecimal taxOn(BigDecimal taxableAmount);
}

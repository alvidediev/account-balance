package by.dediev.account_balance.service.currencyConverter;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class CurrencyConverter {
    private final Map<String, BigDecimal> conversionRates = Map.of(
            "USD", BigDecimal.valueOf(1.0),
            "EUR", BigDecimal.valueOf(1.07),
            "BYN", BigDecimal.valueOf(0.30),
            "RUB", BigDecimal.valueOf(0.011)
    );

    public BigDecimal convertToUsd(BigDecimal amount, String currency) {
        return amount.multiply(conversionRates.get(currency));
    }
}

package by.dediev.account_balance.model.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        String id,
        String accountId,
        String type,
        BigDecimal amount,
        String currency,
        LocalDateTime timestamp
) {
}


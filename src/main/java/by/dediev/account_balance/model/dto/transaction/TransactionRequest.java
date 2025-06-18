package by.dediev.account_balance.model.dto.transaction;

import by.dediev.account_balance.model.enums.CurrencyTypeEnum;
import by.dediev.account_balance.model.enums.TransactionTypeEnum;

import java.math.BigDecimal;

public record TransactionRequest(
        TransactionTypeEnum type,
        BigDecimal amount,
        CurrencyTypeEnum currency
) {}


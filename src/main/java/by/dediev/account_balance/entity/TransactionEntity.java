package by.dediev.account_balance.entity;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сущность транзакции
 *
 * @param uuid      - уникальный идектификатор
 * @param accountId - уникальный идентификатор аккаунта
 * @param type      - тип
 * @param amount    - количество валюты
 * @param currency  - по какой цене
 * @param timeStamp - временная метка операции
 */
@Builder(toBuilder = true)
public record TransactionEntity(
        String uuid,
        String accountId,
        String type,
        BigDecimal amount,
        String currency,
        LocalDateTime timeStamp
) {
}

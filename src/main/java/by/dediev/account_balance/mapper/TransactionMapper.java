package by.dediev.account_balance.mapper;

import by.dediev.account_balance.entity.TransactionEntity;
import by.dediev.account_balance.model.dto.transaction.TransactionResponse;

/**
 * Можно подключить mapstruct, но мне уже лень :)
 */
public class TransactionMapper {
    public static TransactionResponse toResponse(TransactionEntity entity) {
        return new TransactionResponse(
                entity.uuid(),
                entity.accountId(),
                entity.type(),
                entity.amount(),
                entity.currency(),
                entity.timeStamp()
        );
    }
}

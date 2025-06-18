package by.dediev.account_balance.repository.transaction;

import by.dediev.account_balance.entity.TransactionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionRepository {
    Mono<TransactionEntity> findByAccountId(String accountId);
    Mono<TransactionEntity> findById(String id);
    Mono<TransactionEntity> save(TransactionEntity entity);
    Flux<TransactionEntity> findAll();
    Flux<TransactionEntity> findAllByAccountId(String accountId);
}

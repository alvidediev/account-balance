package by.dediev.account_balance.service.transactionService;

import by.dediev.account_balance.entity.TransactionEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransactionService {
    Mono<TransactionEntity> save(TransactionEntity entity);
    Mono<TransactionEntity> findById(String id);
    Mono<TransactionEntity> findByAccountId(String accountId);
    Flux<TransactionEntity> findAll();
    Flux<TransactionEntity> findAllByAccountId(String accountId);
}

package by.dediev.account_balance.service.transactionService.impl;

import by.dediev.account_balance.entity.TransactionEntity;
import by.dediev.account_balance.repository.transaction.TransactionRepository;
import by.dediev.account_balance.service.transactionService.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Mono<TransactionEntity> save(TransactionEntity entity) {
        return transactionRepository.save(entity)
                .onErrorResume(err -> {
                    log.error("Transaction save error {}", err.getMessage());
                    return Mono.error(err);
                });
    }

    @Override
    public Mono<TransactionEntity> findById(String id) {
        return transactionRepository.findById(id)
                .onErrorResume(err -> {
                    log.error("Transaction find error {}", err.getMessage());
                    return Mono.error(err);
                });
    }

    @Override
    public Mono<TransactionEntity> findByAccountId(String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .onErrorResume(err -> {
                    log.error("Transaction find by account id error {}", err.getMessage());
                    return Mono.error(err);
                });
    }

    @Override
    public Flux<TransactionEntity> findAll() {
        return transactionRepository.findAll()
                .onErrorResume(err -> {
                    log.error("Transaction find all error {}", err.getMessage());
                    return Mono.error(err);
                });
    }

    @Override
    public Flux<TransactionEntity> findAllByAccountId(String accountId) {
        return transactionRepository.findAllByAccountId(accountId)
                .onErrorResume(err -> {
                    log.error("Transaction find all by account id error {}", err.getMessage());
                    return Mono.error(err);
                });
    }
}

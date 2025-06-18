package by.dediev.account_balance.service.balanceService;

import by.dediev.account_balance.entity.AccountEntity;
import by.dediev.account_balance.entity.TransactionEntity;
import by.dediev.account_balance.model.dto.transaction.TransactionRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface BalanceService {

    Mono<BigDecimal> getBalanceByAccountName(String name);

    Mono<TransactionEntity> processTransaction(String accountName, TransactionRequest request);

    Mono<AccountEntity> createAccount(String name);

    Flux<TransactionEntity> getAllTransactions(String name);
}

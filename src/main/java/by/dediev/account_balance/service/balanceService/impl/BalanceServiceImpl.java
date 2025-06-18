package by.dediev.account_balance.service.balanceService.impl;

import by.dediev.account_balance.entity.AccountEntity;
import by.dediev.account_balance.entity.TransactionEntity;
import by.dediev.account_balance.exception.AccountNotFoundException;
import by.dediev.account_balance.exception.InsufficientFundsException;
import by.dediev.account_balance.model.dto.transaction.TransactionRequest;
import by.dediev.account_balance.model.enums.TransactionTypeEnum;
import by.dediev.account_balance.service.accountService.AccountService;
import by.dediev.account_balance.service.balanceService.BalanceService;
import by.dediev.account_balance.service.currencyConverter.CurrencyConverter;
import by.dediev.account_balance.service.transactionService.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final CurrencyConverter currencyConverter;

    @Override
    public Mono<BigDecimal> getBalanceByAccountName(String name) {
        return accountService.findByName(name)
                .flatMapMany(account -> transactionService.findAllByAccountId(account.uuid()))
                .map(this::toUsdAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Mono<TransactionEntity> processTransaction(String accountName, TransactionRequest request) {
        return accountService.findByName(accountName)
                .switchIfEmpty(Mono.error(new AccountNotFoundException(accountName)))
                .flatMap(account -> {
                    String accountId = account.uuid();

                    if (request.type() == TransactionTypeEnum.WITHDRAW) {
                        return getBalanceByAccountName(accountName)
                                .flatMap(currentBalance -> {
                                    BigDecimal usdAmount = currencyConverter.convertToUsd(request.amount(), request.currency().name());
                                    if (currentBalance.compareTo(usdAmount) < 0) {
                                        log.warn("Current balance with name {} is less than request amount", accountName);
                                        return Mono.error(new InsufficientFundsException(accountName));
                                    }
                                    return createAndSaveTransaction(request, accountId);
                                });
                    }

                    return createAndSaveTransaction(request, accountId);
                });
    }

    @Override
    public Mono<AccountEntity> createAccount(String name) {
        AccountEntity entity = AccountEntity.builder()
                .uuid(UUID.randomUUID().toString())
                .name(name)
                .createdAt(LocalDateTime.now())
                .build();

        return accountService.save(entity);

    }

    @Override
    public Flux<TransactionEntity> getAllTransactions(String name) {
        return accountService.findByName(name)
                .flatMapMany(account -> transactionService.findAllByAccountId(account.uuid()));
    }

    private Mono<TransactionEntity> createAndSaveTransaction(TransactionRequest request, String accountId) {
        TransactionEntity entity = TransactionEntity.builder()
                .uuid(UUID.randomUUID().toString())
                .accountId(accountId)
                .type(request.type().name())
                .amount(request.amount())
                .currency(request.currency().name())
                .timeStamp(LocalDateTime.now())
                .build();

        return transactionService.save(entity)
                .doOnSuccess(success -> log.info("Transaction with uuid {} saved successfully", success.uuid()));
    }


    private BigDecimal toUsdAmount(TransactionEntity tx) {
        BigDecimal usdAmount = currencyConverter.convertToUsd(tx.amount(), tx.currency());
        if (tx.type().equalsIgnoreCase("DEPOSIT")) {
            return usdAmount;
        } else if (tx.type().equalsIgnoreCase("WITHDRAW")) {
            return usdAmount.negate();
        } else {
            log.warn("Unknown transaction type: {}", tx.type());
            return BigDecimal.ZERO;
        }
    }
}

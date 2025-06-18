package by.dediev.account_balance.rest;

import by.dediev.account_balance.entity.AccountEntity;
import by.dediev.account_balance.exception.AccountNotFoundException;
import by.dediev.account_balance.exception.InsufficientFundsException;
import by.dediev.account_balance.mapper.TransactionMapper;
import by.dediev.account_balance.model.dto.account.CreateAccountRequest;
import by.dediev.account_balance.model.dto.transaction.TransactionRequest;
import by.dediev.account_balance.model.dto.transaction.TransactionResponse;
import by.dediev.account_balance.service.balanceService.BalanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/balances/{name}")
    public Mono<ResponseEntity<BigDecimal>> getBalance(@PathVariable String name) {
        return balanceService.getBalanceByAccountName(name)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/balances/{name}/transactions")
    public Mono<ResponseEntity<TransactionResponse>> createTransaction(
            @PathVariable String name,
            @RequestBody TransactionRequest request
    ) {
        return balanceService.processTransaction(name, request)
                .map(entity -> ResponseEntity.ok(TransactionMapper.toResponse(entity)))
                .onErrorResume(AccountNotFoundException.class,
                        e -> Mono.just(ResponseEntity.notFound().build()))
                .onErrorResume(InsufficientFundsException.class,
                        e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()));
    }

    @PostMapping("/balances")
    public Mono<ResponseEntity<AccountEntity>> createAccount(@RequestBody CreateAccountRequest request) {
        return balanceService.createAccount(request.name())
                .map(ResponseEntity::ok)
                .onErrorResume(e -> {
                    log.error("Error creating account", e);
                    return Mono.just(ResponseEntity.badRequest().build());
                });
    }

    @GetMapping("/balances/{name}/transactions")
    public Flux<TransactionResponse> getAllTransactions(@PathVariable String name) {
        return balanceService.getAllTransactions(name)
                .map(TransactionMapper::toResponse);
    }
}

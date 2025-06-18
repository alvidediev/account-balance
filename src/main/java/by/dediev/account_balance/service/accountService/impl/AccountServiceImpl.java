package by.dediev.account_balance.service.accountService.impl;

import by.dediev.account_balance.entity.AccountEntity;
import by.dediev.account_balance.repository.account.AccountRepository;
import by.dediev.account_balance.service.accountService.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Mono<AccountEntity> findById(String id) {
        return accountRepository.findById(id)
                .onErrorResume(err -> {
                    log.error("Error {}, while trying find Account by id {}", err.getMessage(), id);
                    return Mono.error(err);
                });
    }

    @Override
    public Mono<AccountEntity> findByName(String name) {
        return accountRepository.findByName(name)
                .onErrorResume(err -> {
                    log.error("Error {}, while trying find Account by this name {}", err.getMessage(), name);
                    return Mono.error(err);
                });
    }

    @Override
    public Mono<AccountEntity> save(AccountEntity accountEntity) {
        return accountRepository.save(accountEntity)
                .onErrorResume(err -> {
                    log.error("Error {}, while trying save entity {}", err.getMessage(), accountEntity);
                    return Mono.error(err);
                });
    }

    @Override
    public Mono<AccountEntity> update(AccountEntity accountEntity) {
        return null;
    }

    @Override
    public Flux<AccountEntity> findAll() {
        return accountRepository.findAll()
                .onErrorResume(err -> {
                    log.error("Error while trying get all {}", err.getMessage());
                    return Mono.error(err);
                });
    }
}

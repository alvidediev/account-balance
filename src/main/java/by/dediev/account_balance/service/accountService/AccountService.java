package by.dediev.account_balance.service.accountService;

import by.dediev.account_balance.entity.AccountEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountService {
    Mono<AccountEntity> findById(String id);
    Mono<AccountEntity> findByName(String name);
    Mono<AccountEntity> save(AccountEntity accountEntity);
    Mono<AccountEntity> update(AccountEntity accountEntity);
    Flux<AccountEntity> findAll();
}

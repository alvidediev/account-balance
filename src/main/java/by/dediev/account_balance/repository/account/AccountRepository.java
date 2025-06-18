package by.dediev.account_balance.repository.account;

import by.dediev.account_balance.entity.AccountEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Слой доступа к БД
 */
public interface AccountRepository {

    /**
     * Создание аккаунта
     */
    Mono<AccountEntity> save(AccountEntity account);

    /**
     * Найти по id
     */
    Mono<AccountEntity> findById(String id);

    /**
     * Найти по имени
     */
    Mono<AccountEntity> findByName(String name);

    /**
     * Найти все аккаунты
     */
    Flux<AccountEntity> findAll();
}

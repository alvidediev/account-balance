package by.dediev.account_balance.repository.account.impl;

import by.dediev.account_balance.entity.AccountEntity;
import by.dediev.account_balance.repository.account.AccountRepository;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final DatabaseClient databaseClient;
    private final TransactionalOperator tx;

    @Override
    public Mono<AccountEntity> save(AccountEntity account) {
        return databaseClient
                .sql("""
                        INSERT INTO account (id, name, created_at)
                        VALUES (:id, :name, :createdAt)
                        RETURNING id, name, created_at
                        """)
                .bind("id", account.uuid())
                .bind("name", account.name())
                .bind("createdAt", account.createdAt())
                .map(AccountRepositoryImpl::mapToEntity)
                .one()
                .as(tx::transactional);
    }

    @Override
    public Mono<AccountEntity> findById(String id) {
        return databaseClient
                .sql("""
                        SELECT id, name, created_at
                        FROM account
                        WHERE id = :id
                        """)
                .bind("id", id)
                .map(AccountRepositoryImpl::mapToEntity)
                .one();
    }

    @Override
    public Mono<AccountEntity> findByName(String name) {
        return databaseClient
                .sql("""
                        SELECT id, name, created_at
                        FROM account
                        WHERE name = :name
                        """)
                .bind("name", name)
                .map(AccountRepositoryImpl::mapToEntity)
                .one();
    }

    @Override
    public Flux<AccountEntity> findAll() {
        return databaseClient
                .sql("""
                        SELECT *
                        FROM account
                        """)
                .map(AccountRepositoryImpl::mapToEntity)
                .all();
    }

    /**
     * Маппер
     */
    private static AccountEntity mapToEntity(Row row, RowMetadata metadata) {
        return new AccountEntity(
                row.get("id", String.class),
                row.get("name", String.class),
                row.get("created_at", LocalDateTime.class));
    }
}

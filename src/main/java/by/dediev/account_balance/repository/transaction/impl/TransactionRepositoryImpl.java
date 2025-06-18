package by.dediev.account_balance.repository.transaction.impl;

import by.dediev.account_balance.entity.TransactionEntity;
import by.dediev.account_balance.repository.transaction.TransactionRepository;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepository {

    private final DatabaseClient databaseClient;
    private final TransactionalOperator tx;

    @Override
    public Mono<TransactionEntity> findByAccountId(String accountId) {
        return databaseClient
                .sql("""
                        SELECT id, account_id, type, amount, currency, timestamp
                        FROM transaction
                        WHERE account_id = :accountId
                        """)
                .bind("accountId", accountId)
                .map(TransactionRepositoryImpl::mapToEntity)
                .one();
    }

    @Override
    public Mono<TransactionEntity> findById(String id) {
        return databaseClient
                .sql("""
                        SELECT id, account_id, type, amount, currency, timestamp
                        FROM transaction
                        WHERE id = :id
                        """)
                .bind("id", id)
                .map(TransactionRepositoryImpl::mapToEntity)
                .one();
    }

    @Override
    public Mono<TransactionEntity> save(TransactionEntity entity) {
        return databaseClient
                .sql(""" 
                        INSERT INTO transaction(id, account_id, type, amount, currency, timestamp)
                        VALUES(:id, :accountId, :type, :amount, :currency, :timestamp) 
                        RETURNING id, account_id, type, amount, currency, timestamp
                        """)
                .bind("id", entity.uuid())
                .bind("accountId", entity.accountId())
                .bind("type", entity.type())
                .bind("amount", entity.amount())
                .bind("currency", entity.currency())
                .bind("timestamp", entity.timeStamp())
                .map(TransactionRepositoryImpl::mapToEntity)
                .one()
                .as(tx::transactional);
    }

    @Override
    public Flux<TransactionEntity> findAll() {
        return databaseClient
                .sql("""
                        SELECT *
                        FROM transaction
                        """)
                .map(TransactionRepositoryImpl::mapToEntity)
                .all();
    }

    @Override
    public Flux<TransactionEntity> findAllByAccountId(String accountId) {
        return databaseClient
                .sql("""
                        SELECT id, account_id, type, amount, currency, timestamp
                        FROM transaction
                        WHERE account_id = :accountId
                        """)
                .bind("accountId", accountId)
                .map(TransactionRepositoryImpl::mapToEntity)
                .all();
    }


    /**
     * Маппер
     */
    private static TransactionEntity mapToEntity(Row row, RowMetadata metadata) {
        return new TransactionEntity(
                row.get("id", String.class),
                row.get("account_id", String.class),
                row.get("type", String.class),
                row.get("amount", BigDecimal.class),
                row.get("currency", String.class),
                row.get("timestamp", LocalDateTime.class));
    }
}

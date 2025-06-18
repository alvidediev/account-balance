package by.dediev.account_balance.entity;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Сущность аккаунта
 *
 * @param uuid      - Уникальный идентификатор.
 * @param name      - Имя. Является уникальным полем.
 * @param createdAt - Время создания.
 */
@Builder(toBuilder = true)
public record AccountEntity(
        String uuid,
        String name,
        LocalDateTime createdAt
) {
}

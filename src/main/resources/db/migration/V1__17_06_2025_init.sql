CREATE TABLE IF NOT EXISTS account (
    id VARCHAR(36) PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    created_at TIMESTAMP
);

CREATE TABLE transaction
(
    id         VARCHAR(36) PRIMARY KEY,
    account_id VARCHAR(36) NOT NULL REFERENCES account(id) ON DELETE CASCADE,
    type TEXT NOT NULL,
    amount NUMERIC(19, 4),
    currency TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL
)
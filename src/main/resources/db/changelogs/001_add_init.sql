-- liquibase formatted sql

-- changeset harshal:0001-init-orders
CREATE TABLE orders (
    order_id   UUID PRIMARY KEY,
    user_id    BIGINT NOT NULL,
    amount     NUMERIC(38,2),
    created_at TIMESTAMP NOT NULL
);

-- changeset harshal:0001-index-user
CREATE INDEX idx_orders_user_id ON orders(user_id);

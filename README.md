# Spring Boot Sharding Demo

This project demonstrates **database sharding** using:

- Spring Boot
- JDBC (no JPA)
- PostgreSQL
- Consistent hashing with virtual nodes
- Liquibase for schema management

## Features
- Manual shard routing
- UUID-based shard keys
- Cross-shard queries
- Liquibase-managed schema
- No Hibernate / JPA

## Architecture
- Each shard is a separate PostgreSQL database
- Application routes requests using consistent hashing
- Writes go to one shard
- Reads can fan-out across shards

## Running locally

1. Create two databases:
    - `orders_shard_0`
    - `orders_shard_1`

2. Apply schema:
   ```bash
   mvn liquibase:update -Dliquibase.url=jdbc:postgresql://localhost:5432/orders_shard_0
   mvn liquibase:update -Dliquibase.url=jdbc:postgresql://localhost:5432/orders_shard_1

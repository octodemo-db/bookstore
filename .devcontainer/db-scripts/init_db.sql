#!/usr/bin/env bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --password "$POSTGRES_PASSWORD" <<-EOSQL
  CREATE USER bookstore;
  CREATE DATABASE bookstore;
  GRANT ALL PRIVILEGES ON DATABASE bookstore TO bookstore;
EOSQL

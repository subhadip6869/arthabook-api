CREATE SCHEMA IF NOT EXISTS finance;

CREATE TYPE finance.account_type AS ENUM (
    'BANK',
    'DEMAT',
    'BROKERAGE',
    'CASH',
    'POST_OFFICE',
    'WALLET',
    'OTHER');

CREATE TYPE finance.account_status AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'CLOSED');
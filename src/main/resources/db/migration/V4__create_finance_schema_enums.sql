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

CREATE TYPE finance.holding_type AS ENUM (
    'STOCK',
    'MUTUAL_FUND',
    'ETF',
    'BOND',
    'GOVERNMENT_SECURITY',
    'NSC',
    'KVP',
    'FIXED_DEPOSIT',
    'RECURRING_DEPOSIT',
    'PPF',
    'NPS',
    'GOLD',
    'REAL_ESTATE',
    'CASH',
    'OTHER'
    );

CREATE TYPE finance.holding_status AS ENUM (
    'ACTIVE',
    'CLOSED'
    );

CREATE TYPE finance.transaction_type AS ENUM (
    'BUY',
    'SELL',
    'DEPOSIT',
    'WITHDRAWAL',
    'DIVIDEND',
    'INTEREST',
    'FEE',
    'TAX',
    'TRANSFER_IN',
    'TRANSFER_OUT',
    'MATURITY',
    'OTHER'
    );
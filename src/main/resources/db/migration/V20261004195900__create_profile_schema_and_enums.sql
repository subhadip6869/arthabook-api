CREATE SCHEMA IF NOT EXISTS profile;

CREATE TYPE profile.user_gender AS ENUM (
    'MALE',
    'FEMALE',
    'OTHERS'
);

CREATE TYPE profile.user_status AS ENUM (
    'ACTIVE',
    'SUSPENDED',
    'DELETED'
);

CREATE TYPE profile.occupation AS ENUM (
    'SALARIED',
    'SELF_EMPLOYED',
    'BUSINESS',
    'RETIRED',
    'STUDENT',
    'OTHER'
);

CREATE TYPE profile.annual_income_range AS ENUM (
    'BELOW_3L',
    'FROM_3L_TO_6L',
    'FROM_6L_TO_12L',
    'FROM_12L_TO_25L',
    'ABOVE_25L',
    'PREFER_NOT_TO_SAY'
);

CREATE TYPE profile.risk_appetite AS ENUM (
    'CONSERVATIVE',
    'MODERATE',
    'AGGRESSIVE'
);
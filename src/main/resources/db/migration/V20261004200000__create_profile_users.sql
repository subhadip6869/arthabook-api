CREATE TABLE profile.users
(
    user_id              VARCHAR(128)        NOT NULL,
    email                VARCHAR(320)        NOT NULL,
    isd_code             VARCHAR(5),
    mobile_number        VARCHAR(20),
    full_name            VARCHAR(150)        NOT NULL,
    profile_photo_url    TEXT,
    date_of_birth        DATE,
    gender               profile.user_gender,
    status               profile.user_status NOT NULL DEFAULT 'ACTIVE',
    onboarding_completed BOOLEAN             NOT NULL DEFAULT FALSE,
    created_at           TIMESTAMPTZ         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMPTZ         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version              BIGINT              NOT NULL DEFAULT 0,

    CONSTRAINT pk_users
        PRIMARY KEY (user_id),

    CONSTRAINT uq_email
        UNIQUE (email)
);

DROP TABLE IF EXISTS `WK_USER`;
CREATE TABLE WK_USER (
    id                          BIGINT AUTO_INCREMENT       PRIMARY KEY,
    email                       VARCHAR(255)                NOT NULL,
    nickname                    VARCHAR(100)                NOT NULL,
    phone_number                VARCHAR(20)                 NOT NULL,
    status                      VARCHAR(20)                 NOT NULL,
    password                    VARCHAR(255)                NULL,
    social_type                 VARCHAR(20)                 NULL,
    social_id                   VARCHAR(255)                NULL,
    profile_image_url           VARCHAR(500)                NULL,
    last_login_at               TIMESTAMP                   NULL,
    created_at                  TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX ux_wk_user_email ON WK_USER (email);
CREATE UNIQUE INDEX ux_wk_user_phone_number ON WK_USER (phone_number);
CREATE UNIQUE INDEX ux_wk_user_nickname ON WK_USER (nickname);

DROP TABLE IF EXISTS `WK_TERMS`;
CREATE TABLE WK_TERMS (
    id                          BIGINT AUTO_INCREMENT       PRIMARY KEY,
    type                        VARCHAR(50)                 NOT NULL,
    content                     TEXT                        NOT NULL,
    required                    BOOLEAN                     NOT NULL,
    is_active                   BOOLEAN                     NOT NULL,
    terms_version               BIGINT                      NOT NULL DEFAULT 0,
    created_at                  TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT ux_terms_type_version UNIQUE (type, terms_version)
);

DROP TABLE IF EXISTS `WK_TERMS_HISTORY`;
CREATE TABLE WK_TERMS_HISTORY (
    id                          BIGINT AUTO_INCREMENT       PRIMARY KEY,
    terms_id                    BIGINT                      NOT NULL,
    type                        VARCHAR(50)                 NOT NULL,
    content                     TEXT                        NOT NULL,
    required                    BOOLEAN                     NOT NULL,
    terms_version               BIGINT                      NOT NULL,
    created_at                  TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_terms_history_terms FOREIGN KEY (terms_id) REFERENCES WK_TERMS (id)
);

DROP TABLE IF EXISTS `WK_USER_TERMS`;
CREATE TABLE WK_USER_TERMS (
    id                              BIGINT AUTO_INCREMENT       PRIMARY KEY,
    terms_id                        BIGINT                      NOT NULL,
    user_id                         BIGINT                      NOT NULL,
    agreed                          BOOLEAN                     NOT NULL,
    agreed_terms_version            BIGINT                      NOT NULL,
    agreed_at                       TIMESTAMP                   NULL,
    created_at                  TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_terms_terms FOREIGN KEY (terms_id) REFERENCES WK_TERMS (id),
    CONSTRAINT fk_user_terms_user FOREIGN KEY (user_id) REFERENCES WK_USER (id),
    CONSTRAINT ux_user_terms_unique UNIQUE (user_id, terms_id)
);

CREATE TABLE WK_USER
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         VARCHAR(255) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    email           VARCHAR(50)  NOT NULL,
    nickname        VARCHAR(12)  NOT NULL,
    status          VARCHAR(20)  NOT NULL,
    role            VARCHAR(20)  NOT NULL,
    level           VARCHAR(20)  NOT NULL,
    point           INT          NOT NULL DEFAULT 0,

    created_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_time TIMESTAMP    NULL,

    CONSTRAINT UK_USER_USER_ID  UNIQUE (user_id),
    CONSTRAINT UK_USER_EMAIL    UNIQUE (email),
    CONSTRAINT UK_USER_NICKNAME UNIQUE (nickname)
);

CREATE TABLE WK_LOGIN_HISTORY
(
    id           BIGINT AUTO_INCREMENT  PRIMARY KEY,
    user_id      BIGINT                 NOT NULL,
    ip_address   VARCHAR(45)            NOT NULL,
    user_agent   VARCHAR(255)           NOT NULL,
    result       VARCHAR(20)            NOT NULL,
    message      VARCHAR(255)           NULL,
    login_time   TIMESTAMP              NOT NULL
);

CREATE TABLE WK_CATEGORY
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(50)        NOT NULL,
    parent_id    BIGINT             NULL,
    status       VARCHAR(20)        NOT NULL,
    type         VARCHAR(20)        NOT NULL,
    order_no     INT                NOT NULL,
    created_time TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
);

CREATE TABLE WK_POST (
    id              BIGINT AUTO_INCREMENT   PRIMARY KEY,
    user_id         BIGINT                  NOT NULL,
    category_id     BIGINT                  NOT NULL,
    title           VARCHAR(200)            NOT NULL,
    content         TEXT                    NOT NULL,
    thumbnail_url   VARCHAR(500)            NULL,
    view_count      BIGINT                  NOT NULL DEFAULT 0,
    status          VARCHAR(30)             NOT NULL,
    created_time    TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

CONSTRAINT FK_WK_POST_USER FOREIGN KEY (user_id) REFERENCES wk_user (id),
CONSTRAINT FK_WK_POST_CATEGORY FOREIGN KEY (category_id) REFERENCES wk_category (id)
);

CREATE TABLE WK_POST_IMAGE (
    id              BIGINT AUTO_INCREMENT   PRIMARY KEY,
    post_id         BIGINT                  NOT NULL,
    image_url       VARCHAR(500)            NOT NULL,
    order_no        INT                     NOT NULL,
    created_time    TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_time    TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
CONSTRAINT fk_post_image_post FOREIGN KEY (post_id)
REFERENCES wk_post(id) ON DELETE CASCADE
);

SET FOREIGN_KEY_CHECKS = 0;
SET FOREIGN_KEY_CHECKS = 1;

DROP TABLE IF EXISTS `WK_USER`;
CREATE TABLE WK_USER (
    id                          BIGINT AUTO_INCREMENT       PRIMARY KEY,
    email                       VARCHAR(255)                NOT NULL,
    nickname                    VARCHAR(100)                NOT NULL,
    phone_number                VARCHAR(20)                 NOT NULL,
    status                      VARCHAR(20)                 NOT NULL,
    role                        VARCHAR(20)                 NOT NULL,
    password                    VARCHAR(255)                NULL,
    provider_type               VARCHAR(20)                 NULL,
    provider_id                 VARCHAR(255)                NULL,
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
INSERT INTO WK_TERMS (type, content, required, is_active)
VALUES
    ('SERVICE',  '서비스 이용약관 내용입니다.',  TRUE,  TRUE),
    ('PRIVACY',  '개인정보 처리방침 내용입니다.', TRUE,  TRUE),
    ('MARKETING','마케팅 정보 수신 동의 내용입니다.', FALSE, TRUE),
    ('KAKAO_AD', '카카오 광고성 정보 수신 동의 내용입니다.', FALSE, TRUE);


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
    created_at                      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_terms_terms FOREIGN KEY (terms_id) REFERENCES WK_TERMS (id),
    CONSTRAINT fk_user_terms_user FOREIGN KEY (user_id) REFERENCES WK_USER (id),
    CONSTRAINT ux_user_terms_unique UNIQUE (user_id, terms_id)
);

DROP TABLE IF EXISTS WK_REFRESH_TOKEN;
CREATE TABLE WK_REFRESH_TOKEN (
    id                              BIGINT AUTO_INCREMENT       PRIMARY KEY,
    user_id                         BIGINT                      NOT NULL,
    token                           VARCHAR(500)                NOT NULL,
    expiration                      TIMESTAMP                   NOT NULL,
    created_at                      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_token_user FOREIGN KEY (user_id) REFERENCES WK_USER (id)
);

DROP TABLE IF EXISTS WK_BOARD;
CREATE TABLE WK_BOARD (
    id                              BIGINT AUTO_INCREMENT       PRIMARY KEY,
    name                            VARCHAR(100)                NOT NULL,
    slug                            VARCHAR(50)                 NOT NULL,
    description                     VARCHAR(500)                NULL,
    display_order                   INT                         NOT NULL DEFAULT 0,
    is_active                       BOOLEAN                     NOT NULL DEFAULT TRUE,
    created_at                      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX ux_wk_board_slug ON WK_BOARD (slug);

INSERT INTO WK_BOARD (name, slug, description, display_order) VALUES
('공지사항', 'notice', '공지사항입니다', 0),
('자유게시판', 'free', '자유롭게 글을 작성하세요', 1),
('QnA', 'qna', '질문과 답변', 2);

DROP TABLE IF EXISTS WK_POST;
CREATE TABLE WK_POST (
    id                              BIGINT AUTO_INCREMENT       PRIMARY KEY,
    title                           VARCHAR(200)                NOT NULL,
    content                         TEXT                        NOT NULL,
    author_id                       BIGINT                      NOT NULL,
    board_id                        BIGINT                      NOT NULL,
    view_count                      BIGINT                      NOT NULL DEFAULT 0,
    created_at                      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_post_author FOREIGN KEY (author_id) REFERENCES WK_USER (id),
    CONSTRAINT fk_post_board  FOREIGN KEY (board_id)  REFERENCES WK_BOARD (id)
);

DROP TABLE IF EXISTS WK_COMMENT;
CREATE TABLE WK_COMMENT (
    id                              BIGINT AUTO_INCREMENT       PRIMARY KEY,
    content                         TEXT                        NOT NULL,
    author_id                       BIGINT                      NOT NULL,
    post_id                         BIGINT                      NOT NULL,
    created_at                      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                      TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES WK_USER (id),
    CONSTRAINT fk_comment_post   FOREIGN KEY (post_id)   REFERENCES WK_POST (id)
);



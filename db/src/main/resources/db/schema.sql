-- 관리자 계정(사용자용)
CREATE TABLE admin_user
(
    id           BIGSERIAL PRIMARY KEY,

    -- 관리자 이름
    name         TEXT,

    -- 관리자 이메일
    email        TEXT UNIQUE,

    -- 비밀번호
    password     TEXT,

    -- 역할
    role         TEXT    DEFAULT 'ADMIN',

    -- 슈퍼 관리자 승인
    is_confirmed BOOLEAN DEFAULT FALSE,

    created_at   TIMESTAMP,
    is_archived  BOOLEAN DEFAULT FALSE
);

-- 프로젝트
CREATE TABLE project
(
    id                 BIGSERIAL PRIMARY KEY,

    -- 프로젝트명
    name               TEXT,

    -- 설명
    description        TEXT,

    -- git 레포지토리
    git_repository_url TEXT,

    created_at         TIMESTAMP,
    is_archived        BOOLEAN DEFAULT FALSE
);

-- 관리자 계정(사용자용)
CREATE TABLE admin_user
(
    id          BIGSERIAL PRIMARY KEY,

    -- 관리자 이름
    name        TEXT,

    -- 관리자 이메일
    email       TEXT UNIQUE,

    -- 비밀번호
    password    TEXT,

    created_at  TIMESTAMP,
    is_archived BOOLEAN DEFAULT FALSE
);

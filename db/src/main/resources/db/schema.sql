-- 관리자 계정(사용자용)
CREATE TABLE admin_user
(
    id             BIGSERIAL      PRIMARY KEY,
    name           TEXT           NOT NULL,
    email          TEXT           UNIQUE,
    password       TEXT           NOT NULL,
    -- 역할 (ADMIN, SUPER_ADMIN)
    role           VARCHAR(10)    DEFAULT 'ADMIN',
    -- 슈퍼 관리자 승인
    is_confirmed   BOOLEAN        DEFAULT FALSE,

    created_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    is_archived    BOOLEAN        DEFAULT FALSE
);

-- AI 프로젝트 (Prompt Studio)
CREATE TABLE ai_project
(
    id             BIGSERIAL      PRIMARY KEY,
    name           TEXT           NOT NULL,
    description    TEXT           NOT NULL,

    -- 프로젝트 상태 (ACTIVE, INACTIVE)
    status         VARCHAR(10)    DEFAULT 'INACTIVE',
    -- fk admin_user
    created_by     BIGINT         NOT NULL,

    created_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    is_archived    BOOLEAN        DEFAULT FALSE
);

-- API 제공자 설정 (GPT, Gemini 등)
CREATE TABLE api_provider
(
    id             BIGSERIAL      PRIMARY KEY,
    name           VARCHAR(10)    NOT NULL, -- 'GPT', 'GEMINI', etc.
    api_key        TEXT           NOT NULL, -- encrypted
    base_url       TEXT           NOT NULL,
    -- fk admin_user
    created_by     BIGINT         NOT NULL,
    
    created_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    is_archived    BOOLEAN        DEFAULT FALSE
);

-- 프롬프트 (Prompt Studio)
CREATE TABLE prompt
(
    id             BIGSERIAL      PRIMARY KEY,
    -- fk project
    project_id     BIGINT         NOT NULL,
    content        TEXT           NOT NULL,
    version        INT            DEFAULT 1,
    is_active      BOOLEAN        DEFAULT TRUE,
    variables      TEXT[]         NOT NULL,
    
    created_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    is_archived    BOOLEAN        DEFAULT FALSE
);
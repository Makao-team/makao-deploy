-- project
CREATE TABLE project
(
    id                 BIGSERIAL PRIMARY KEY,
    name               TEXT,
    description        TEXT,
    git_repository_url TEXT,
    created_at         TIMESTAMP,
    is_archived        BOOLEAN DEFAULT FALSE
);

-- admin user
CREATE TABLE admin_user
(
    id          BIGSERIAL PRIMARY KEY,
    email       TEXT UNIQUE,
    password    TEXT,
    created_at  TIMESTAMP,
    is_archived BOOLEAN DEFAULT FALSE
);

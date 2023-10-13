--liquibase formatted sql
--changeset DOROFEEV-MS:create_users_table_9eff576b-13aa-412f-bb3f-4c24a560e0a4.sql:TASK-1
--type DDL

CREATE TABLE users
(
    id                UUID PRIMARY KEY NOT NULL,
    role_id           UUID             NOT NULL REFERENCES role (id),
    login             VARCHAR(256)     NOT NULL UNIQUE NOT NULL,
    password          VARCHAR(256)     NOT NULL,
    phone_number      VARCHAR(16) UNIQUE,
    duration_password SMALLINT         NOT NULL DEFAULT 7,
    nickname          VARCHAR(256) UNIQUE,
    surname           VARCHAR(256)     NOT NULL,
    name              VARCHAR(256)     NOT NULL,
    patronymic        VARCHAR(256)     NOT NULL,
    birth_date        DATE,
    gender            VARCHAR(8)       NOT NULL,
    status            VARCHAR(16)      NOT NULL DEFAULT 'CREATED',
    confirm_type      VARCHAR(8)       NOT NULL DEFAULT 'EMAIL',
    is_confirm        BOOLEAN          NOT NULL DEFAULT FALSE,
    last_seen         TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    create_date       TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    update_date       TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    version           BIGINT           NOT NULL DEFAULT 0
);

CREATE INDEX ix_users_role_id ON users (role_id);
CREATE INDEX ix_users_status ON users (status);

ALTER TABLE users
    ADD CONSTRAINT check_users_gender CHECK ( gender IN ('MALE', 'FEMALE'));

ALTER TABLE users
    ADD CONSTRAINT check_users_status CHECK ( status IN
                                              ('CREATED', 'DELETED', 'ACTIVE', 'NOT_ACTIVE', 'BANNED', 'GUEST'));

ALTER TABLE users
    ADD CONSTRAINT check_users_confirm_type CHECK ( confirm_type IN
                                                    ('MOBILE', 'EMAIL'));

-- ROLLBACK DROP TABLE users;

COMMENT ON TABLE users IS 'Таблица с пользователями';

COMMENT ON COLUMN users.id IS 'Идентификатор пользователя';
COMMENT ON COLUMN users.login IS 'Логин (Email) пользователя';
COMMENT ON COLUMN users.password IS 'Логин (Email) пользователя';
COMMENT ON COLUMN users.phone_number IS 'Номер телефона пользователя';
COMMENT ON COLUMN users.duration_password IS 'Время действия пароля в днях';
COMMENT ON COLUMN users.nickname IS 'Никнейм пользователя';
COMMENT ON COLUMN users.surname IS 'Фамилия пользователя';
COMMENT ON COLUMN users.name IS 'Имя пользователя';
COMMENT ON COLUMN users.patronymic IS 'Отчество пользователя';
COMMENT ON COLUMN users.birth_date IS 'Дата рождения пользователя';
COMMENT ON COLUMN users.gender IS 'Пол пользователя';
COMMENT ON COLUMN users.status IS 'Статус пользователя';
COMMENT ON COLUMN users.confirm_type IS 'Способ подтверждения аккаунта';
COMMENT ON COLUMN users.is_confirm IS 'Подтверждение аккаунта';
COMMENT ON COLUMN users.last_seen IS 'Дата последней авторизации';
COMMENT ON COLUMN users.create_date IS 'Дата создания записи';
COMMENT ON COLUMN users.update_date IS 'Дата последнего обновления записи';
COMMENT ON COLUMN users.version IS 'Номер версии объекта';
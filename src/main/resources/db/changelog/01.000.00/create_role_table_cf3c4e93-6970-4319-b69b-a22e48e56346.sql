--liquibase formatted sql
--changeset Maxim Dorofeev:create_role_table_cf3c4e93-6970-4319-b69b-a22e48e56346.sql
--type DDL
--comment AM-1

CREATE TABLE role
(
    id          UUID PRIMARY KEY    NOT NULL,
    name        VARCHAR(124) UNIQUE NOT NULL,
    create_date TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    update_date TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    version     BIGINT              NOT NULL DEFAULT 0
);

-- ROLLBACK DROP TABLE role;

COMMENT ON TABLE role IS 'Таблица с ролями пользователей';

COMMENT ON COLUMN role.id IS 'Идентификатор записи';
COMMENT ON COLUMN role.name IS 'Уникальное наименование роли пользователя';
COMMENT ON COLUMN role.create_date IS 'Дата создания записи';
COMMENT ON COLUMN role.update_date IS 'Дата последнего обновления записи';
COMMENT ON COLUMN role.version IS 'Номер версии объекта';
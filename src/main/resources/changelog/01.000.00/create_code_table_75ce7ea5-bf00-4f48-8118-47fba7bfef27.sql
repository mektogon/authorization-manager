--liquibase formatted sql
--changeset DOROFEEV-MS:create_code_table_75ce7ea5-bf00-4f48-8118-47fba7bfef27.sql:TASK-1
--type DDL

CREATE TABLE code
(
    id          UUID PRIMARY KEY NOT NULL,
    user_id     UUID UNIQUE      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    name        VARCHAR(16)      NOT NULL,
    create_date TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    update_date TIMESTAMPTZ      NOT NULL DEFAULT NOW(),
    version     BIGINT           NOT NULL DEFAULT 0
);

CREATE INDEX I_CODE_USER_ID ON code (user_id);

-- ROLLBACK DROP TABLE code;

COMMENT ON TABLE code IS 'Таблица хранения кодов входа для пользователя';

COMMENT ON COLUMN code.id IS 'Идентификатор записи';
COMMENT ON COLUMN code.user_id IS 'Идентификатор пользователя, которому принадлежит запись';
COMMENT ON COLUMN code.create_date IS 'Дата создания записи';
COMMENT ON COLUMN code.version IS 'Номер версии объекта';
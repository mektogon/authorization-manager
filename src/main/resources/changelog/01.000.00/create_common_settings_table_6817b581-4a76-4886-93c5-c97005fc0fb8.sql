--liquibase formatted sql
--changeset DOROFEEV-MS:create_common_settings_table_6817b581-4a76-4886-93c5-c97005fc0fb8.sql:TASK-1
--type DDL

CREATE TABLE common_settings
(
    id          UUID PRIMARY KEY    NOT NULL,
    name        VARCHAR(128) UNIQUE NOT NULL,
    value       VARCHAR(256)        NOT NULL,
    description VARCHAR(512),
    create_date TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    update_date TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    version     BIGINT              NOT NULL DEFAULT 0
);

-- ROLLBACK DROP TABLE common_settings;

COMMENT ON TABLE common_settings IS 'Таблица с универсальными рубильниками';

COMMENT ON COLUMN common_settings.id IS 'Идентификатор записи';
COMMENT ON COLUMN common_settings.name IS 'Уникальное наименование рубильника';
COMMENT ON COLUMN common_settings.value IS 'Значение рубильника';
COMMENT ON COLUMN common_settings.description IS 'Описание рубильника';
COMMENT ON COLUMN common_settings.create_date IS 'Дата создания записи';
COMMENT ON COLUMN common_settings.update_date IS 'Дата последнего обновления записи';
COMMENT ON COLUMN common_settings.version IS 'Номер версии объекта';
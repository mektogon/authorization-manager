--liquibase formatted sql
--changeset DOROFEEV-MS:create_photo_table_a0034f35-e864-4937-871b-09796abb2e2e.sql:TASK-1
--type DDL


CREATE TABLE photo
(
    id          UUID PRIMARY KEY    NOT NULL,
    user_id     UUID                NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    name        VARCHAR(256) UNIQUE NOT NULL,
    url         VARCHAR(1024)       NOT NULL,
    url_preview VARCHAR(1024)       NOT NULL,
    create_date TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    update_date TIMESTAMPTZ         NOT NULL DEFAULT NOW(),
    version     BIGINT              NOT NULL DEFAULT 0
);

CREATE INDEX I_PHOTO_USER_ID ON photo (user_id);

-- ROLLBACK DROP TABLE photo;

COMMENT ON TABLE photo IS 'Таблица хранения аватарок пользователей';

COMMENT ON COLUMN photo.id IS 'Идентификатор записи';
COMMENT ON COLUMN photo.user_id IS 'Идентификатор пользователя, которому принадлежит запись';
COMMENT ON COLUMN photo.name IS 'Уникальное наименование изображения в формате: UUID_width_height.ext';
COMMENT ON COLUMN photo.url IS 'Относительный путь до изображения';
COMMENT ON COLUMN photo.url_preview IS 'Относительный путь до превью изображения';
COMMENT ON COLUMN photo.create_date IS 'Дата создания записи';
COMMENT ON COLUMN photo.update_date IS 'Дата последнего обновления записи';
COMMENT ON COLUMN photo.version IS 'Номер версии объекта';
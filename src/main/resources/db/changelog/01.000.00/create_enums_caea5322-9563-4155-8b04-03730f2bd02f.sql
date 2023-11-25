--liquibase formatted sql
--changeset Maxim Dorofeev:create_enums_caea5322-9563-4155-8b04-03730f2bd02f.sql
--type DDL
--comment AM-1

CREATE TYPE gender_type AS ENUM ('MALE', 'FEMALE');

-- ROLLBACK DROP TYPE gender_type;

CREATE TYPE user_status_type AS ENUM ('CREATED', 'DELETED', 'ACTIVE', 'NOTACTIVE', 'BANNED', 'TEMPORARY');

-- ROLLBACK DROP TYPE user_status_type;

CREATE TYPE confirm_type AS ENUM ('MOBILE', 'EMAIL');

-- ROLLBACK DROP TYPE confirm_type;
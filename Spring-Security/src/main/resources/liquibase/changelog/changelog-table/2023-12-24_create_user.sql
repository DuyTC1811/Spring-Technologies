--liquibase formatted sql

--changeset duytc:01
--rollback DROP TABLE IF EXISTS users;
CREATE SEQUENCE IF NOT EXISTS auto_user_code;
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE TABLE IF NOT EXISTS users
(
    user_id           VARCHAR(36)  NOT NULL,
    user_code         VARCHAR(10)  NOT NULL UNIQUE DEFAULT CONCAT('NV', LPAD(NEXTVAL('auto_user_code')::TEXT, 6, '0')),
    username          VARCHAR(10)  NULL,
    mobile            VARCHAR(15)  NULL,
    active            BOOLEAN      NOT NULL        DEFAULT TRUE,
    email             VARCHAR(50)  NULL,
    password          VARCHAR(255) NOT NULL,
    registered_date   DATE                         DEFAULT NOW(),
    modification_date DATE         NULL            DEFAULT NULL,
    lastLogin         DATE         NULL            DEFAULT NULL,
    PRIMARY KEY (user_id)
);


--liquibase formatted sql

--changeset DUYTC:01
CREATE TABLE IF NOT EXISTS roles
(
    role_id      VARCHAR(36) PRIMARY KEY,
    role_code    VARCHAR(20) NOT NULL UNIQUE,
    description  VARCHAR(20) NOT NULL,
    created_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP   NULL
);

COMMENT ON TABLE roles IS 'BẢNG LƯU TRỮ THÔNG TIN ROLES';
COMMENT ON COLUMN roles.role_id IS 'MÃ USER ID';
COMMENT ON COLUMN roles.role_code IS 'MÃ ROLE';
COMMENT ON COLUMN roles.description IS 'MÔ TẢ ROLE';
COMMENT ON COLUMN roles.created_date IS 'NGÀY TẠO';
COMMENT ON COLUMN roles.updated_date IS 'NGÀY SỬA ĐỔI';


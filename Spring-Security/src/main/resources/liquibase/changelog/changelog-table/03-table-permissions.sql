--liquibase formatted sql

--changeset DUYTC:create-permissions-table
CREATE TABLE IF NOT EXISTS permissions
(
    per_id       VARCHAR(36)  PRIMARY KEY,
    per_code     VARCHAR(100) NOT NULL UNIQUE,
    per_name     VARCHAR(255) NOT NULL,
    description  VARCHAR(500) NULL,
    enabled      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP    NULL
);

COMMENT ON TABLE permissions IS 'LƯU TRỮ THÔNG TIN QUYỀN';
COMMENT ON COLUMN permissions.per_id IS 'ID QUYỀN';
COMMENT ON COLUMN permissions.per_code IS 'MÃ QUYỀN';
COMMENT ON COLUMN permissions.per_name IS 'TÊN QUYỀN';
COMMENT ON COLUMN permissions.description IS 'MÔ TẢ QUYỀN';
COMMENT ON COLUMN permissions.enabled IS 'TRẠNG THÁI QUYỀN';
COMMENT ON COLUMN permissions.created_date IS 'NGÀY TẠO';
COMMENT ON COLUMN permissions.updated_date IS 'NGÀY SỬA ĐỔI';
--liquibase formatted sql

--changeset DUYTC:01
CREATE TABLE permissions
(
    per_id       VARCHAR(36) PRIMARY KEY,
    per_name     VARCHAR(50) NOT NULL UNIQUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP   NULL
);

COMMENT ON TABLE permissions IS 'LƯU TRỮ THÔNG TIN QUYỀN';
COMMENT ON COLUMN permissions.per_id IS 'ID QUYỀN';
COMMENT ON COLUMN permissions.per_name IS 'TÊN QUYỀN';
COMMENT ON COLUMN permissions.created_date IS 'NGÀY TẠO';
COMMENT ON COLUMN permissions.updated_date IS 'NGÀY SỬA ĐỔI';
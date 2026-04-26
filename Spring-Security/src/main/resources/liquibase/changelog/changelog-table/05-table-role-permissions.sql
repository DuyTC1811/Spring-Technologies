--liquibase formatted sql

--changeset DUYTC:create-role-permissions-table
CREATE TABLE IF NOT EXISTS role_permissions
(
    role_id      VARCHAR(36) NOT NULL,
    per_id       VARCHAR(36) NOT NULL,
    enabled      BOOLEAN     NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP   NULL,

    PRIMARY KEY (role_id, per_id),

    CONSTRAINT fk_role_permissions_role
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,

    CONSTRAINT fk_role_permissions_permission
    FOREIGN KEY (per_id) REFERENCES permissions(per_id) ON DELETE CASCADE
);

COMMENT ON TABLE role_permissions IS 'BẢNG LƯU TRỮ THÔNG TIN ROLE - QUYỀN';
COMMENT ON COLUMN role_permissions.role_id IS 'ID ROLE';
COMMENT ON COLUMN role_permissions.per_id IS 'ID QUYỀN';
COMMENT ON COLUMN role_permissions.enabled IS 'TRẠNG THÁI QUYỀN: TRUE, FALSE';
COMMENT ON COLUMN role_permissions.created_date IS 'NGÀY TẠO';
COMMENT ON COLUMN role_permissions.updated_date IS 'NGÀY SỬA ĐỔI';
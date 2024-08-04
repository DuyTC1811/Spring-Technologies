CREATE TABLE role_permissions
(
    role_id    VARCHAR(36) NOT NULL,
    per_id     VARCHAR(36) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP   NULL,
    PRIMARY KEY (role_id, per_id),
    FOREIGN KEY (role_id) REFERENCES roles (role_id) ON DELETE CASCADE,
    FOREIGN KEY (per_id) REFERENCES permissions (per_id) ON DELETE CASCADE
);

COMMENT ON TABLE role_permissions IS 'BẢNG LƯU TRỮ THÔNG TIN ID ROLE QUYỀN ';
COMMENT ON COLUMN role_permissions.role_id IS 'ID ROLE';
COMMENT ON COLUMN role_permissions.per_id IS 'ID QUYỀN';
COMMENT ON COLUMN role_permissions.created_date IS 'NGÀY TẠO';
COMMENT ON COLUMN role_permissions.updated_date IS 'NGÀY SỬA ĐỔI';
CREATE TABLE IF NOT EXISTS user_role
(
    user_id      VARCHAR(36) NOT NULL,
    role_id      VARCHAR(36) NOT NULL,
    created_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP   NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (role_id) REFERENCES roles (role_id)
    );

COMMENT ON TABLE user_role IS 'BẢNG LƯU TRỮ THÔNG TIN USER ROLES';
COMMENT ON COLUMN user_role.user_id IS 'ID BẢN USERS';
COMMENT ON COLUMN user_role.role_id IS 'ID BẢNG ROLES';
COMMENT ON COLUMN user_role.created_date IS 'NGÀY TẠO';
COMMENT ON COLUMN user_role.updated_date IS 'NGÀY SỬA ĐỔI';
--liquibase formatted sql

--changeset DUYTC:create-user-roles-table
CREATE TABLE IF NOT EXISTS user_roles
(
    user_id      VARCHAR(36) NOT NULL,
    role_id      VARCHAR(36) NOT NULL,
    created_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP   NULL,

    PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_roles_user
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
);

COMMENT ON TABLE user_roles IS 'BẢNG LƯU TRỮ THÔNG TIN USER ROLES';
COMMENT ON COLUMN user_roles.user_id IS 'ID BẢNG USERS';
COMMENT ON COLUMN user_roles.role_id IS 'ID BẢNG ROLES';
COMMENT ON COLUMN user_roles.created_date IS 'NGÀY TẠO';
COMMENT ON COLUMN user_roles.updated_date IS 'NGÀY SỬA ĐỔI';
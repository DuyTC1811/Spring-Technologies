--liquibase formatted sql

--changeset duytc:01
--rollback DROP TABLE IF EXISTS users;
CREATE SEQUENCE IF NOT EXISTS auto_user_code;
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE TABLE IF NOT EXISTS users
(
    user_id           VARCHAR(36)  NOT NULL,
    user_code         VARCHAR(10)  NOT NULL UNIQUE DEFAULT ('NV' || LPAD(NEXTVAL('auto_user_code')::TEXT, 6, '0')),
    username          VARCHAR(10)  NULL UNIQUE,
    mobile            VARCHAR(15)  NULL UNIQUE,
    active            BOOLEAN      NOT NULL        DEFAULT TRUE,
    email             VARCHAR(50)  NULL UNIQUE,
    password          VARCHAR(255) NOT NULL,
    registered_date   DATE                         DEFAULT NOW(),
    modification_date DATE         NULL,
    PRIMARY KEY (user_id)
);
CREATE UNIQUE INDEX index_mobile ON users (mobile);
CREATE UNIQUE INDEX index_mail ON users (email);

COMMENT ON COLUMN users.user_id IS 'MÃ USER';
COMMENT ON COLUMN users.user_code IS 'MÃ SỐ USER';
COMMENT ON COLUMN users.username IS 'TÀI KHOẢN USER NAME';
COMMENT ON COLUMN users.mobile IS 'SỐ ĐIỆN THOẠI';
COMMENT ON COLUMN users.active IS 'TRẠN THÁI TÀI KHOẢN';
COMMENT ON COLUMN users.email IS 'ĐỊA CHỈ EMAIL';
COMMENT ON COLUMN users.password IS 'MẬT KHẨU';
COMMENT ON COLUMN users.registered_date IS 'NGÀY ĐĂNG KÝ';
COMMENT ON COLUMN users.modification_date IS 'NGÀY SỬA ĐỔI';

--rollback DROP TABLE IF EXISTS role;
CREATE TABLE role
(
    role_id     VARCHAR(36)  NOT NULL,
    title       VARCHAR(30)  NOT NULL ,
    slug        VARCHAR(15)  NOT NULL,
    active      VARCHAR(10)  NOT NULL DEFAULT 'ACTIVE',
    description VARCHAR(200) NULL ,
    created_at  DATE         NOT NULL DEFAULT NOW(),
    updated_at  DATE         NULL,
    PRIMARY KEY (role_id)
);
CREATE UNIQUE INDEX index_slug ON role (slug);

COMMENT ON COLUMN role.role_id IS 'MÃ ID ROLE';
COMMENT ON COLUMN role.title IS 'TÊU ĐỀ ROLE';
COMMENT ON COLUMN role.slug IS 'ROLE';
COMMENT ON COLUMN role.active IS 'TRẠNG THÁI ROLE';
COMMENT ON COLUMN role.description IS 'MÔ TẢ ROLE';
COMMENT ON COLUMN role.created_at IS 'NGÀY TẠO ROLE';
COMMENT ON COLUMN role.updated_at IS 'NGÀY CẬP NHẬT';

--rollback DROP TABLE IF EXISTS user_role;
CREATE TABLE user_role
(
    user_id VARCHAR(36) NOT NULL,
    role_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fku_role FOREIGN KEY (role_id)
        REFERENCES role (role_id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fkr_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

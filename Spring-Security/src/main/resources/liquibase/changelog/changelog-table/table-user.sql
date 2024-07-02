--liquibase formatted sql

--changeset DUYTC:01
CREATE TABLE IF NOT EXISTS users
(
    user_id           VARCHAR(36)  NOT NULL,
    username          VARCHAR(10)  NULL UNIQUE,
    mobile            VARCHAR(15)  NULL UNIQUE,
    active            VARCHAR(20)  NOT NULL        DEFAULT 'AWAITING',
    email             VARCHAR(50)  NULL UNIQUE,
    password          VARCHAR(255) NOT NULL,
    created_date      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date      TIMESTAMP    NULL,
    PRIMARY KEY (user_id)
);
CREATE UNIQUE INDEX index_username ON users (username);
CREATE UNIQUE INDEX index_mobile ON users (mobile);
CREATE UNIQUE INDEX index_mail ON users (email);

COMMENT ON COLUMN users.user_id IS 'MÃ USER ID';
COMMENT ON COLUMN users.username IS 'TÀI KHOẢN USER NAME';
COMMENT ON COLUMN users.mobile IS 'SỐ ĐIỆN THOẠI';
COMMENT ON COLUMN users.active IS 'TRẠN THÁI TÀI KHOẢN: ACTIVE, INACTIVE, AWAITING';
COMMENT ON COLUMN users.email IS 'ĐỊA CHỈ EMAIL';
COMMENT ON COLUMN users.password IS 'MẬT KHẨU';
COMMENT ON COLUMN users.created_date IS 'NGÀY ĐĂNG KÝ';
COMMENT ON COLUMN users.updated_date IS 'NGÀY SỬA ĐỔI';


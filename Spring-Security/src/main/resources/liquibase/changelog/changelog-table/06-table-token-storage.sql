--liquibase formatted sql

--changeset DUYTC:01
CREATE TABLE IF NOT EXISTS token_storage
(
    token_id          VARCHAR(36)   NOT NULL,
    asset_token       VARCHAR(300)  NOT NULL UNIQUE,
    refresh_token     VARCHAR(300)  NOT NULL UNIQUE,
    status            VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    username          VARCHAR(10)   NOT NULL,
    count_refresh     INT           NOT NULL DEFAULT 0,
    created_date      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    logout_date       TIMESTAMP     NULL,
    updated_date      TIMESTAMP     NULL,
    PRIMARY KEY (token_id)
);

COMMENT ON TABLE token_storage IS 'BẢNG LƯU TRỮ THÔNG TIN TOKEN DÙNG ĐỂ VALIDATE TOKEN';
COMMENT ON COLUMN token_storage.token_id IS 'MÃ TOKEN ID';
COMMENT ON COLUMN token_storage.asset_token IS 'ASSET TOKEN';
COMMENT ON COLUMN token_storage.refresh_token IS 'REFRESH TOKEN';
COMMENT ON COLUMN token_storage.username IS 'TÀI KHOẢN USER NAME';
COMMENT ON COLUMN token_storage.count_refresh IS 'ĐẾM SỐ LẦN REFRESH';
COMMENT ON COLUMN token_storage.status IS 'TRẠN THÁI TÀI TOKEN: ACTIVE, INACTIVE';
COMMENT ON COLUMN token_storage.created_date IS 'THỜI GIAN TẠO';
COMMENT ON COLUMN token_storage.logout_date IS 'THỜI GIAN ĐĂNG XUẤT';
--liquibase formatted sql

--changeset DUYTC:01
CREATE TABLE IF NOT EXISTS token_storage
(
    token_id          VARCHAR(36)   NOT NULL,
    asset_token       VARCHAR(300)  NOT NULL UNIQUE,
    refresh_token     VARCHAR(300)  NOT NULL UNIQUE,
    active            VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    username          VARCHAR(10)   NOT NULL,
    created_date      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    logout_date       TIMESTAMP     NULL,
    PRIMARY KEY (token_id)
);

COMMENT ON COLUMN token_storage.token_id IS 'MÃ TOKEN ID';
COMMENT ON COLUMN token_storage.asset_token IS 'ASSET TOKEN';
COMMENT ON COLUMN token_storage.refresh_token IS 'REFRESH TOKEN';
COMMENT ON COLUMN token_storage.username IS 'TÀI KHOẢN USER NAME';
COMMENT ON COLUMN token_storage.active IS 'TRẠN THÁI TÀI TOKEN: ACTIVE, INACTIVE, AWAITING';
COMMENT ON COLUMN token_storage.created_date IS 'THỜI GIAN TẠO';
COMMENT ON COLUMN token_storage.logout_date IS 'THỜI GIAN ĐĂNG XUẤT';
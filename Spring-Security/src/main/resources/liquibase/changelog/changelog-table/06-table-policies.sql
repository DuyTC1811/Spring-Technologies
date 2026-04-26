--liquibase formatted sql

--changeset DUYTC:create-policies-table
CREATE TABLE IF NOT EXISTS policies
(
    policy_id    VARCHAR(36)  PRIMARY KEY,
    policy_code  VARCHAR(100) NOT NULL UNIQUE,
    policy_name  VARCHAR(255) NOT NULL,
    description  VARCHAR(500) NULL,
    effect       VARCHAR(20)  NOT NULL DEFAULT 'ALLOW',
    enabled      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP    NULL,

    CONSTRAINT chk_policies_effect
    CHECK (effect IN ('ALLOW', 'DENY'))
    );

COMMENT ON TABLE policies IS 'BẢNG LƯU TRỮ THÔNG TIN POLICY PHÂN QUYỀN ABAC';
COMMENT ON COLUMN policies.policy_id IS 'ID POLICY';
COMMENT ON COLUMN policies.policy_code IS 'MÃ POLICY';
COMMENT ON COLUMN policies.policy_name IS 'TÊN POLICY';
COMMENT ON COLUMN policies.description IS 'MÔ TẢ POLICY';
COMMENT ON COLUMN policies.effect IS 'HIỆU LỰC POLICY: ALLOW, DENY';
COMMENT ON COLUMN policies.enabled IS 'TRẠNG THÁI POLICY';
COMMENT ON COLUMN policies.created_date IS 'NGÀY TẠO';
COMMENT ON COLUMN policies.updated_date IS 'NGÀY SỬA ĐỔI';
--liquibase formatted sql

--changeset DUYTC:create-policy-rules-table
CREATE TABLE IF NOT EXISTS policy_rules
(
    rule_id            VARCHAR(36)  PRIMARY KEY,
    policy_id          VARCHAR(36)  NOT NULL,

    subject_attribute  VARCHAR(100) NULL,
    operator           VARCHAR(30)  NOT NULL,
    resource_attribute VARCHAR(100) NULL,
    expected_value     VARCHAR(255) NULL,

    description        VARCHAR(500) NULL,
    enabled            BOOLEAN      NOT NULL DEFAULT TRUE,
    created_date       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date       TIMESTAMP    NULL,

    CONSTRAINT fk_policy_rules_policy
    FOREIGN KEY (policy_id) REFERENCES policies(policy_id) ON DELETE CASCADE,

    CONSTRAINT chk_policy_rules_operator
    CHECK (operator IN (
           'EQ',            -- BẰNG
           'NE',            -- KHÁC
           'GT',            -- LỚN HƠN
           'GTE',           -- LỚN HƠN HOẶC BẰNG
           'LT',            -- NHỎ HƠN
           'LTE',           -- NHỎ HƠN HOẶC BẰNG
           'IN',            -- NẰM TRONG DANH SÁCH
           'NOT_IN',        -- KHÔNG NẰM TRONG DANH SÁCH
           'CONTAINS'       -- CÓ CHỨA
    ))
);


COMMENT ON TABLE policy_rules IS 'BẢNG LƯU TRỮ RULE CHO POLICY ABAC';
COMMENT ON COLUMN policy_rules.rule_id IS 'ID RULE';
COMMENT ON COLUMN policy_rules.policy_id IS 'ID POLICY';
COMMENT ON COLUMN policy_rules.subject_attribute IS 'THUỘC TÍNH CỦA USER/SUBJECT, VÍ DỤ: branchCode, username, approvalLimit';
COMMENT ON COLUMN policy_rules.operator IS 'TOÁN TỬ SO SÁNH: EQ, NE, GT, GTE, LT, LTE, IN, NOT_IN, CONTAINS';
COMMENT ON COLUMN policy_rules.resource_attribute IS 'THUỘC TÍNH CỦA RESOURCE, VÍ DỤ: branchCode, status, createdBy, amount';
COMMENT ON COLUMN policy_rules.expected_value IS 'GIÁ TRỊ CỐ ĐỊNH CẦN SO SÁNH, VÍ DỤ: PENDING';
COMMENT ON COLUMN policy_rules.description IS 'MÔ TẢ RULE';
COMMENT ON COLUMN policy_rules.enabled IS 'TRẠNG THÁI RULE';
COMMENT ON COLUMN policy_rules.created_date IS 'NGÀY TẠO';
COMMENT ON COLUMN policy_rules.updated_date IS 'NGÀY SỬA ĐỔI';
--liquibase formatted sql

--changeset DUYTC:create-permission-policies-table
CREATE TABLE IF NOT EXISTS permission_policies
(
    per_id       VARCHAR(36) NOT NULL,
    policy_id    VARCHAR(36) NOT NULL,
    enabled      BOOLEAN     NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP   NULL,

    PRIMARY KEY (per_id, policy_id),

    CONSTRAINT fk_permission_policies_permission
    FOREIGN KEY (per_id) REFERENCES permissions(per_id) ON DELETE CASCADE,

    CONSTRAINT fk_permission_policies_policy
    FOREIGN KEY (policy_id) REFERENCES policies(policy_id) ON DELETE CASCADE
    );

COMMENT ON TABLE permission_policies IS 'BẢNG LIÊN KẾT PERMISSION VỚI POLICY ABAC';
COMMENT ON COLUMN permission_policies.per_id IS 'ID QUYỀN';
COMMENT ON COLUMN permission_policies.policy_id IS 'ID POLICY';
COMMENT ON COLUMN permission_policies.enabled IS 'TRẠNG THÁI LIÊN KẾT PERMISSION - POLICY';
COMMENT ON COLUMN permission_policies.created_date IS 'NGÀY TẠO';
COMMENT ON COLUMN permission_policies.updated_date IS 'NGÀY SỬA ĐỔI';
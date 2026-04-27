--liquibase formatted sql

--changeset DUYTC:seed-permissions
INSERT INTO PERMISSIONS (PER_ID, PER_CODE, PER_NAME, DESCRIPTION, ENABLED, CREATED_DATE, UPDATED_DATE)
VALUES ('d8e26d96-fc1c-435f-b326-c89d868d07f6', 'USER_CREATE', 'Create user', 'Quyền tạo user', TRUE, CURRENT_TIMESTAMP, NULL),
       ('dca432d0-3309-4cb3-861f-3bab383217e6', 'USER_EDIT', 'Edit user', 'Quyền chỉnh sửa user', TRUE, CURRENT_TIMESTAMP, NULL),
       ('b0e9f103-a3ed-4404-9123-80e1edb92527', 'USER_DELETE', 'Delete user', 'Quyền xóa user', TRUE, CURRENT_TIMESTAMP, NULL),
       ('deb371a2-cd2f-44bf-bb79-527956e1028b', 'USER_VIEW', 'View user', 'Quyền xem user', TRUE, CURRENT_TIMESTAMP, NULL);
--liquibase formatted sql

--changeset DUYTC:seed-roles
INSERT INTO ROLES (ROLE_ID, ROLE_CODE, DESCRIPTION, ENABLED, CREATED_DATE, UPDATED_DATE)
VALUES ('71ee19d1-bb27-4f1c-867d-b3053fa1c003', 'ADMIN', 'Quyền quản trị hệ thống', TRUE, CURRENT_TIMESTAMP, NULL),
       ('33d4bfbd-23cd-4854-aee4-09fa2ae9e713', 'USER', 'Quyền người dùng thông thường', TRUE, CURRENT_TIMESTAMP, NULL),
       ('567fdff5-ea76-4fc6-996b-205094adec43', 'TECHNIQUE', 'Quyền kỹ thuật', TRUE, CURRENT_TIMESTAMP, NULL),
       ('41049a97-effe-4f28-bfcd-27547a4c8bc7', 'STAFF', 'Quyền nhân viên', TRUE, CURRENT_TIMESTAMP, NULL);
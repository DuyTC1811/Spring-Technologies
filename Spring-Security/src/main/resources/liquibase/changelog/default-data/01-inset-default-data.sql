--liquibase formatted sql

--changeset DUYTC:01
INSERT INTO permissions (per_id, per_name, created_date, updated_date)
VALUES ('d8e26d96-fc1c-435f-b326-c89d868d07f6', 'CREATED', '2024-08-05 22:21:29.000000', null),
       ('dca432d0-3309-4cb3-861f-3bab383217e6', 'EDIT', '2024-08-05 22:26:33.000000', null),
       ('b0e9f103-a3ed-4404-9123-80e1edb92527', 'DELETE', '2024-08-05 22:26:35.000000', null),
       ('deb371a2-cd2f-44bf-bb79-527956e1028b', 'VIEW', '2024-08-05 22:26:50.000000', null);

--changeset DUYTC:02
INSERT INTO roles (role_id, role_code, description, created_date, updated_date)
VALUES ('71ee19d1-bb27-4f1c-867d-b3053fa1c003', 'ADMIN', 'QUYỀN ADMIN', '2024-08-05 22:36:11.000000', null),
       ('33d4bfbd-23cd-4854-aee4-09fa2ae9e713', 'USER', 'QUYỀN USER', '2024-08-05 22:36:13.000000', null),
       ('567fdff5-ea76-4fc6-996b-205094adec43', 'TECHNIQUE', 'QUYỀN KỸ THUẬT', '2024-08-05 22:36:15.000000', null),
       ('41049a97-effe-4f28-bfcd-27547a4c8bc7', 'STAFF', 'QUYỀN NHÂN VIÊN', '2024-08-05 22:36:16.000000', null);

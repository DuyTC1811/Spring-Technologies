--changeset DUYTC:insert-default-categories
INSERT INTO CATEGORIES (CATEGORY_ID,
                        NAME,
                        DESCRIPTION,
                        DISPLAY_ORDER,
                        ENABLED,
                        CREATED_DATE,
                        UPDATED_DATE)
VALUES ('11111111-1111-1111-1111-111111111111', 'Technology', 'Danh mục công nghệ', 1, TRUE, CURRENT_TIMESTAMP, NULL),
       ('22222222-2222-2222-2222-222222222222', 'Education', 'Danh mục giáo dục', 2, TRUE, CURRENT_TIMESTAMP, NULL),
       ('33333333-3333-3333-3333-333333333333', 'Health', 'Danh mục sức khỏe', 3, TRUE, CURRENT_TIMESTAMP, NULL),
       ('44444444-4444-4444-4444-444444444444', 'Finance', 'Danh mục tài chính', 4, TRUE, CURRENT_TIMESTAMP, NULL),
       ('55555555-5555-5555-5555-555555555555', 'Travel', 'Danh mục du lịch', 5, TRUE, CURRENT_TIMESTAMP, NULL),
       ('66666666-6666-6666-6666-666666666666', 'Food', 'Danh mục ẩm thực', 6, TRUE, CURRENT_TIMESTAMP, NULL),
       ('77777777-7777-7777-7777-777777777777', 'Sports', 'Danh mục thể thao', 7, TRUE, CURRENT_TIMESTAMP, NULL),
       ('88888888-8888-8888-8888-888888888888', 'Music', 'Danh mục âm nhạc', 8, TRUE, CURRENT_TIMESTAMP, NULL),
       ('99999999-9999-9999-9999-999999999999', 'Books', 'Danh mục sách', 9, TRUE, CURRENT_TIMESTAMP, NULL),
       ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Lifestyle', 'Danh mục phong cách sống', 10, TRUE, CURRENT_TIMESTAMP,
        NULL) ON CONFLICT (name) DO NOTHING;

--rollback DELETE FROM public.categories
--rollback WHERE name IN (
--rollback     'Technology',
--rollback     'Education',
--rollback     'Health',
--rollback     'Finance',
--rollback     'Travel',
--rollback     'Food',
--rollback     'Sports',
--rollback     'Music',
--rollback     'Books',
--rollback     'Lifestyle'
--rollback );
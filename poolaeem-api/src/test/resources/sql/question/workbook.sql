delete from `workbook`;

insert into `workbook` (id, user_id, name, description, problem_count, solved_count, theme, workbook_order, is_deleted,
                        created_by, updated_by, created_at, updated_at)
values ('workbook-1', 'user-1', '고등영어1', '고등학교에서 사용하는 영단어 문제입니다.', 3, 2, 'PINK', 1, false, 'user-1', 'user-1',
        '2023-07-04 14:36:23.599', '2023-07-04 14:36:23.599'),
       ('workbook-2', 'user-2', '초등영어1', '초등학교 필수 영단어', 5, 2, 'PINK', 1, false, 'user-2', 'user-2',
        '2023-07-04 22:42:23.599', '2023-07-04 22:42:23.599'),
       ('workbook-3', 'user-1', '신생영어 - 상', '새롭게 나온 영어 단어 암기 시스템', 0, 0, 'PINK', 2, false, 'user-1', 'user-1',
        '2023-08-16 15:15:23.599', '2023-08-16 15:15:23.599')
;



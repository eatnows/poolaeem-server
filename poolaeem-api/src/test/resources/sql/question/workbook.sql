delete from `workbook`;

insert into `workbook` (id, user_id, name, description, question_count, solved_count, theme, workbook_order, is_deleted,
                        created_by, updated_by, created_at, updated_at)
values ('workbook-1', 'user-1', '고등영어1', '고등학교에서 사용하는 영단어 문제입니다.', 3, 2, 'PINK', 1, false, 'user-1', 'user-1',
        '2023-07-04 14:36:23.599', '2023-07-04 14:36:23.599')
;



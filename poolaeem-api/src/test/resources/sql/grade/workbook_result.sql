delete from `workbook_result`;

insert into `workbook_result` (`id`, `workbook_id`, `user_id`, `user_name`, `total_questions`, `correct_count`,
                               `created_at`)
values ('w-result-1', 'workbook-1', 'user-1', '풀내임', 3, 3, '2023-07-23 21:38:12.121'),
       ('w-result-2', 'workbook-1', null, '원어민', 3, 2, '2023-07-23 21:40:10.593'),
       ('w-result-3', 'workbook-2', 'user-1', '풀내임', 5, 3, '2023-07-23 21:40:12.121'),
       ('w-result-4', 'workbook-2', null, '사과농장', 5, 0, '2023-07-23 21:41:55.990')
;

delete from `problem`;

insert into `problem` (`id`, `workbook_id`, `question`, `problem_order`, `is_deleted`, `created_by`, `updated_by`,
                       `created_at`, `updated_at`)
values ('problem-1', 'workbook-1', 'Word', 1, false, 'user-1', 'user-1', '2023-07-09 22:40:31.511',
        '2023-07-09 22:40:31.511'),
       ('problem-2', 'workbook-1', 'School', 2, false, 'user-1', 'user-1', '2023-07-09 22:40:32.511',
        '2023-07-09 22:40:32.511')
;


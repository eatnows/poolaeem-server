delete from `problem_result`;

insert into `problem_result` (`id`, `user_id`, `problem_id`, `is_correct`, `created_at`)
values ('p-result-1', 'user-1', 'problem-1', true, '2023-07-23 21:38:12.222'),
       ('p-result-2', 'user-1', 'problem-2', true, '2023-07-23 21:38:12.232'),
       ('p-result-3', 'user-1', 'problem-3', true, '2023-07-23 21:38:12.242')
;
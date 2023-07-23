delete from `answer_result`;

insert into `answer_result` (`id`, `user_id`, `problem_id`, `answer`, `is_correct`, `created_at`)
values ('a-result-1', 'user-1', 'problem-1', '세계', true, '2023-07-09 22:40:32.111'),
       ('a-result-2', 'user-1', 'problem-2', '학교', true, '2023-07-09 22:40:32.121'),
       ('a-result-3', 'user-1', 'problem-3', '비디오', true, '2023-07-09 22:40:32.131'),
       ('a-result-4', 'user-1', 'problem-3', '동영상', true, '2023-07-09 22:40:32.141')
;
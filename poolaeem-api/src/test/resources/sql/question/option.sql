delete from `problem_option`;

insert into `problem_option` (`id`, `problem_id`, `value`, `is_correct`, `option_order`, `is_deleted`, `created_by`,
                              `updated_by`, `created_at`, `updated_at`)
values ('option-1', 'problem-1', '단어', true, 1, false, 'user-1', 'user-1', '2023-07-09 22:40:31.612',
        '2023-07-09 22:40:31.612'),
       ('option-2', 'problem-1', '세계', false, 2, false, 'user-1', 'user-1', '2023-07-09 22:40:31.623',
        '2023-07-09 22:40:31.623'),
       ('option-3', 'problem-2', '스쿨버스', false, 1, false, 'user-1', 'user-1', '2023-07-09 22:40:32.623',
        '2023-07-09 22:40:32.623'),
       ('option-4', 'problem-2', '수업', false, 2, false, 'user-1', 'user-1', '2023-07-09 22:40:32.643',
        '2023-07-09 22:40:32.643'),
       ('option-5', 'problem-2', '학교', true, 3, false, 'user-1', 'user-1', '2023-07-09 22:40:32.653',
        '2023-07-09 22:40:32.653'),
       ('option-6', 'problem-3', '비디오', true, 1, false, 'user-1', 'user-1', '2023-07-09 22:40:52.653',
        '2023-07-09 22:40:52.653'),
       ('option-7', 'problem-3', '동영상', true, 2, false, 'user-1', 'user-1', '2023-07-09 22:40:52.753',
        '2023-07-09 22:40:52.753'),
       ('option-8', 'problem-3', '이미지', false, 3, false, 'user-1', 'user-1', '2023-07-09 22:40:52.763',
        '2023-07-09 22:40:52.763')
;


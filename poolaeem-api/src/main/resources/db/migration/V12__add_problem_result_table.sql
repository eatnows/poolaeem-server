CREATE TABLE problem_result (
    `id` varchar(32) not null,
    `user_id` varchar(32) default null,
    `problem_id` varchar(32) not null,
    `is_correct` bit(1) not null,
    `created_at` datetime(3) not null,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create index idx_user_id on `problem_result` (`user_id`);
create index idx_problem_id on `problem_result` (`problem_id`);

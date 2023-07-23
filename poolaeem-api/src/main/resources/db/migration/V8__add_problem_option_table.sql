CREATE TABLE problem_option (
    `id` varchar(32) not null,
    `problem_id` varchar(32) not null,
    `value` varchar(300) not null,
    `is_correct` bit(1) not null,
    `option_order` int(11) default 1 not null,
    `is_deleted` bit(1) default 0 not null,
    `created_by` varchar(32) not null,
    `updated_by` varchar(32) not null,
    `created_at` datetime(3) not null,
    `updated_at` datetime(3) not null,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create index idx_problem_id on `problem_option` (`problem_id`);
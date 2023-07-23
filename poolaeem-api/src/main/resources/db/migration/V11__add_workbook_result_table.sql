CREATE TABLE workbook_result (
    `id` varchar(32) not null,
    `workbook_id` varchar(32) not null,
    `user_id` varchar(32) default null,
    `user_name` varchar(300) not null,
    `total_questions` smallint(11) unsigned default 0 not null,
    `correct_count` smallint(11) unsigned default 0 not null,
    `created_at` datetime(3) not null,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create index idx_workbook_id on `workbook_result` (`workbook_id`);
create index idx_user_id on `workbook_result` (`user_id`);
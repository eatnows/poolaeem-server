CREATE TABLE problem (
    `id` varchar(32) not null,
    `workbook_id` varchar(32) not null,
    `question` varchar(300) not null,
    `problem_order` int(11) default 1 not null,
    `is_deleted` bit(1) default 0 not null,
    `created_by` varchar(32) not null,
    `updated_by` varchar(32) not null,
    `created_at` datetime(3) not null,
    `updated_at` datetime(3) not null,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create index idx_workbook_id on `problem` (`workbook_id`);
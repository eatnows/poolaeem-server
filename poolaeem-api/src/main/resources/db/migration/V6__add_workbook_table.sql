CREATE TABLE workbook (
    `id` varchar(32) not null,
    `name` varchar(200) not null,
    `description` varchar(500) not null,
    `question_count` int(11) not null,
    `solved_count` int(11) not null,
    `theme` varchar(15) not null,
    `workbook_order` int(11) default 1 not null,
    `is_deleted` bit(1) default 0 not null,
    `created_by` varchar(32) not null,
    `updated_by` varchar(32) not null,
    `created_at` datetime(3) not null,
    `updated_at` datetime(3) not null,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
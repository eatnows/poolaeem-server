CREATE TABLE file (
`id` varchar(32) not null,
`file_name` varchar(500) not null,
`file_size` varchar(11) not null,
`file_extension` varchar(30) not null,
`path` varchar(100) not null,
`is_deleted` bit(1) default 0 not null,
`created_by` varchar(32) not null,
`updated_by` varchar(32) not null,
`created_at` datetime(3) not null,
`updated_at` datetime(3) not null,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

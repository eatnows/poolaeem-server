CREATE TABLE logged_in_history (
    `id` varchar(32) not null,
    `user_id` varchar(32) default null,
    `logged_at` datetime(3) not null,
    `client_ip` varchar(100) not null,
    `user_agent` varchar(255) not null,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

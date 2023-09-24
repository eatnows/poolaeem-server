CREATE TABLE logged_in_user_jwt (
    `id` varchar(32) not null,
    `user_id` varchar(32) not null,
    `token` varchar(700) not null,
    `client_ip` varchar(100) not null,
    `user_agent` varchar(255) not null,
    `issued_at` datetime(3) not null,
    `expires_at` datetime(3) not null,
    `created_at` datetime(3) not null,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create index idx_user_id on `logged_in_user_jwt` (`user_id`);
create index idx_token on `logged_in_user_jwt` (`token`);

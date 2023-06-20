CREATE TABLE user (
     `id` varchar(32) not null,
     `email` varchar(200) not null,
     `name` varchar(100) not null,
     `oauth_provider` varchar(30) not null,
     `oauth_id` varchar(300) not null,
     `profile_image` varchar(32) default null,
     `terms_version` varchar(10) not null,
     `is_deleted` bit(1) DEFAULT 0 not null,
     `updated_by` varchar(32) not null,
     `created_at` datetime(3) not null,
     `updated_at` datetime(3) not null,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

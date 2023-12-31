CREATE TABLE IF NOT EXISTS `oauth2_authorized_client` (
    `client_registration_id` varchar(100) NOT NULL,
    `principal_name` varchar(200) NOT NULL,
    `access_token_type` varchar(100) NOT NULL,
    `access_token_value` blob NOT NULL,
    `access_token_issued_at` timestamp NOT NULL,
    `access_token_expires_at` timestamp NOT NULL,
    `access_token_scopes` varchar(1000) DEFAULT NULL,
    `refresh_token_value` blob,
    `refresh_token_issued_at` timestamp NULL DEFAULT NULL,
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`client_registration_id`,`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
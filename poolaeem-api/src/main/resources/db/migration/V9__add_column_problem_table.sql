ALTER TABLE `problem` ADD COLUMN `option_count` tinyint(11) NOT NULL AFTER `question`;
ALTER TABLE `problem` ADD COLUMN `type` varchar(15) NOT NULL AFTER `question`;
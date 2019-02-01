CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `uuid` binary(16) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `lastname` varchar(255) DEFAULT NULL,
  `is_enabled` bit(1) NOT NULL,
  `created_by_id` bigint(20) DEFAULT NULL,
  `last_modified_by_id` bigint(20) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_user_username` (`username`),
  KEY `IX_user_created_by_id` (`created_by_id`),
  KEY `IX_user_last_modified_by_id` (`last_modified_by_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `uuid` binary(16) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_role_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FK_user_role_role_id` (`role_id`),
  CONSTRAINT `FK_user_role_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK_user_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_date` datetime(6) DEFAULT NULL,
  `last_modified_date` datetime(6) DEFAULT NULL,
  `uuid` binary(16) NOT NULL,
  `version` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_authority_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `role_authority` (
  `role_id` bigint(20) NOT NULL,
  `authority_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`authority_id`),
  KEY `FK_role_authority_authority_id` (`authority_id`),
  CONSTRAINT `FK_role_authority_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `FK_role_authority_authority_id` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `revinfo` (
  `id` int(11) NOT NULL,
  `timestamp` bigint(20) NOT NULL,
  `ip` varchar(46) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IX_revinfo_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `revchanges` (
  `rev` int(11) NOT NULL,
  `entityname` varchar(255) DEFAULT NULL,
  KEY `FK_revchanges_rev` (`rev`),
  CONSTRAINT `FK_revchanges_rev` FOREIGN KEY (`rev`) REFERENCES `revinfo` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
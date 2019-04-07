CREATE TABLE `locations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  `description` varchar(5000) DEFAULT NULL,
  `url` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `events` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  `description` varchar(5000) DEFAULT NULL,
  `url` varchar(1000) DEFAULT NULL,
  `mediaUrl` varchar(1000) DEFAULT NULL,
  `isPublic` tinyint(1) DEFAULT '0',
  `party` varchar(255) NOT NULL,
  `location_id` bigint(20) DEFAULT NULL,
  `startTime` bigint(20) NOT NULL,
  `endTime` bigint(20) NOT NULL,
  `originalStartTime` bigint(20) DEFAULT NULL,
  `prepStartTime` bigint(20) DEFAULT NULL,
  `postEndTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_events_location_id` (`location_id`),
  CONSTRAINT `FK_events_location_id` FOREIGN KEY (`location_id`) REFERENCES `locations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tags` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `events_tags` (
  `event_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  PRIMARY KEY (`event_id`,`tag_id`),
  KEY `FK_events_tags_tag_id` (`tag_id`),
  CONSTRAINT `FK_events_tags_event_id` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`),
  CONSTRAINT `FK_events_tags_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `locations` ADD UNIQUE INDEX `name` (`name`(255));
ALTER TABLE `tags` ADD UNIQUE INDEX `name` (`name`(255));
ALTER TABLE `events` ADD UNIQUE INDEX `name_party` (`name`(255), `party`(64));
ALTER TABLE `events` ADD INDEX `party` (`party`(255));

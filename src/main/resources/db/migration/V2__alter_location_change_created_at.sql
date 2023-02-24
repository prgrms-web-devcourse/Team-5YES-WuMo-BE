ALTER TABLE `location` CHANGE `createdAt` `created_at` TIMESTAMP;

ALTER TABLE `location` CHANGE `updatedAt` `updated_at` TIMESTAMP;

ALTER TABLE `comment` CHANGE `DTYPE` `dtype` VARCHAR(20);

ALTER TABLE `invitation` ADD COLUMN `created_at` TIMESTAMP NOT NULL;

ALTER TABLE `invitation` ADD COLUMN `updated_at` TIMESTAMP NOT NULL;

ALTER TABLE `member` MODIFY `email` VARCHAR(50);

ALTER TABLE `member` MODIFY `nickname` VARCHAR(20);

ALTER TABLE `party_route_comment` MODIFY `id` BIGINT;

ALTER TABLE `member`
DROP INDEX `email`,
ADD UNIQUE KEY `user_index_email` (`email`);

ALTER TABLE `member`
DROP INDEX `nickname`,
ADD UNIQUE KEY `user_index_nickname` (`nickname`);
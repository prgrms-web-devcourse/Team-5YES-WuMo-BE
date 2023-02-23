CREATE TABLE IF NOT EXISTS `member` (
`id`	        BIGINT	        NOT NULL,
`email`	        VARCHAR(255)	NOT NULL,
`nickname`	    VARCHAR(255)	NOT NULL,
`password`	    VARCHAR(60)	    NOT NULL,
`image_url`	    VARCHAR(255)	NULL,
`created_at`	TIMESTAMP	    NOT NULL,
`updated_at`	TIMESTAMP	    NOT NULL
);

CREATE TABLE IF NOT EXISTS `party_member` (
`id`	        BIGINT	    NOT NULL,
`member_id`	    BIGINT	    NOT NULL,
`party_id`	    BIGINT	    NOT NULL,
`role`	        VARCHAR(10)	NULL,
`is_leader`	    BIT	        NOT NULL    DEFAULT 0,
`created_at`	TIMESTAMP	NOT NULL,
`updated_at`	TIMESTAMP	NOT NULL
);

CREATE TABLE IF NOT EXISTS `party` (
`id`	        BIGINT	        NOT NULL,
`name`	        VARCHAR(40)	    NOT NULL,
`start_date`	TIMESTAMP	    NOT NULL,
`end_date`	    TIMESTAMP	    NOT NULL,
`description`	VARCHAR(100)	NULL,
`image_url`	    VARCHAR(255)	NOT NULL,
`created_at`	TIMESTAMP	    NOT NULL,
`updated_at`	TIMESTAMP	    NOT NULL,
`password`	    VARCHAR(4)	    NULL
);

CREATE TABLE IF NOT EXISTS `invitation` (
`id`	        BIGINT	        NOT NULL,
`party_id`	    BIGINT	        NOT NULL,
`expired_date`	TIMESTAMP	    NOT NULL,
`code`	        VARCHAR(255)	NOT NULL
);

CREATE TABLE IF NOT EXISTS `location` (
`id`	        BIGINT	        NOT NULL,
`name`	        VARCHAR(50)	    NOT NULL,
`address`	    VARCHAR(100)	NOT NULL,
`latitude`	    FLOAT	        NOT NULL,
`longitude`	    FLOAT	        NOT NULL,
`image_url`	    VARCHAR(255)	NOT NULL,
`category`	    VARCHAR(20)	    NOT NULL,
`description`	VARCHAR(50)	    NULL,
`visit_date`	TIMESTAMP	    NOT NULL,
`expected_cost`	INT	            NOT NULL,
`spending`	    INT	            NOT NULL    DEFAULT 0,
`createdAt`	    TIMESTAMP	    NOT NULL,
`updatedAt`	    TIMESTAMP	    NOT NULL,
`party_id`	    BIGINT	        NOT NULL,
`route_id`	    BIGINT	        NULL
);

CREATE TABLE IF NOT EXISTS `route` (
`id`	        BIGINT	        NOT NULL,
`name`	        VARCHAR(20)	    NULL,
`is_public`	    BIT	            NOT NULL    DEFAULT 0,
`created_at`	TIMESTAMP	    NOT NULL,
`updated_at`	TIMESTAMP	    NOT NULL,
`party_id`	    BIGINT	        NOT NULL
);

CREATE TABLE IF NOT EXISTS `comment` (
`id`	        BIGINT	        NOT NULL,
`member_id`	    BIGINT	        NOT NULL,
`content`	    VARCHAR(255)	NULL,
`image_url`	    VARCHAR(255)	NULL,
`DTYPE`	        VARCHAR(20)	    NOT NULL,
`is_edited`	    BIT	            NOT NULL DEFAULT 0,
`created_at`	TIMESTAMP	    NOT NULL,
`updated_at`	TIMESTAMP	    NOT NULL
);

CREATE TABLE IF NOT EXISTS `location_comment` (
`id`	            BIGINT	NOT NULL,
`location_id`	    BIGINT	NOT NULL,
`party_member_id`	BIGINT	NOT NULL
);

CREATE TABLE IF NOT EXISTS `party_route_comment` (
`id`	            VARCHAR(255)	NOT NULL,
`route_id`	        BIGINT	        NOT NULL,
`party_member_id`	BIGINT	        NOT NULL
);

ALTER TABLE `party_member` ADD CONSTRAINT `PK_PARTY_MEMBER` PRIMARY KEY (`id`);

ALTER TABLE `member` ADD CONSTRAINT `PK_MEMBER` PRIMARY KEY (`id`);

ALTER TABLE `party` ADD CONSTRAINT `PK_PARTY` PRIMARY KEY (`id`);

ALTER TABLE `comment` ADD CONSTRAINT `PK_COMMENT` PRIMARY KEY (`id`);

ALTER TABLE `route` ADD CONSTRAINT `PK_ROUTE` PRIMARY KEY (`id`);

ALTER TABLE `location` ADD CONSTRAINT `PK_LOCATION` PRIMARY KEY (`id`);

ALTER TABLE `invitation` ADD CONSTRAINT `PK_INVITATION` PRIMARY KEY (`id`);

ALTER TABLE `location_comment` ADD CONSTRAINT `PK_LOCATION_COMMENT` PRIMARY KEY (`id`);

ALTER TABLE `party_route_comment` ADD CONSTRAINT `PK_PARTY_ROUTE_COMMENT` PRIMARY KEY (`id`);

ALTER TABLE `invitation` MODIFY COLUMN `code` VARCHAR(255) UNIQUE;

ALTER TABLE `member` MODIFY COLUMN `email` VARCHAR(255) UNIQUE;

ALTER TABLE `member` MODIFY COLUMN `nickname` VARCHAR(255) UNIQUE;
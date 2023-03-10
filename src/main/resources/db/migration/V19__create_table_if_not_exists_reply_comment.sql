CREATE TABLE IF NOT EXISTS `reply_comment` (
`id`	        BIGINT	        NOT NULL PRIMARY KEY AUTO_INCREMENT,
`member_id`	    BIGINT	        NOT NULL,
`comment_id`    BIGINT          NOT NULL,
`content`	    VARCHAR(255)	NOT NULL,
`created_at`	TIMESTAMP	    NOT NULL,
`updated_at`	TIMESTAMP	    NOT NULL
);
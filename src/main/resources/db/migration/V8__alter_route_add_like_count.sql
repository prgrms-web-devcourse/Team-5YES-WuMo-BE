ALTER TABLE `route`
    ADD `like_count` BIGINT NOT NULL DEFAULT 0 AFTER `is_public`;
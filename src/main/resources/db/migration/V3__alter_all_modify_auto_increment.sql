ALTER TABLE `member`              MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `party_member`        MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `party`               MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `invitation`          MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `location`            MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `route`               MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `comment`             MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `location_comment`    MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
ALTER TABLE `party_route_comment` MODIFY id BIGINT NOT NULL AUTO_INCREMENT;
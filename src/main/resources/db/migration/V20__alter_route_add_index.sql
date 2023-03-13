ALTER TABLE `route`
    DROP INDEX `index_route`;
ALTER TABLE `route`
    ADD INDEX `index_route_party_id` (party_id);
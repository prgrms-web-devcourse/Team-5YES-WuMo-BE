ALTER TABLE `location`
    ADD INDEX `index_location_search` (route_id, search_address);
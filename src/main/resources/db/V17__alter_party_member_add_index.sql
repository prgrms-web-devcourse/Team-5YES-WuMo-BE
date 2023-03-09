ALTER TABLE party_member
    ADD INDEX `index_party_member_party_id` (party_id);
ALTER TABLE party_member
    ADD INDEX `index_party_member_party_id_and_member_id` (party_id, member_id);
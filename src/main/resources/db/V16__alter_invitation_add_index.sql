ALTER TABLE invitation
    ADD INDEX `index_invitation_id_desc` (party_id, id DESC);
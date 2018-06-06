-- 06th Mar 2017 
-- Balaji 
ALTER TABLE `outlook_login`
	CHANGE COLUMN `resource_id` `resource_id` VARCHAR(1000) NOT NULL AFTER `client_id`;
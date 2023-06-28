ALTER TABLE `rozmer-apis`.`answer` 
CHANGE COLUMN `answer` `answer` VARCHAR(1000) NOT NULL ;

ALTER TABLE `rozmer-apis`.`comments` 
CHANGE COLUMN `content` `content` VARCHAR(1000) NULL DEFAULT NULL ;

ALTER TABLE `rozmer-apis`.`question` 
CHANGE COLUMN `question` `question` VARCHAR(1000) NOT NULL ;
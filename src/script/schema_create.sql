CREATE SCHEMA `landauction` ;

CREATE TABLE `landauction`.`location` (
  `id` INT NOT NULL AUTO_INCREMENT, 
  `lat` DOUBLE NOT NULL,
  `lng` DOUBLE NOT NULL,
  PRIMARY KEY (`id`));

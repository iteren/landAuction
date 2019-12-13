CREATE SCHEMA `landauction` ;

CREATE TABLE `landauction`.`location` (
  `id` INT NOT NULL AUTO_INCREMENT, 
  `lat` DOUBLE NOT NULL,
  `lng` DOUBLE NOT NULL,
  PRIMARY KEY (`id`));
ALTER DATABASE landauction CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

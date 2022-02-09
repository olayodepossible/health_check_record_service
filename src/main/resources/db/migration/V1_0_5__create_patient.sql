-- -----------------------------------------------------
-- Table `patient`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `patient` ;

CREATE TABLE IF NOT EXISTS `patient` (
    `patient_id` INT NOT NULL AUTO_INCREMENT,
    `source_id` VARCHAR(100) NOT NULL,
    `first_name` VARCHAR(100) NOT NULL,
    `middle_initial` VARCHAR(1) NOT NULL,
    `last_name` VARCHAR(100) NOT NULL,
    `email_address` VARCHAR(200) NOT NULL,
    `phone_number` VARCHAR(50) NOT NULL,
    `street` VARCHAR(255) NOT NULL,
    `city` VARCHAR(255) NOT NULL,
    `state` VARCHAR(100) NOT NULL,
    `zip_code` VARCHAR(20) NOT NULL,
    `birth_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `social_security_number` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`patient_id`))
    ENGINE = InnoDB;






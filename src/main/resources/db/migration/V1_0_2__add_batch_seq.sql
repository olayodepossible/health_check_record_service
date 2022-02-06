-- -----------------------------------------------------
-- Table `BATCH_STEP_EXECUTION_SEQ`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_SEQ` ;

CREATE TABLE IF NOT EXISTS  `BATCH_STEP_EXECUTION_SEQ` (`ID` BIGINT NOT NULL) ENGINE = InnoDB;
INSERT INTO `BATCH_STEP_EXECUTION_SEQ` values(0);

-- -----------------------------------------------------
-- Table `BATCH_JOB_EXECUTION_SEQ`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_SEQ` ;

CREATE TABLE IF NOT EXISTS  `BATCH_JOB_EXECUTION_SEQ` (ID BIGINT NOT NULL) ENGINE = InnoDB;
INSERT INTO `BATCH_JOB_EXECUTION_SEQ` values(0);

-- -----------------------------------------------------
-- Table `BATCH_JOB_SEQ`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `BATCH_JOB_SEQ` ;

CREATE TABLE IF NOT EXISTS  `BATCH_JOB_SEQ` (`ID` BIGINT NOT NULL) ENGINE = InnoDB;
INSERT INTO `BATCH_JOB_SEQ` values(0);
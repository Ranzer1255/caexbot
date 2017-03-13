
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema caexDB2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema caexDB2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `caexDB2` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Schema cekipDB2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cekipDB2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cekipDB2` ;
USE `caexDB2` ;

-- -----------------------------------------------------
-- Table `caexDB2`.`guild`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `caexDB2`.`guild` (
  `guild_id` VARCHAR(20) NOT NULL COMMENT 'Guild ID of guild from Discord',
  `prefix` VARCHAR(45) NULL DEFAULT NULL COMMENT 'the set prefix for this guild, if null use global default',
  `chan_announcement` VARCHAR(20) NULL DEFAULT NULL COMMENT 'channel caex will use to make announcements in. if Null use guild\'s Public channel',
  `def_chan_music` VARCHAR(20) NULL DEFAULT NULL COMMENT 'channel caex will announce changes in music in. if null, reply in last used music command channel.',
  `xp_annouce` TINYINT(1) NULL DEFAULT 1 COMMENT 'if true caex will announce when a user levels up. if false, he\'ll send a private message.',
  PRIMARY KEY (`guild_id`),
  UNIQUE INDEX `idguild_UNIQUE` (`guild_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `caexDB2`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `caexDB2`.`member` (
  `guild_id` VARCHAR(20) NOT NULL,
  `user_id` VARCHAR(20) NOT NULL,
  `xp` INT NULL DEFAULT 0,
  `last_xp` DOUBLE NULL DEFAULT 0 COMMENT 'this houses the long value for the date of the last xp gained',
  PRIMARY KEY (`guild_id`, `user_id`),
  INDEX `guild_id_idx` (`guild_id` ASC),
  CONSTRAINT `guild_id`
    FOREIGN KEY (`guild_id`)
    REFERENCES `caexDB2`.`guild` (`guild_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `caexDB2`.`text_channel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `caexDB2`.`text_channel` (
  `text_channel_id` VARCHAR(20) NOT NULL COMMENT 'ID of text channel from JDA/Discord',
  `guild_id` VARCHAR(20) NOT NULL COMMENT 'Guild ID of associated with this channel from Discord',
  `perm_game` TINYINT(1) NULL DEFAULT 1 COMMENT 'allow text based games in this channel',
  `perm_xp` TINYINT(1) NULL DEFAULT 1 COMMENT 'true if members earn XP in this channel',
  `perm_music` TINYINT(1) NULL DEFAULT 1 COMMENT 'allow music commands in this channel',
  PRIMARY KEY (`text_channel_id`),
  INDEX `guild_id_idx` (`guild_id` ASC),
  UNIQUE INDEX `text_channel_id_UNIQUE` (`text_channel_id` ASC),
  CONSTRAINT `TextGuild`
    FOREIGN KEY (`guild_id`)
    REFERENCES `caexDB2`.`guild` (`guild_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `caexDB2`.`voice_channel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `caexDB2`.`voice_channel` (
  `voice_channel_id` VARCHAR(20) NOT NULL,
  `guild_id` VARCHAR(20) NOT NULL,
  `perm_xp` TINYINT(1) NULL DEFAULT 1 COMMENT 'if true users earn XP for being in this channel',
  `perm_music` TINYINT(1) NULL DEFAULT 1 COMMENT 'caex can play music in this channel',
  PRIMARY KEY (`voice_channel_id`),
  INDEX `guild ID_idx` (`guild_id` ASC),
  UNIQUE INDEX `voice_channel_id_UNIQUE` (`voice_channel_id` ASC),
  CONSTRAINT `VoiceGuild`
    FOREIGN KEY (`guild_id`)
    REFERENCES `caexDB2`.`guild` (`guild_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `cekipDB2` ;

-- -----------------------------------------------------
-- Table `cekipDB2`.`guild`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cekipDB2`.`guild` (
  `guild_id` VARCHAR(20) NOT NULL COMMENT 'Guild ID of guild from Discord',
  `prefix` VARCHAR(45) NULL DEFAULT NULL COMMENT 'the set prefix for this guild, if null use global default',
  `chan_announcement` VARCHAR(20) NULL DEFAULT NULL COMMENT 'channel caex will use to make announcements in. if Null use guild\'s Public channel',
  `def_chan_music` VARCHAR(20) NULL COMMENT 'channel caex will announce changes in music in. if null, reply in last used music command channel.',
  `xp_annouce` TINYINT(1) NULL DEFAULT 1 COMMENT 'if true caex will announce when a user levels up. if false, he\'ll send a private message.',
  PRIMARY KEY (`guild_id`),
  UNIQUE INDEX `idguild_UNIQUE` (`guild_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cekipDB2`.`member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cekipDB2`.`member` (
  `guild_id` VARCHAR(20) NOT NULL,
  `user_id` VARCHAR(20) NOT NULL,
  `xp` INT NULL DEFAULT 0,
  `last_xp` DOUBLE NULL DEFAULT 0 COMMENT 'this houses the long value for the date of the last xp gained',
  PRIMARY KEY (`guild_id`, `user_id`),
  CONSTRAINT `guild_ID`
    FOREIGN KEY (`guild_id`)
    REFERENCES `cekipDB2`.`guild` (`guild_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cekipDB2`.`text_channel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cekipDB2`.`text_channel` (
  `channel_id` VARCHAR(20) NOT NULL COMMENT 'ID of text channel from JDA/Discord',
  `guild_id` VARCHAR(20) NOT NULL COMMENT 'Guild ID of associated with this channel from Discord',
  `perm_game` TINYINT(1) NULL DEFAULT 1 COMMENT 'allow text based games in this channel',
  `perm_xp` TINYINT(1) NULL DEFAULT 1 COMMENT 'true if members earn XP in this channel',
  `perm_music` TINYINT(1) NULL DEFAULT 1 COMMENT 'allow music commands in this channel',
  PRIMARY KEY (`channel_id`),
  INDEX `guild_id_idx` (`guild_id` ASC),
  UNIQUE INDEX `channel_id_UNIQUE` (`channel_id` ASC),
  CONSTRAINT `textGuild`
    FOREIGN KEY (`guild_id`)
    REFERENCES `cekipDB2`.`guild` (`guild_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cekipDB2`.`voice_channel`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cekipDB2`.`voice_channel` (
  `channel_id` VARCHAR(20) NOT NULL,
  `guild_id` VARCHAR(20) NOT NULL,
  `perm_xp` TINYINT(1) NULL DEFAULT 1 COMMENT 'if true users earn XP for being in this channel',
  `perm_music` TINYINT(1) NULL DEFAULT 1 COMMENT 'caex can play music in this channel',
  PRIMARY KEY (`channel_id`),
  INDEX `guild ID_idx` (`guild_id` ASC),
  UNIQUE INDEX `channel_id_UNIQUE` (`channel_id` ASC),
  CONSTRAINT `VoiceGuild`
    FOREIGN KEY (`guild_id`)
    REFERENCES `cekipDB2`.`guild` (`guild_id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

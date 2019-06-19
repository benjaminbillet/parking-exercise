/* =========== Create the database =========== */
CREATE DATABASE IF NOT EXISTS parkingtoll CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

/* =========== Create the tables =========== */
CREATE TABLE IF NOT EXISTS `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `car` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `hour_fee_pricing` (
  `id` bigint(20) NOT NULL,
  `hour_fee` double NOT NULL,
  `minimum_fee` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `parking` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `pricing_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `parking_spot` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `label` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `occupied_since` datetime(6) DEFAULT NULL,
  `occupied_by_id` bigint(20) DEFAULT NULL,
  `parking_id` bigint(20) NOT NULL,
  `type_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKftadx8qmr3s84h3dokofx5vwh` (`occupied_by_id`),
  KEY `FKdphrc3wvv0h91p3yi13erlyy9` (`parking_id`),
  KEY `FKoe2wm57iy4it65gcg3wg747ao` (`type_id`),
  CONSTRAINT `FKdphrc3wvv0h91p3yi13erlyy9` FOREIGN KEY (`parking_id`) REFERENCES `parking` (`id`),
  CONSTRAINT `FKftadx8qmr3s84h3dokofx5vwh` FOREIGN KEY (`occupied_by_id`) REFERENCES `car` (`id`),
  CONSTRAINT `FKoe2wm57iy4it65gcg3wg747ao` FOREIGN KEY (`type_id`) REFERENCES `parking_spot_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

CREATE TABLE IF NOT EXISTS `parking_spot_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `label` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


/* =========== Create data =========== */
LOCK TABLES `hibernate_sequence` WRITE;
INSERT INTO `hibernate_sequence` VALUES (2);
UNLOCK TABLES;

LOCK TABLES `hour_fee_pricing` WRITE;
INSERT INTO `hour_fee_pricing` VALUES (1,1.5,0);
UNLOCK TABLES;

LOCK TABLES `parking` WRITE;
INSERT INTO `parking` VALUES (1,'Q-Park Pré-des-Pêcheurs',1);
UNLOCK TABLES;

LOCK TABLES `parking_spot` WRITE;
INSERT INTO `parking_spot` VALUES (1,'A0',NULL,NULL,1,1),(2,'B0',NULL,NULL,1,2),(3,'C0',NULL,NULL,1,3),(4,'A1',NULL,NULL,1,1),(5,'B1',NULL,NULL,1,2),(6,'C1',NULL,NULL,1,3),(7,'A2',NULL,NULL,1,1),(8,'B2',NULL,NULL,1,2),(9,'C2',NULL,NULL,1,3),(10,'A3',NULL,NULL,1,1),(11,'B3',NULL,NULL,1,2),(12,'C3',NULL,NULL,1,3),(13,'A4',NULL,NULL,1,1),(14,'B4',NULL,NULL,1,2),(15,'C4',NULL,NULL,1,3);
UNLOCK TABLES;

LOCK TABLES `parking_spot_type` WRITE;
INSERT INTO `parking_spot_type` VALUES (1,'No power supply'),(2,'20kW power supply'),(3,'50kW power supply');
UNLOCK TABLES;

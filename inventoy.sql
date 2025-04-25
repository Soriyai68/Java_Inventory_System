/*
SQLyog Enterprise - MySQL GUI v8.18 
MySQL - 5.5.5-10.4.32-MariaDB : Database - inventory_system
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`inventory_system` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;

USE `inventory_system`;

/*Table structure for table `adjuststocks` */

DROP TABLE IF EXISTS `adjuststocks`;

CREATE TABLE `adjuststocks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NOT NULL,
  `option` varchar(255) DEFAULT NULL,
  `adjust_stock` int(11) NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `date` date DEFAULT curdate(),
  `description` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `adjuststocks_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `adjuststocks` */

insert  into `adjuststocks`(`id`,`product_id`,`option`,`adjust_stock`,`price`,`total_price`,`date`,`description`) values (14,18,'bek',1,'12.00','12.00','2025-04-18',''),(17,32,'',10,'12.00','120.00','2025-04-18',''),(18,18,'Restock',7,'12.00','84.00','2025-04-20','bek bak'),(19,34,'Damage',20,'15.00','300.00','2025-04-20','');

/*Table structure for table `brands` */

DROP TABLE IF EXISTS `brands`;

CREATE TABLE `brands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `brands` */

insert  into `brands`(`id`,`name`,`description`) values (9,'CLEAR MEN',''),(10,'LUX',''),(11,'POND\'S',''),(12,'RIVACASE',''),(13,'SUNSILK','');

/*Table structure for table `categories` */

DROP TABLE IF EXISTS `categories`;

CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` int(11) NOT NULL,
  `description` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `categories` */

insert  into `categories`(`id`,`name`,`code`,`description`) values (7,'កាបូបយួរដៃ',10,''),(8,'ម្សៅលាបមុខ',11,''),(9,'សាប៊ូកក់សក់',12,''),(10,'សាប៊ូសម្អាតខ្លួន',14,'');

/*Table structure for table `customers` */

DROP TABLE IF EXISTS `customers`;

CREATE TABLE `customers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `phone` bigint(20) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `phone` (`phone`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `customers` */

insert  into `customers`(`id`,`name`,`phone`,`email`) values (4,'kakvey',120131346,'kakvey@gmail.com'),(5,'Seth',478954645,'seth@gmail.com'),(7,'yakuza',8128840,'soriya@gmail.com');

/*Table structure for table `openingstock` */

DROP TABLE IF EXISTS `openingstock`;

CREATE TABLE `openingstock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NOT NULL,
  `stock` int(11) NOT NULL,
  `date` date NOT NULL,
  `description` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `openingstock_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `openingstock` */

insert  into `openingstock`(`id`,`product_id`,`stock`,`date`,`description`) values (118,18,45,'2025-04-16',''),(120,32,50,'2025-04-18',''),(122,34,50,'2025-04-20','');

/*Table structure for table `products` */

DROP TABLE IF EXISTS `products`;

CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `sku` int(11) NOT NULL,
  `unit_id` int(11) NOT NULL,
  `brand_id` int(11) NOT NULL,
  `category_id` int(11) NOT NULL,
  `default_price` int(11) NOT NULL,
  `selling_price` int(11) NOT NULL,
  `imageUrl` varchar(100) DEFAULT NULL,
  `current_stock` int(11) DEFAULT NULL,
  `descriptions` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_sku` (`sku`),
  KEY `brand_id` (`brand_id`),
  KEY `category_id` (`category_id`),
  KEY `FK_products` (`unit_id`),
  CONSTRAINT `FK_products` FOREIGN KEY (`unit_id`) REFERENCES `units` (`id`),
  CONSTRAINT `products_ibfk_2` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`),
  CONSTRAINT `products_ibfk_3` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `products` */

insert  into `products`(`id`,`name`,`sku`,`unit_id`,`brand_id`,`category_id`,`default_price`,`selling_price`,`imageUrl`,`current_stock`,`descriptions`) values (18,'CLEAR Men Anti Dandruff Shampoo Deep Cleanse (សាប៊ូកក់សក់)',743,42,9,9,5000,12,'Uploads\\1741195622_10605_b8a50d20d893f7a2d3d7328a8f75762e.jpeg',50,''),(19,'CLEAR Men Shampoo Legend By CR7 (សាប៊ូកក់សក់)',750,42,9,9,10,12,'Uploads\\1741235785_10606_cfd498c94a674d95a6d8b3dce5442a8b.jpeg',0,''),(20,'LUX Botanical Skin Renegade (សាប៊ូដុះខ្លួន) 450ml',707,42,10,10,10,14,'Uploads\\1740990764_2.jpeg',0,''),(21,'LUX Botanicals Body Wash Skin Detox (សាប៊ូដុសខ្លួន) 450ml',708,42,10,10,10,14,'Uploads\\1740990680_1.jpeg',0,''),(22,'LUX Botanicals Liquid Glowing Skin (សាប៊ូដុសខ្លួន) 450ml',709,42,10,10,10,14,'Uploads\\1740990823_3.png',0,''),(23,'LUX Camellia White Whitening Body Wash (សាប៊ូទឹកដុសខ្លួន)',742,42,10,10,2,5,'Uploads\\1741195636_10664_ee8d35800e5f68cc08ee100d6559eacb.jpeg',0,''),(24,'Pond\'s Age Miracle Facial Cream Day Cream (គ្រីមលាបពេលថ្ងៃ) SPF 18 PA - 50g',746,41,11,8,10,17,'Uploads\\1741235235_10682_7f7a73e6595792e9cf49b0c8d8196879.png',0,''),(25,'Pond\'s SPF15 PA Flawless Radiance Derma Hydrating Day Gel for Normal Skin 50g',749,41,11,8,5,7,'Uploads\\1741235472_10679_42635cffaaa2925a337ea21d2c3a6749.jpeg',0,''),(26,'SUNSILK Shampoo Black Shine (សាប៊ូកក់សក់)',744,42,13,9,2,5,'Uploads\\1741229128_10608_9c115afd7f441f66e8f3b307ad9c8da6.jpeg',0,''),(27,'RIVACASE Biscayne Travel Organizer 5631',753,43,12,7,6,10,'Uploads\\1741236281_13221_17b474a9650762a79635ba19d4be0f51.jpeg',0,''),(28,'RIVACASE Biscayne Travel Organizer 5631',754,43,12,7,10,16,'Uploads\\1741236898_13226_e748c35abb8b48687d31b37a2d301c95.jpeg',0,''),(29,'RIVACASE Lantau Melange Crossbody Bag for Tablets 11inch 8811',751,43,12,7,20,30,'Uploads\\1741236281_13221_17b474a9650762a79635ba19d4be0f51.jpeg',0,''),(30,'RIVACASE Melange Macbook Pro and Untrabook 13.3inch 8825',752,43,12,7,30,50,'Uploads\\1741236898_13226_e748c35abb8b48687d31b37a2d301c95.jpeg',0,''),(32,'Coffee',1112,49,10,10,10,12,'Uploads\\1741236453_13226_e748c35abb8b48687d31b37a2d301c95.jpeg',70,''),(33,'Sao Soriya',786,41,9,7,12,15,'Uploads\\1741236281_13221_17b474a9650762a79635ba19d4be0f51.jpeg',0,''),(34,'sansil',11111,42,13,9,15,15,'Uploads\\photo_2025-04-20_13-35-25.jpg',50,'');

/*Table structure for table `purchases` */

DROP TABLE IF EXISTS `purchases`;

CREATE TABLE `purchases` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `suppliers_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `date` date NOT NULL,
  `status` varchar(20) NOT NULL,
  `stock` int(11) NOT NULL,
  `cost_price` int(11) NOT NULL,
  `description` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `suppliers_id` (`suppliers_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `purchases_ibfk_1` FOREIGN KEY (`suppliers_id`) REFERENCES `suppliers` (`id`),
  CONSTRAINT `purchases_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `purchases` */

insert  into `purchases`(`id`,`suppliers_id`,`product_id`,`date`,`status`,`stock`,`cost_price`,`description`) values (15,1,18,'2025-04-18','received',15,15,''),(16,1,32,'2025-04-18','received',50,10,''),(18,1,34,'2025-04-20','received',50,15,'');

/*Table structure for table `sells` */

DROP TABLE IF EXISTS `sells`;

CREATE TABLE `sells` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `sale_stock` int(11) NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `date` date DEFAULT curdate(),
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `sells_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `sells_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `sells` */

insert  into `sells`(`id`,`product_id`,`customer_id`,`sale_stock`,`price`,`total_price`,`description`,`date`) values (35,18,5,2,'12.00','24.00','','2025-04-18'),(37,32,7,20,'12.00','240.00','','2025-04-18'),(39,34,5,30,'15.00','450.00','','2025-04-20');

/*Table structure for table `suppliers` */

DROP TABLE IF EXISTS `suppliers`;

CREATE TABLE `suppliers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `phone` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `suppliers` */

insert  into `suppliers`(`id`,`name`,`phone`,`email`) values (1,'Sao Soriya',81218840,'soriya@gmail.com'),(2,'Visal',86957336,'visal@gmail.com'),(6,'samPhors',11111111,'haiyo@gmail.com');

/*Table structure for table `units` */

DROP TABLE IF EXISTS `units`;

CREATE TABLE `units` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `sub_name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `units` */

insert  into `units`(`id`,`name`,`sub_name`,`description`) values (41,'កំប៉ុង','កំប៉ុង',''),(42,'ដប','ដប',''),(43,'Pocket','Pocket',''),(48,'kg','kg',''),(49,'Box11','Box11','');

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pin` varchar(10) NOT NULL,
  `role` varchar(50) DEFAULT 'user',
  `created_at` date DEFAULT curdate(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `pin` (`pin`),
  CONSTRAINT `chk_pin` CHECK (`pin` regexp '^[0-9]+$')
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

/*Data for the table `users` */

insert  into `users`(`id`,`pin`,`role`,`created_at`) values (1,'12345','admin',NULL),(4,'1111','user',NULL),(5,'22222','user',NULL);

/* Trigger structure for table `adjuststocks` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `before_adjuststock_insert` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `before_adjuststock_insert` BEFORE INSERT ON `adjuststocks` FOR EACH ROW BEGIN
    DECLARE current_stock INT;
    SELECT current_stock INTO current_stock
    FROM Products
    WHERE id = NEW.product_id;
    -- Optional: Prevent negative stock
    -- IF current_stock + NEW.adjust_stock < 0 THEN
    --     SIGNAL SQLSTATE '45000'
    --     SET MESSAGE_TEXT = 'Stock adjustment would result in negative stock';
    -- END IF;
END */$$


DELIMITER ;

/* Trigger structure for table `adjuststocks` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `after_adjuststock_insert` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `after_adjuststock_insert` AFTER INSERT ON `adjuststocks` FOR EACH ROW BEGIN
    UPDATE Products
    SET current_stock = current_stock - NEW.adjust_stock
    WHERE id = NEW.product_id;
END */$$


DELIMITER ;

/* Trigger structure for table `adjuststocks` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `before_adjuststock_update` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `before_adjuststock_update` BEFORE UPDATE ON `adjuststocks` FOR EACH ROW BEGIN
    DECLARE current_stock INT;
    SELECT current_stock INTO current_stock
    FROM Products
    WHERE id = NEW.product_id;
    -- Optional: Prevent negative stock
    -- IF current_stock - OLD.adjust_stock + NEW.adjust_stock < 0 THEN
    --     SIGNAL SQLSTATE '45000'
    --     SET MESSAGE_TEXT = 'Stock adjustment update would result in negative stock';
    -- END IF;
END */$$


DELIMITER ;

/* Trigger structure for table `adjuststocks` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `after_adjuststock_update` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `after_adjuststock_update` AFTER UPDATE ON `adjuststocks` FOR EACH ROW BEGIN
    UPDATE Products
    SET current_stock = current_stock + OLD.adjust_stock - NEW.adjust_stock
    WHERE id = NEW.product_id;
END */$$


DELIMITER ;

/* Trigger structure for table `adjuststocks` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `after_adjuststock_delete` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `after_adjuststock_delete` AFTER DELETE ON `adjuststocks` FOR EACH ROW BEGIN
    UPDATE Products
    SET current_stock = current_stock + OLD.adjust_stock
    WHERE id = OLD.product_id;
END */$$


DELIMITER ;

/* Trigger structure for table `purchases` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `update_product_stock_and_price_after_purchase` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `update_product_stock_and_price_after_purchase` AFTER INSERT ON `purchases` FOR EACH ROW BEGIN
    IF NEW.status = 'received' THEN
        -- Update the stock and default_price in Products table
        UPDATE Products
        SET current_stock = current_stock + NEW.stock,
            default_price = NEW.cost_price
        WHERE id = NEW.product_id;
    END IF;
END */$$


DELIMITER ;

/* Trigger structure for table `purchases` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `update_product_stock_and_price_after_purchase_update` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `update_product_stock_and_price_after_purchase_update` AFTER UPDATE ON `purchases` FOR EACH ROW BEGIN
    -- If status changes to 'received' from something else
    IF NEW.status = 'received' AND OLD.status != 'received' THEN
        UPDATE Products
        SET current_stock = current_stock + NEW.stock,
            default_price = NEW.cost_price
        WHERE id = NEW.product_id;
    -- If status changes from 'received' to something else
    ELSEIF NEW.status != 'received' AND OLD.status = 'received' THEN
        UPDATE Products
        SET current_stock = current_stock - OLD.stock
        WHERE id = NEW.product_id;
    -- If status remains 'received' but stock or cost_price changes
    ELSEIF NEW.status = 'received' AND OLD.status = 'received' THEN
        UPDATE Products
        SET current_stock = current_stock - OLD.stock + NEW.stock,
            default_price = NEW.cost_price
        WHERE id = NEW.product_id;
    END IF;
END */$$


DELIMITER ;

/* Trigger structure for table `purchases` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `update_product_stock_and_price_after_purchase_delete` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `update_product_stock_and_price_after_purchase_delete` AFTER DELETE ON `purchases` FOR EACH ROW BEGIN
    IF OLD.status = 'received' THEN
        -- Reduce the stock in Products table
        UPDATE Products
        SET current_stock = current_stock - OLD.stock
        WHERE id = OLD.product_id;
    END IF;
END */$$


DELIMITER ;

/* Trigger structure for table `sells` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `before_sell_insert` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `before_sell_insert` BEFORE INSERT ON `sells` FOR EACH ROW BEGIN
    DECLARE product_price DECIMAL(10,2);
    SELECT selling_price INTO product_price 
    FROM Products 
    WHERE id = NEW.product_id;
    
    IF product_price IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Product price not found';
    END IF;
    
    SET NEW.price = product_price;
    SET NEW.total_price = product_price * NEW.sale_stock;
END */$$


DELIMITER ;

/* Trigger structure for table `sells` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `after_sell_insert` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `after_sell_insert` AFTER INSERT ON `sells` FOR EACH ROW BEGIN
    -- Subtract sale_stock from current_stock in Products
    UPDATE Products
    SET current_stock = current_stock - NEW.sale_stock
    WHERE id = NEW.product_id;
END */$$


DELIMITER ;

/* Trigger structure for table `sells` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `before_sell_update` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `before_sell_update` BEFORE UPDATE ON `sells` FOR EACH ROW BEGIN
    DECLARE product_price DECIMAL(10,2);
    SELECT selling_price INTO product_price 
    FROM Products 
    WHERE id = NEW.product_id;
    
    IF product_price IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Product price not found';
    END IF;
    
    SET NEW.price = product_price;
    SET NEW.total_price = product_price * NEW.sale_stock;
END */$$


DELIMITER ;

/* Trigger structure for table `sells` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `after_sell_update` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `after_sell_update` AFTER UPDATE ON `sells` FOR EACH ROW BEGIN
    UPDATE Products
    SET current_stock = current_stock + OLD.sale_stock - NEW.sale_stock
    WHERE id = NEW.product_id;
END */$$


DELIMITER ;

/* Trigger structure for table `sells` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `after_sell_delete` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `after_sell_delete` AFTER DELETE ON `sells` FOR EACH ROW BEGIN
    UPDATE Products
    SET current_stock = current_stock + OLD.sale_stock
    WHERE id = OLD.product_id;
END */$$


DELIMITER ;

/*Table structure for table `adjuststockview` */

DROP TABLE IF EXISTS `adjuststockview`;

/*!50001 DROP VIEW IF EXISTS `adjuststockview` */;
/*!50001 DROP TABLE IF EXISTS `adjuststockview` */;

/*!50001 CREATE TABLE  `adjuststockview`(
 `adjustment_id` int(11) ,
 `product_id` int(11) ,
 `product_name` varchar(255) ,
 `product_sku` int(11) ,
 `adjustment_type` varchar(255) ,
 `quantity_adjusted` int(11) ,
 `unit_price` decimal(10,2) ,
 `total_value` decimal(10,2) ,
 `adjustment_date` date ,
 `description` text ,
 `product_current_stock` int(11) 
)*/;

/*Table structure for table `productstockview` */

DROP TABLE IF EXISTS `productstockview`;

/*!50001 DROP VIEW IF EXISTS `productstockview` */;
/*!50001 DROP TABLE IF EXISTS `productstockview` */;

/*!50001 CREATE TABLE  `productstockview`(
 `product_id` int(11) ,
 `product_name` varchar(255) ,
 `sku` int(11) ,
 `current_stock` int(11) ,
 `opening_stock_id` int(11) ,
 `opening_stock` int(11) ,
 `stock_date` date ,
 `stock_description` text 
)*/;

/*Table structure for table `productview` */

DROP TABLE IF EXISTS `productview`;

/*!50001 DROP VIEW IF EXISTS `productview` */;
/*!50001 DROP TABLE IF EXISTS `productview` */;

/*!50001 CREATE TABLE  `productview`(
 `product_id` int(11) ,
 `product_name` varchar(255) ,
 `sku` int(11) ,
 `unit_id` int(11) ,
 `unit_name` varchar(255) ,
 `unit_sub_name` varchar(255) ,
 `unit_description` varchar(255) ,
 `brand_id` int(11) ,
 `brand_name` varchar(255) ,
 `brand_description` text ,
 `category_id` int(11) ,
 `category_name` varchar(255) ,
 `category_code` int(11) ,
 `category_description` text ,
 `default_price` int(11) ,
 `selling_price` int(11) ,
 `imageUrl` varchar(100) ,
 `current_stock` int(11) ,
 `product_description` text 
)*/;

/*Table structure for table `purchase_details` */

DROP TABLE IF EXISTS `purchase_details`;

/*!50001 DROP VIEW IF EXISTS `purchase_details` */;
/*!50001 DROP TABLE IF EXISTS `purchase_details` */;

/*!50001 CREATE TABLE  `purchase_details`(
 `purchase_id` int(11) ,
 `suppliers_id` int(11) ,
 `supplier_name` varchar(255) ,
 `product_id` int(11) ,
 `product_name` varchar(255) ,
 `purchase_date` date ,
 `purchase_status` varchar(20) ,
 `purchased_stock` int(11) ,
 `purchase_cost_price` int(11) ,
 `purchase_description` text 
)*/;

/*Table structure for table `purchase_write_view` */

DROP TABLE IF EXISTS `purchase_write_view`;

/*!50001 DROP VIEW IF EXISTS `purchase_write_view` */;
/*!50001 DROP TABLE IF EXISTS `purchase_write_view` */;

/*!50001 CREATE TABLE  `purchase_write_view`(
 `purchase_id` int(11) ,
 `suppliers_id` int(11) ,
 `product_id` int(11) ,
 `purchase_date` date ,
 `purchase_status` varchar(20) ,
 `purchased_stock` int(11) ,
 `purchase_cost_price` int(11) ,
 `purchase_description` text 
)*/;

/*Table structure for table `sellview` */

DROP TABLE IF EXISTS `sellview`;

/*!50001 DROP VIEW IF EXISTS `sellview` */;
/*!50001 DROP TABLE IF EXISTS `sellview` */;

/*!50001 CREATE TABLE  `sellview`(
 `sale_id` int(11) ,
 `sale_date` date ,
 `product_id` int(11) ,
 `product_name` varchar(255) ,
 `customer_id` int(11) ,
 `customer_name` varchar(255) ,
 `quantity_sold` int(11) ,
 `unit_price` decimal(10,2) ,
 `total_sale_amount` decimal(10,2) ,
 `sale_description` text 
)*/;

/*Table structure for table `sell_summary_view` */

DROP TABLE IF EXISTS `sell_summary_view`;

/*!50001 DROP VIEW IF EXISTS `sell_summary_view` */;
/*!50001 DROP TABLE IF EXISTS `sell_summary_view` */;

/*!50001 CREATE TABLE  `sell_summary_view`(
 `date` date ,
 `product_name` varchar(255) ,
 `total_quantity_sold` decimal(32,0) ,
 `total_revenue` decimal(32,2) 
)*/;

/*View structure for view adjuststockview */

/*!50001 DROP TABLE IF EXISTS `adjuststockview` */;
/*!50001 DROP VIEW IF EXISTS `adjuststockview` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `adjuststockview` AS select `a`.`id` AS `adjustment_id`,`a`.`product_id` AS `product_id`,`p`.`name` AS `product_name`,`p`.`sku` AS `product_sku`,`a`.`option` AS `adjustment_type`,`a`.`adjust_stock` AS `quantity_adjusted`,`a`.`price` AS `unit_price`,`a`.`total_price` AS `total_value`,`a`.`date` AS `adjustment_date`,`a`.`description` AS `description`,`p`.`current_stock` AS `product_current_stock` from (`adjuststocks` `a` join `products` `p` on(`a`.`product_id` = `p`.`id`)) */;

/*View structure for view productstockview */

/*!50001 DROP TABLE IF EXISTS `productstockview` */;
/*!50001 DROP VIEW IF EXISTS `productstockview` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `productstockview` AS select `p`.`id` AS `product_id`,`p`.`name` AS `product_name`,`p`.`sku` AS `sku`,`p`.`current_stock` AS `current_stock`,`os`.`id` AS `opening_stock_id`,`os`.`stock` AS `opening_stock`,`os`.`date` AS `stock_date`,`os`.`description` AS `stock_description` from (`products` `p` left join `openingstock` `os` on(`p`.`id` = `os`.`product_id`)) */;

/*View structure for view productview */

/*!50001 DROP TABLE IF EXISTS `productview` */;
/*!50001 DROP VIEW IF EXISTS `productview` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `productview` AS select `p`.`id` AS `product_id`,`p`.`name` AS `product_name`,`p`.`sku` AS `sku`,`p`.`unit_id` AS `unit_id`,`u`.`name` AS `unit_name`,`u`.`sub_name` AS `unit_sub_name`,`u`.`description` AS `unit_description`,`p`.`brand_id` AS `brand_id`,`b`.`name` AS `brand_name`,`b`.`description` AS `brand_description`,`p`.`category_id` AS `category_id`,`c`.`name` AS `category_name`,`c`.`code` AS `category_code`,`c`.`description` AS `category_description`,`p`.`default_price` AS `default_price`,`p`.`selling_price` AS `selling_price`,`p`.`imageUrl` AS `imageUrl`,`p`.`current_stock` AS `current_stock`,`p`.`descriptions` AS `product_description` from (((`products` `p` left join `units` `u` on(`p`.`unit_id` = `u`.`id`)) left join `brands` `b` on(`p`.`brand_id` = `b`.`id`)) left join `categories` `c` on(`p`.`category_id` = `c`.`id`)) */;

/*View structure for view purchase_details */

/*!50001 DROP TABLE IF EXISTS `purchase_details` */;
/*!50001 DROP VIEW IF EXISTS `purchase_details` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `purchase_details` AS select `p`.`id` AS `purchase_id`,`p`.`suppliers_id` AS `suppliers_id`,`s`.`name` AS `supplier_name`,`p`.`product_id` AS `product_id`,`pr`.`name` AS `product_name`,`p`.`date` AS `purchase_date`,`p`.`status` AS `purchase_status`,`p`.`stock` AS `purchased_stock`,`p`.`cost_price` AS `purchase_cost_price`,`p`.`description` AS `purchase_description` from ((`purchases` `p` join `suppliers` `s` on(`p`.`suppliers_id` = `s`.`id`)) join `products` `pr` on(`p`.`product_id` = `pr`.`id`)) */;

/*View structure for view purchase_write_view */

/*!50001 DROP TABLE IF EXISTS `purchase_write_view` */;
/*!50001 DROP VIEW IF EXISTS `purchase_write_view` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `purchase_write_view` AS select `purchases`.`id` AS `purchase_id`,`purchases`.`suppliers_id` AS `suppliers_id`,`purchases`.`product_id` AS `product_id`,`purchases`.`date` AS `purchase_date`,`purchases`.`status` AS `purchase_status`,`purchases`.`stock` AS `purchased_stock`,`purchases`.`cost_price` AS `purchase_cost_price`,`purchases`.`description` AS `purchase_description` from `purchases` */;

/*View structure for view sellview */

/*!50001 DROP TABLE IF EXISTS `sellview` */;
/*!50001 DROP VIEW IF EXISTS `sellview` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sellview` AS select `s`.`id` AS `sale_id`,`s`.`date` AS `sale_date`,`p`.`id` AS `product_id`,`p`.`name` AS `product_name`,`c`.`id` AS `customer_id`,`c`.`name` AS `customer_name`,`s`.`sale_stock` AS `quantity_sold`,`s`.`price` AS `unit_price`,`s`.`total_price` AS `total_sale_amount`,`s`.`description` AS `sale_description` from ((`sells` `s` join `products` `p` on(`s`.`product_id` = `p`.`id`)) join `customers` `c` on(`s`.`customer_id` = `c`.`id`)) */;

/*View structure for view sell_summary_view */

/*!50001 DROP TABLE IF EXISTS `sell_summary_view` */;
/*!50001 DROP VIEW IF EXISTS `sell_summary_view` */;

/*!50001 CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `sell_summary_view` AS select `s`.`date` AS `date`,`p`.`name` AS `product_name`,sum(`s`.`sale_stock`) AS `total_quantity_sold`,sum(`s`.`total_price`) AS `total_revenue` from (`sells` `s` join `products` `p` on(`s`.`product_id` = `p`.`id`)) group by `s`.`date`,`p`.`name` order by `s`.`date` desc */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

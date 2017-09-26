/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.1.73 : Database - smt_eps
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`smt_eps` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `smt_eps`;

/*Table structure for table `operation` */

DROP TABLE IF EXISTS `operation`;

CREATE TABLE `operation` (
  `id` bigint(32) unsigned NOT NULL AUTO_INCREMENT,
  `operator` varchar(32) NOT NULL COMMENT '操作者',
  `time` datetime NOT NULL COMMENT '操作时间',
  `type` int(11) NOT NULL COMMENT '0:上料 1:换料 2:检查 3:全料检查',
  `result` varchar(32) NOT NULL COMMENT '操作结果',
  `lineseat` varchar(32) NOT NULL COMMENT '操作站位',
  `material_no` varchar(32) NOT NULL COMMENT '操作料号',
  `old_material_no` varchar(32) DEFAULT NULL COMMENT '旧的操作料号',
  `scanlineseat` varchar(32) NOT NULL COMMENT '扫描的站位',
  `remark` varchar(32) NOT NULL COMMENT '操作失败原因或是其它备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=629 DEFAULT CHARSET=utf8;

/*Table structure for table `program` */

DROP TABLE IF EXISTS `program`;

CREATE TABLE `program` (
  `id` varchar(32) NOT NULL,
  `file_name` varchar(128) NOT NULL,
  `client` varchar(32) DEFAULT NULL COMMENT '产品客户',
  `machine_name` varchar(32) DEFAULT NULL COMMENT '机器名',
  `version` varchar(32) DEFAULT NULL COMMENT '版本',
  `machine_config` varchar(32) DEFAULT NULL COMMENT '机器配置',
  `program_no` varchar(32) DEFAULT NULL COMMENT '程序编号',
  `line` varchar(32) DEFAULT NULL COMMENT '线别',
  `effective_date` varchar(32) DEFAULT NULL COMMENT '生效日期',
  `PCB_no` varchar(32) DEFAULT NULL COMMENT 'PCB编号',
  `BOM` varchar(128) DEFAULT NULL COMMENT 'BOM文件',
  `program_name` varchar(128) DEFAULT NULL COMMENT '程序名',
  `auditor` varchar(32) DEFAULT NULL COMMENT '审核者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `program_item` */

DROP TABLE IF EXISTS `program_item`;

CREATE TABLE `program_item` (
  `program_id` varchar(32) NOT NULL COMMENT '排位表的ID',
  `lineseat` varchar(32) NOT NULL COMMENT '站位',
  `alternative` bit(1) NOT NULL COMMENT '是否属于替补料',
  `material_no` varchar(32) NOT NULL COMMENT '料号',
  `specitification` varchar(128) DEFAULT NULL COMMENT '规格',
  `position` varbinary(128) DEFAULT NULL COMMENT '单板位置',
  `quantity` int(11) DEFAULT NULL COMMENT '数量',
  PRIMARY KEY (`program_id`,`lineseat`,`alternative`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

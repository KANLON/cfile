/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.1.33-community : Database - cfile
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cfile` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `cfile`;

/*Table structure for table `task` */

DROP TABLE IF EXISTS `task`;

CREATE TABLE `task` (
  `tid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `uid` int(11) NOT NULL COMMENT '对应的用户id',
  `task_name` varchar(30) NOT NULL COMMENT '任务名',
  `dendline` datetime NOT NULL DEFAULT '2099-01-01 00:00:00' COMMENT '提交截止时间',
  `file_type` varchar(10) NOT NULL DEFAULT 'all' COMMENT '限定提交的文件类型',
  `submit_num` int(11) NOT NULL DEFAULT '10000' COMMENT '限定提交人数',
  `submiting_num` int(11) NOT NULL DEFAULT '0' COMMENT '已经提交的人数',
  `remark` varchar(500) NOT NULL DEFAULT '' COMMENT '备注',
  `authentication` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否经过验证',
  `submiting_list` mediumtext COMMENT '提交了名单',
  `ctime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `dr` int(1) NOT NULL DEFAULT '0' COMMENT '是否有效,标记删除',
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='任务表';

/*Data for the table `task` */

insert  into `task`(`tid`,`uid`,`task_name`,`dendline`,`file_type`,`submit_num`,`submiting_num`,`remark`,`authentication`,`submiting_list`,`ctime`,`mtime`,`dr`) values (1,1,'测试任务','2099-01-01 00:00:00','all',10000,4,'',1,NULL,'2018-12-01 00:00:00','2018-12-01 14:41:48',0),(5,1,'postmant测试1','1970-01-01 08:00:01','all',10000,0,'',0,NULL,'2018-12-02 11:15:23','2018-12-02 11:15:23',0),(6,1,'postman测试1','2018-12-03 12:00:00','all',43,0,'post测试1的备注',0,NULL,'2018-12-03 18:26:03','2018-12-03 18:26:03',0),(7,1,'postman测试任务2','2018-12-06 12:00:00','all',43,0,'post测试1的备注',0,NULL,'2018-12-03 18:31:58','2018-12-03 18:31:58',0),(8,1,'测试任务4','2099-01-01 00:00:00','all',10000,0,'',1,NULL,'2018-12-08 00:15:47','2018-12-08 00:15:47',0),(9,1,'正式123','2019-01-01 00:00:00','image',43,0,'正式1的描述，加长描述，加长描述',0,NULL,'2018-12-08 16:30:29','2018-12-09 07:38:49',0),(10,1,'正式测试1','2099-01-01 00:00:00','image',100,0,'在创建/修改页面创建的一个任务',0,NULL,'2018-12-08 16:35:03','2018-12-08 16:35:03',0),(11,1,'测试1','2099-01-01 00:00:00','word',12,0,'',0,NULL,'2018-12-08 16:40:23','2018-12-08 16:40:23',0),(12,1,'1','2099-01-01 00:00:00','excel',100,0,'备注信息',0,NULL,'2018-12-08 16:50:48','2018-12-08 16:50:48',0),(13,1,'2','2018-12-06 10:30:00','powerpoint',13,0,'备注2',0,NULL,'2018-12-08 16:51:31','2018-12-08 16:51:31',0),(14,1,'撒飞机ask就沙拉','2018-11-21 18:30:00','all',100,0,'',0,NULL,'2018-12-08 16:55:20','2018-12-09 07:43:42',0),(15,1,'撒打算撒撒发','2099-01-01 00:00:00','all',10000,0,'',0,NULL,'2018-12-08 16:59:53','2018-12-09 07:43:04',0),(16,1,'第三方打','2099-01-01 00:00:00','all',10000,0,'',0,NULL,'2018-12-08 17:00:04','2018-12-09 07:42:51',0),(17,1,'3','2018-12-06 12:00:00','all',43,0,'post测试1的备注',0,NULL,'2018-12-08 17:00:12','2018-12-08 19:57:44',0),(18,1,'正式测试2','2018-12-13 14:30:00','excel',23,0,'测试任务2',0,NULL,'2018-12-08 23:39:21','2018-12-08 23:39:21',0),(19,1,'12','2099-01-01 00:00:00','all',10000,0,'',0,NULL,'2018-12-09 07:13:57','2018-12-09 07:13:57',0),(20,1,'33','2099-01-01 00:00:00','all',10000,0,'',0,NULL,'2018-12-09 07:37:19','2018-12-09 07:37:19',0),(21,1,'设置任务名','2099-01-01 00:00:00','all',10000,0,'',0,NULL,'2018-12-09 07:39:10','2018-12-09 07:39:10',0),(22,1,'ABCSDAKFJ','2099-01-01 00:00:00','all',10000,0,'',0,NULL,'2018-12-09 10:30:12','2018-12-09 10:30:12',0),(23,1,'ablcad','2099-01-01 00:00:00','all',100,0,'',0,NULL,'2018-12-09 10:31:58','2018-12-09 10:37:06',0),(24,1,'张三三的任务','2099-01-01 00:00:00','all',10000,0,'',0,NULL,'2018-12-09 10:37:22','2018-12-09 10:37:22',0);

/*Table structure for table `teacher` */

DROP TABLE IF EXISTS `teacher`;

CREATE TABLE `teacher` (
  `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `PASSWORD` char(32) NOT NULL COMMENT '密码',
  `salt` char(24) NOT NULL COMMENT '用于密码md5加密的盐',
  `nickname` varchar(20) DEFAULT NULL COMMENT '昵称',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `ctime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  `mtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `dr` int(1) NOT NULL DEFAULT '0' COMMENT '是否有效,标记删除',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='教师用户表';

/*Data for the table `teacher` */

insert  into `teacher`(`uid`,`username`,`PASSWORD`,`salt`,`nickname`,`email`,`ctime`,`mtime`,`dr`) values (1,'admin','ea48576f30be1669971699c09ad05c94','123456','默认用户','s19961234@126.com','2018-11-28 23:14:00','2018-11-30 19:46:18',0),(2,'李四四','9fc3683273b7a0642be7baae4153d610','FttCs2U3/oCL9j8+MpKMNg==',NULL,'','2018-12-02 10:46:55','2018-12-02 10:46:55',0),(3,'1','56f2600c75137e0146581a9dca87b2f1','AWq9EJC4BzzqMzbIIJ7z5g==',NULL,'','2018-12-05 20:27:34','2018-12-05 20:27:34',0),(4,'2','bd09217ea5a101a7cfb54d8af616dbbb','15B8jkSJWjfZZeC6/a8dWA==',NULL,'','2018-12-05 20:28:08','2018-12-05 20:28:08',0);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

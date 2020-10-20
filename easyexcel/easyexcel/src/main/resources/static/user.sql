/*
Navicat MySQL Data Transfer

Source Server         : CentOS7_Local
Source Server Version : 50728
Source Host           : 192.168.0.100:3306
Source Database       : easyexcel

Target Server Type    : MYSQL
Target Server Version : 50728
File Encoding         : 65001

Date: 2020-01-14 23:06:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `USERNAME` varchar(30) DEFAULT NULL COMMENT '用户名',
  `NICKNAME` varchar(30) DEFAULT NULL COMMENT '别名',
  `PASSWORD` varchar(50) DEFAULT NULL COMMENT '密码',
  `IDENTITYNUM` varchar(18) DEFAULT NULL COMMENT '身份证号码',
  `SEX` int(1) DEFAULT NULL COMMENT '性别',
  `AGE` int(3) DEFAULT NULL COMMENT '年龄',
  `BIRTHDAY` date DEFAULT NULL COMMENT '生日',
  `HEIGHT` double(5,2) DEFAULT NULL COMMENT '身高',
  `WEIGHT` double(5,2) DEFAULT NULL COMMENT '体重',
  `TELEPHONE` varchar(11) DEFAULT NULL COMMENT '电话号码',
  `EMAIL` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `ADDRESS` varchar(150) DEFAULT NULL COMMENT '住址',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2000005 DEFAULT CHARSET=utf8;

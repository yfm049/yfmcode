/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50527
Source Host           : localhost:3306
Source Database       : wykfc

Target Server Type    : MYSQL
Target Server Version : 50527
File Encoding         : 65001

Date: 2014-08-10 00:03:10
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `pass` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', '111', '111');

-- ----------------------------
-- Table structure for `devices`
-- ----------------------------
DROP TABLE IF EXISTS `devices`;
CREATE TABLE `devices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) DEFAULT NULL,
  `clientname` varchar(100) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `deviceimei` varchar(100) DEFAULT NULL,
  `clientid` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `pass` varchar(100) DEFAULT NULL,
  `endtime` datetime DEFAULT NULL,
  `state` int(11) DEFAULT '1',
  `sms` varchar(255) DEFAULT 'true',
  `call` varchar(255) DEFAULT 'true',
  `loc` varchar(255) DEFAULT 'true',
  `callrecord` varchar(255) DEFAULT 'call',
  `count` int(11) DEFAULT '10',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  CONSTRAINT `devices_ibfk_1` FOREIGN KEY (`userid`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of devices
-- ----------------------------
INSERT INTO `devices` VALUES ('1', '1', '明明', '13473405826', '868033012962439', 'a74118a243d134f5c99c8feffd941a81', 'yfm049@163.com', '1357', 'ming861004$&', '2015-01-31 00:00:00', '1', 'true', 'true', 'true', 'call', '10');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `serverid` varchar(100) DEFAULT NULL,
  `pass` varchar(100) DEFAULT NULL,
  `imei` varchar(100) DEFAULT NULL,
  `phonenum` varchar(100) DEFAULT NULL,
  `state` int(11) DEFAULT '1',
  `endtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'yfm049', null, '123456', '', '13473405826', '1', '2014-10-31 00:00:00');

/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50096
Source Host           : localhost:3306
Source Database       : smsservernew

Target Server Type    : MYSQL
Target Server Version : 50096
File Encoding         : 65001

Date: 2014-08-21 17:20:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `pass` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', '123456', '123456');
INSERT INTO `admin` VALUES ('2', 'admin', 'admin');

-- ----------------------------
-- Table structure for devices
-- ----------------------------
DROP TABLE IF EXISTS `devices`;
CREATE TABLE `devices` (
  `id` int(11) NOT NULL auto_increment,
  `clientname` varchar(255) default NULL,
  `userid` int(11) default NULL,
  `clientid` varchar(255) default NULL,
  `deviceimei` varchar(255) default NULL,
  `phone` varchar(255) default NULL,
  `code` varchar(255) default NULL,
  `endtime` date default NULL,
  `state` int(255) default NULL COMMENT '1可用 0 不可用',
  `sms` varchar(255) default 'true',
  `call` varchar(255) default 'true',
  `loc` varchar(255) default 'true',
  `count` int(11) default '10',
  `callrecord` varchar(255) default 'call',
  `email` varchar(255) default NULL,
  `pass` varchar(255) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of devices
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(255) default NULL,
  `serverid` varchar(255) default NULL,
  `pass` varchar(255) default NULL,
  `phonenum` varchar(255) default NULL,
  `imei` varchar(255) default NULL,
  `endtime` date default NULL,
  `state` int(11) default NULL COMMENT '1 可用 0不可用',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '123', null, '123', null, null, '2014-07-02', '1');

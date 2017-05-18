/*
 Navicat Premium Data Transfer

 Source Server         : 逍邦网络局域网数据库
 Source Server Type    : MySQL
 Source Server Version : 50173
 Source Host           : 192.168.10.100
 Source Database       : db_xbb_dingtalk

 Target Server Type    : MySQL
 Target Server Version : 50173
 File Encoding         : utf-8

 Date: 05/16/2017 16:14:01 PM
*/
SET NAMES utf8;


SET FOREIGN_KEY_CHECKS = 0;

-- 创建 接口文档表
-- author：huajun.zhang
-- date: 2017-05-16
-- ----------------------------
--  Table structure for `tb_api_doc`
-- ----------------------------
DROP TABLE
IF EXISTS `tb_api_doc`;

CREATE TABLE `tb_api_doc`(
  `id` INT(10) NOT NULL AUTO_INCREMENT COMMENT 'Api Doc 主键' ,
  `version` VARCHAR(10) DEFAULT NULL COMMENT 'Api Doc 版本号' ,
  `module` VARCHAR(255) DEFAULT NULL COMMENT 'Api Doc 模块名' ,
  `name` VARCHAR(255) DEFAULT NULL COMMENT 'Api Doc 接口名' ,
  `url` VARCHAR(255) DEFAULT NULL COMMENT 'Api Doc 请求地址，不包含域名部分' ,
  `username` VARCHAR(255) DEFAULT NULL COMMENT 'Api Doc 创建者名称' ,
  `method` VARCHAR(10) DEFAULT 'POST' COMMENT 'Api Doc 请求方式：POST或GET' ,
  `memo` text COMMENT 'Api Doc 说明' ,
  `params` text COMMENT 'Api Doc 请求参数 {"key": "参数 key", "name": "参数中文", "type": "参数类型（String、int、float、double、boolean）", "limie": "参数长度上限（0：无上限）", "required": "是否必填（boolean）", "memo": "说明"}' ,
  `params_demo` text COMMENT 'Api Doc 请求参数 Demo' ,
  `response` text COMMENT 'Api Doc 返回 Response 的 body 主要内容 {"key": "参数 key", "name": "参数中文", "type": "参数类型（String、int、float、double、boolean）", "memo": "说明"}' ,
  `response_demo` text COMMENT 'Api Doc 返回 Response 的 body 的 demo' ,
  `wrong_code` text COMMENT '错误代码： {"code": "错误代码 Code", "msg": "错误说明"}' ,
  `add_time` INT(10) DEFAULT NULL COMMENT '创建时间（Unix_timestamp）' ,
  `update_time` INT(10) DEFAULT NULL COMMENT '更新时间（Unix_timestamp）' ,
  `del` INT(1) DEFAULT '0' COMMENT '是否删除' ,
  PRIMARY KEY(`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8 COMMENT = 'Api 测试平台保存的接口文档';


SET FOREIGN_KEY_CHECKS = 1;

-- 插入Api文档的相关Api
-- huajun.zhang 2017.05.17

INSERT INTO `db_xbb_dingtalk`.`tb_api_doc`(
  `version` ,
  `module` ,
  `name` ,
  `url` ,
  `username` ,
  `method` ,
  `memo` ,
  `params` ,
  `params_demo` ,
  `response` ,
  `response_demo` ,
  `wrong_code` ,
  `add_time` ,
  `update_time` ,
  `del`
)
VALUES
  (
    '3.7' ,
    'Api文档' ,
    'Api列表' ,
    '/api/doc/list.do' ,
    'huajun.zhang' ,
    'POST' ,
    '按页索引 Api 列表' ,
    '[{"key":"page","name":"页数","type":"Integer","limit":"0","required":true,"memo":"当前页数"},{"key":"pageSize","name":"每页数量","type":"Integer","limit":0,"required":true,"memo":"默认20"}]' ,
    '{"page":1, "pageSize":20}' ,
    '[{"key":"id","name":"Api 文档主键","type":"Integer","memo":""}]' ,
    '{"msg":"操作成功","ret":[{"name":"是的","method":"12","id":2,"responseDemo":"12","wrongCode":"1234","updateTime":412,"params":"[]","response":"[]","module":"一级","memo":"41","url":"打算","username":"二级2i","version":"3.6","del":0,"paramsDemo":"12345","addTime":12},{"name":"xx","method":"231","id":6,"responseDemo":"1232","wrongCode":"123","updateTime":123,"params":"[]","response":"[]","module":"一级","memo":"213","url":"1231","username":"231","version":"3.6","del":0,"paramsDemo":"31","addTime":1231}],"errorcode":0}' ,
    '[{"code":"","msg":""}]' ,
    '1495006877' ,
    '1495006877' ,
    '0'
  );

INSERT INTO `db_xbb_dingtalk`.`tb_api_doc`(
  `version` ,
  `module` ,
  `name` ,
  `url` ,
  `username` ,
  `method` ,
  `memo` ,
  `params` ,
  `params_demo` ,
  `response` ,
  `response_demo` ,
  `wrong_code` ,
  `add_time` ,
  `update_time` ,
  `del`
)
VALUES
  (
    '3.7' ,
    'Api文档' ,
    '新建、编辑 Api' ,
    '/api/doc/item.do' ,
    'huajun.zhang' ,
    'POST' ,
    '新建或编辑 Api 文档' ,
    '[{"key":"id","name":"主键","type":"Integer","limit":0,"required":false,"memo":"为空时，是新建操作"},{"key":"","name":"","type":"String","limit":0,"required":true,"memo":""}]' ,
    '{"params":["{"id":12,"version":"3.7","module":"Api文档","name":"新建、编辑 Api","url":"/api/doc/item.do","username":"huajun.zhang","method":"POST","memo":"新建或编辑 Api 文档","params":"[{"key":"id","name":"主键","type":"Integer","limit":0,"required":false,"memo":"为空时，是新建操作"}]","paramsDemo":"","response":"[{"key":"code","name":"数据库操作结果","type":"Integer","memo":"1：成功"},{"key":"id","name":"Api 文档主键","type":"Integer","memo":"新建成功后的主键"}]","responseDemo":"{"msg":"操作成功","ret":{"code":1,"id":12},"errorcode":0}","wrongCode":"[{"code":"","msg":""}]","del":0}"]}' ,
    '[{"key":"code","name":"数据库操作结果","type":"Integer","memo":"1：成功"},{"key":"id","name":"Api 文档主键","type":"Integer","memo":"新建成功后的主键"}]' ,
    '{"msg":"操作成功","ret":{"code":1,"id":12},"errorcode":0}' ,
    '[]' ,
    '1495007235' ,
    '1495068690' ,
    '0'
  );

-- 创建 接口版本表
-- author：huajun.zhang
-- date: 2017-05-18
--  Table structure for `tb_api_version`
-- ----------------------------

DROP TABLE
IF EXISTS `tb_api_version`;

CREATE TABLE `tb_api_version`(
  `id` INT(10) NOT NULL AUTO_INCREMENT COMMENT '版本Id' ,
  `version` VARCHAR(10) DEFAULT NULL COMMENT '版本号' ,
  `del` TINYINT(1) DEFAULT '0' COMMENT '是否删除' ,
  PRIMARY KEY(`id`)
) ENGINE = INNODB AUTO_INCREMENT = 7 DEFAULT CHARSET = utf8;

-- ----------------------------
--  Records of `tb_api_version`
-- ----------------------------

BEGIN
;

INSERT INTO `tb_api_version`
VALUES
  ('1' , '3.2' , '0') ,
  ('2' , '3.3' , '0') ,
  ('3' , '3.4' , '0') ,
  ('4' , '3.5' , '0') ,
  ('5' , '3.6' , '0') ,
  ('6' , '3.7' , '0');

COMMIT;


SET FOREIGN_KEY_CHECKS = 1;

-- 创建 接口模块表
-- author：huajun.zhang
-- date: 2017-05-16
-- ----------------------------
--  Table structure for `tb_api_module`
-- ----------------------------
DROP TABLE
IF EXISTS `tb_api_module`;

CREATE TABLE `tb_api_module`(
  `id` INT(10) NOT NULL COMMENT '模块主键' ,
  `module` VARCHAR(50) DEFAULT NULL COMMENT '模块名称' ,
  `del` TINYINT(1) DEFAULT '0' COMMENT '是否删除' ,
  PRIMARY KEY(`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8;

-- ----------------------------
--  Records of `tb_api_module`
-- ----------------------------

BEGIN
;

INSERT INTO `tb_api_module`
VALUES
  ('1' , '客户模块' , '0') ,
  ('2' , '销售机会模块' , '0') ,
  ('3' , '合同模块' , '0') ,
  ('4' , '访客计划模块' , '0') ,
  ('5' , '跟进记录模块' , '0') ,
  ('6' , '消息模块' , '0') ,
  ('7' , 'SFA模块' , '0') ,
  ('8' , 'BI智能报表模块' , '0') ,
  ('9' , '审批模块' , '0') ,
  ('10' , '发票模块' , '0') ,
  ('11' , '产品模块' , '0') ,
  ('12' , '用户模块' , '0') ,
  ('13' , '公司模块' , '0') ,
  ('14' , '短信模块' , '0');

COMMIT;


SET FOREIGN_KEY_CHECKS = 1;
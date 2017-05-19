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

-- 创建 接口用例表
-- author：huajun.zhang
-- date: 2017-05-19
-- ----------------------------
--  Table structure for `tb_api_case`
-- ----------------------------
DROP TABLE
IF EXISTS `tb_api_case`;

CREATE TABLE `tb_api_case`(
  `id` INT(10) NOT NULL AUTO_INCREMENT COMMENT 'api用例id' ,
  `case_name` VARCHAR(50) NOT NULL COMMENT 'api用例名称' ,
  `api_id` INT(10) NOT NULL COMMENT 'api关联表 id' ,
  `request_parameters` text COMMENT 'api请求参数' ,
  `expected_state_code` INT(10) DEFAULT NULL COMMENT 'api预期返回 code' ,
  `actual_state_code` INT(10) DEFAULT NULL COMMENT 'api实际返回 code' ,
  `expected_content` text COMMENT 'api预期返回文本' ,
  `actual_content` text COMMENT 'api实际返回文本' ,
  `content_judge_logic` VARCHAR(10)  DEFAULT NULL COMMENT 'api比较方式：equal或contains' ,
  `duration_time` INT(10) DEFAULT NULL COMMENT '请求耗时ms' ,
  `test_result` VARCHAR(10) DEFAULT NULL COMMENT 'api验证结果：Pass或Fail' ,
  `update_time` INT(10) DEFAULT NULL COMMENT '更新时间' ,
  `add_time` INT(10) DEFAULT NULL COMMENT '创建时间' ,
  `del` TINYINT(1) DEFAULT '0' COMMENT '是否删除' ,
  PRIMARY KEY(`id`)
) ENGINE = INNODB AUTO_INCREMENT = 9 DEFAULT CHARSET = utf8;

-- ----------------------------
--  Records of `tb_api_case`
-- ----------------------------

BEGIN
;

INSERT INTO `tb_api_case`
VALUES
  (
    '1' ,
    'get_customer_item_without_any_parameters' ,
    '6' ,
    '{\"corpid\":\"\",\"nowUserId\":\"\"}' ,
    '200' ,
    '200' ,
    '{\"code\":19,\"msg\":\"登录参数缺失！\"}' ,
    '{\"code\":19,\"msg\":\"登录参数缺失！\"}' ,
    ' contains' ,
    '1' ,
    'Fail' ,
    '1494845023' ,
    '1494845023' ,
    '0'
  ) ,
  (
    '2' ,
    'get_customer_item_without_corpid' ,
    '6' ,
    '{\"corpid\":\"\",\"nowUserId\":\"1\"}' ,
    '200' ,
    '200' ,
    '{\"code\":19,\"msg\":\"登录参数缺失！\"}' ,
    '{\"code\":19,\"msg\":\"登录参数缺失！\"}' ,
    ' contains' ,
    '1' ,
    'Fail' ,
    '1494845023' ,
    '1494845023' ,
    '0'
  ) ,
  (
    '3' ,
    'get_customer_item_without_nowUserId' ,
    '6' ,
    '{\"corpid\":\"\",\"nowUserId\":\"\"}' ,
    '200' ,
    '200' ,
    '{\"code\":19,\"msg\":\"登录参数缺失！\"}' ,
    '{\"code\":19,\"msg\":\"登录参数缺失！\"}' ,
    ' contains' ,
    '1' ,
    'Fail' ,
    '1494845023' ,
    '1494845023' ,
    '0'
  ) ,
  (
    '4' ,
    'get_customer_item_successfully' ,
    '6' ,
    '{\"corpid\":\"ding9debe4701d59e5d8\",\"nowUserId\":\"03472117635634\"}' ,
    '200' ,
    '200' ,
    '{\"isApproval\":false,\"isNameCheck\":true,\"isPhoneCheck\":true,\"checkPhoneType\":\"2\",\"result\":[{\"attr\":\"userId\",\"attrName\":\"创建人\",\"required\":0,\"isRedundant\":0,\"fieldType\":0,\"attrValue\":[{\"id\":\"03472117635634\",\"name\":\"胡顺旋\"}],\"validate\":\"array\",\"isMultiple\":0},{\"attr\":\"nameShort\",\"attrName\":\"客户简称\",\"required\":0,\"isRedundant\":0,\"fieldType\":1,\"attrValue\":\"\"},{\"attr\":\"name\",\"attrName\":\"客户名称\",\"required\":1,\"isRedundant\":0,\"fieldType\":0,\"attrValue\":\"\",\"validate\":\"string\"},{\"attr\":\"phone\",\"attrName\":\"客户电话\",\"required\":0,\"isRedundant\":0,\"fieldType\":0,\"attrValue\":[],\"validate\":\"array\"},{\"attr\":\"genre\",\"attrName\":\"客户类型\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"最终客户\",\"key\":\"1\"},{\"value\":\"类型1\",\"key\":\"4\"},{\"value\":\"渠道客户\",\"key\":\"2\"},{\"value\":\"竞争对手\",\"key\":\"3\"}],\"dictionaryCode\":13},{\"attr\":\"type\",\"attrName\":\"客户状态\",\"required\":1,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"潜在客户\",\"key\":\"1\"},{\"value\":\"初步接触1\",\"key\":\"2\"},{\"value\":\"持续跟进\",\"key\":\"3\"},{\"value\":\"成交客户\",\"key\":\"4\"},{\"value\":\"忠诚客户\",\"key\":\"5\"},{\"value\":\"无效客户\",\"key\":\"6\"}],\"dictionaryCode\":3},{\"attr\":\"isIndividual\",\"attrName\":\"客户性质\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"个人客户\",\"key\":\"2\"},{\"value\":\"企业客户\",\"key\":\"1\"},{\"value\":\"测试字段\",\"key\":\"4\"},{\"value\":\"政府事业单位\",\"key\":\"3\"}],\"dictionaryCode\":4},{\"attr\":\"scale\",\"attrName\":\"客户分级\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"小型\",\"key\":\"3\"},{\"value\":\"中型\",\"key\":\"2\"},{\"value\":\"大型\",\"key\":\"1\"}]},{\"attr\":\"industry\",\"attrName\":\"客户行业\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"新兴产业\",\"key\":\"16\"},{\"value\":\"金融\",\"key\":\"1\"},{\"value\":\"电信\",\"key\":\"2\"},{\"value\":\"教育\",\"key\":\"3\"},{\"value\":\"零售\",\"key\":\"9\"},{\"value\":\"媒体\",\"key\":\"10\"},{\"value\":\"娱乐\",\"key\":\"11\"},{\"value\":\"咨询\",\"key\":\"12\"},{\"value\":\"非营利事业\",\"key\":\"13\"},{\"value\":\"公用事业\",\"key\":\"14\"},{\"value\":\"其他\",\"key\":\"15\"}],\"dictionaryCode\":6},{\"attr\":\"attr1\",\"attrName\":\"单行输入\",\"required\":0,\"isRedundant\":1,\"fieldType\":1,\"initValue\":\"\",\"attrValue\":\"\"},{\"attr\":\"importantDegree\",\"attrName\":\"重要程度\",\"required\":0,\"isRedundant\":0,\"fieldType\":10,\"attrValue\":\"\"},{\"attr\":\"country\",\"attrName\":\"国家\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"中国\",\"key\":\"中国\"},{\"value\":\"美国\",\"key\":\"美国\"},{\"value\":\"澳大利亚\",\"key\":\"澳大利亚\"},{\"value\":\"巴西\",\"key\":\"巴西\"},{\"value\":\"英国\",\"key\":\"英国\"},{\"value\":\"加拿大\",\"key\":\"加拿大\"},{\"value\":\"中国\",\"key\":\"中国\"},{\"value\":\"埃及\",\"key\":\"埃及\"},{\"value\":\"法国\",\"key\":\"法国\"},{\"value\":\"德国\",\"key\":\"德国\"},{\"value\":\"希腊\",\"key\":\"希腊\"},{\"value\":\"印度\",\"key\":\"印度\"},{\"value\":\"爱尔兰\",\"key\":\"爱尔兰\"},{\"value\":\"以色列\",\"key\":\"以色列\"},{\"value\":\"意大利\",\"key\":\"意大利\"},{\"value\":\"日本\",\"key\":\"日本\"},{\"value\":\"荷兰\",\"key\":\"荷兰\"},{\"value\":\"新西兰\",\"key\":\"新西兰\"},{\"value\":\"葡萄牙\",\"key\":\"葡萄牙\"},{\"value\":\"俄国\",\"key\":\"俄国\"},{\"value\":\"西班牙\",\"key\":\"西班牙\"},{\"value\":\"瑞典\",\"key\":\"瑞典\"},{\"value\":\"瑞士\",\"key\":\"瑞士\"}],\"dictionaryCode\":11},{\"attr\":\"address\",\"attrName\":\"客户地址\",\"required\":0,\"isRedundant\":0,\"fieldType\":0,\"attrValue\":\"\",\"validate\":\"object\"},{\"attr\":\"source\",\"attrName\":\"客户来源\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"网络推广\",\"key\":\"网络推广\"},{\"value\":\"电话销售\",\"key\":\"电话销售\"},{\"value\":\"第四来源1\",\"key\":\"第四来源1\"},{\"value\":\"渠道代理\",\"key\":\"渠道代理\"}],\"dictionaryCode\":12},{\"attr\":\"website\",\"attrName\":\"客户官网\",\"required\":0,\"isRedundant\":0,\"fieldType\":1,\"attrValue\":\"\"},{\"attr\":\"instruction\",\"attrName\":\"客户简介\",\"required\":0,\"isRedundant\":0,\"fieldType\":7,\"attrValue\":\"\"},{\"attr\":\"attr2\",\"attrName\":\"客户生日\",\"required\":0,\"isRedundant\":1,\"fieldType\":4,\"initValue\":\"\",\"attrValue\":\"\"}],\"code\":1,\"msg\":\"操作成功\"}' ,
    '{\"isApproval\":false,\"isNameCheck\":true,\"isPhoneCheck\":true,\"checkPhoneType\":\"2\",\"result\":[{\"attr\":\"userId\",\"attrName\":\"创建人\",\"required\":0,\"isRedundant\":0,\"fieldType\":0,\"attrValue\":[{\"id\":\"03472117635634\",\"name\":\"胡顺旋\"}],\"validate\":\"array\",\"isMultiple\":0},{\"attr\":\"nameShort\",\"attrName\":\"客户简称\",\"required\":0,\"isRedundant\":0,\"fieldType\":1,\"attrValue\":\"\"},{\"attr\":\"name\",\"attrName\":\"客户名称\",\"required\":1,\"isRedundant\":0,\"fieldType\":0,\"attrValue\":\"\",\"validate\":\"string\"},{\"attr\":\"phone\",\"attrName\":\"客户电话\",\"required\":0,\"isRedundant\":0,\"fieldType\":0,\"attrValue\":[],\"validate\":\"array\"},{\"attr\":\"genre\",\"attrName\":\"客户类型\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"最终客户\",\"key\":\"1\"},{\"value\":\"类型1\",\"key\":\"4\"},{\"value\":\"渠道客户\",\"key\":\"2\"},{\"value\":\"竞争对手\",\"key\":\"3\"}],\"dictionaryCode\":13},{\"attr\":\"type\",\"attrName\":\"客户状态\",\"required\":1,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"潜在客户\",\"key\":\"1\"},{\"value\":\"初步接触1\",\"key\":\"2\"},{\"value\":\"持续跟进\",\"key\":\"3\"},{\"value\":\"成交客户\",\"key\":\"4\"},{\"value\":\"忠诚客户\",\"key\":\"5\"},{\"value\":\"无效客户\",\"key\":\"6\"}],\"dictionaryCode\":3},{\"attr\":\"isIndividual\",\"attrName\":\"客户性质\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"个人客户\",\"key\":\"2\"},{\"value\":\"企业客户\",\"key\":\"1\"},{\"value\":\"测试字段\",\"key\":\"4\"},{\"value\":\"政府事业单位\",\"key\":\"3\"}],\"dictionaryCode\":4},{\"attr\":\"scale\",\"attrName\":\"客户分级\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"小型\",\"key\":\"3\"},{\"value\":\"中型\",\"key\":\"2\"},{\"value\":\"大型\",\"key\":\"1\"}]},{\"attr\":\"industry\",\"attrName\":\"客户行业\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"新兴产业\",\"key\":\"16\"},{\"value\":\"金融\",\"key\":\"1\"},{\"value\":\"电信\",\"key\":\"2\"},{\"value\":\"教育\",\"key\":\"3\"},{\"value\":\"零售\",\"key\":\"9\"},{\"value\":\"媒体\",\"key\":\"10\"},{\"value\":\"娱乐\",\"key\":\"11\"},{\"value\":\"咨询\",\"key\":\"12\"},{\"value\":\"非营利事业\",\"key\":\"13\"},{\"value\":\"公用事业\",\"key\":\"14\"},{\"value\":\"其他\",\"key\":\"15\"}],\"dictionaryCode\":6},{\"attr\":\"attr1\",\"attrName\":\"单行输入\",\"required\":0,\"isRedundant\":1,\"fieldType\":1,\"initValue\":\"\",\"attrValue\":\"\"},{\"attr\":\"importantDegree\",\"attrName\":\"重要程度\",\"required\":0,\"isRedundant\":0,\"fieldType\":10,\"attrValue\":\"\"},{\"attr\":\"country\",\"attrName\":\"国家\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"中国\",\"key\":\"中国\"},{\"value\":\"美国\",\"key\":\"美国\"},{\"value\":\"澳大利亚\",\"key\":\"澳大利亚\"},{\"value\":\"巴西\",\"key\":\"巴西\"},{\"value\":\"英国\",\"key\":\"英国\"},{\"value\":\"加拿大\",\"key\":\"加拿大\"},{\"value\":\"中国\",\"key\":\"中国\"},{\"value\":\"埃及\",\"key\":\"埃及\"},{\"value\":\"法国\",\"key\":\"法国\"},{\"value\":\"德国\",\"key\":\"德国\"},{\"value\":\"希腊\",\"key\":\"希腊\"},{\"value\":\"印度\",\"key\":\"印度\"},{\"value\":\"爱尔兰\",\"key\":\"爱尔兰\"},{\"value\":\"以色列\",\"key\":\"以色列\"},{\"value\":\"意大利\",\"key\":\"意大利\"},{\"value\":\"日本\",\"key\":\"日本\"},{\"value\":\"荷兰\",\"key\":\"荷兰\"},{\"value\":\"新西兰\",\"key\":\"新西兰\"},{\"value\":\"葡萄牙\",\"key\":\"葡萄牙\"},{\"value\":\"俄国\",\"key\":\"俄国\"},{\"value\":\"西班牙\",\"key\":\"西班牙\"},{\"value\":\"瑞典\",\"key\":\"瑞典\"},{\"value\":\"瑞士\",\"key\":\"瑞士\"}],\"dictionaryCode\":11},{\"attr\":\"address\",\"attrName\":\"客户地址\",\"required\":0,\"isRedundant\":0,\"fieldType\":0,\"attrValue\":\"\",\"validate\":\"object\"},{\"attr\":\"source\",\"attrName\":\"客户来源\",\"required\":0,\"isRedundant\":0,\"fieldType\":3,\"attrValue\":\"\",\"valueArrMap\":[{\"value\":\"网络推广\",\"key\":\"网络推广\"},{\"value\":\"电话销售\",\"key\":\"电话销售\"},{\"value\":\"第四来源1\",\"key\":\"第四来源1\"},{\"value\":\"渠道代理\",\"key\":\"渠道代理\"}],\"dictionaryCode\":12},{\"attr\":\"website\",\"attrName\":\"客户官网\",\"required\":0,\"isRedundant\":0,\"fieldType\":1,\"attrValue\":\"\"},{\"attr\":\"instruction\",\"attrName\":\"客户简介\",\"required\":0,\"isRedundant\":0,\"fieldType\":7,\"attrValue\":\"\"},{\"attr\":\"attr2\",\"attrName\":\"客户生日\",\"required\":0,\"isRedundant\":1,\"fieldType\":4,\"initValue\":\"\",\"attrValue\":\"\"}],\"code\":1,\"msg\":\"操作成功\"}' ,
    ' contains' ,
    '1' ,
    'Fail' ,
    '1494845023' ,
    '1494845023' ,
    '0'
  );

COMMIT;


SET FOREIGN_KEY_CHECKS = 1;
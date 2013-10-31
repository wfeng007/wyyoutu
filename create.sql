-- 
-- 

-- create database
CREATE DATABASE IF NOT EXISTS wyyoutu DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE wyyoutu;
-- 

-- 用户账号
DROP TABLE IF EXISTS `CO_PEOPLE`;
CREATE TABLE `CO_PEOPLE` (
  `seq_id` INT NOT NULL auto_increment,
  `id` VARCHAR(100) NOT NULL COMMENT '用户ID 登录用login',
  `name` VARCHAR(256) NULL COMMENT '用户名称 显示用',
  `password` VARCHAR(256) NOT NULL COMMENT '密码',
  `email` VARCHAR(100) NULL COMMENT '邮箱',
--  `desc` VARCHAR(1024) NULL COMMENT '描述',
  `add_ts` DATETIME NULL COMMENT '新增时间戳',
--  `status` DATETIME NULL COMMENT '状态',
  PRIMARY KEY (`id`),
  UNIQUE (`seq_id`) 
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='人员概要信息';

-- 默认用户
insert into `CO_PEOPLE` (`id`, `name`, `password`, `add_ts`)
	values('admin','系统管理员','21232f297a57a5a743894a0e4a801fc3','2012-12-1');

-- 人员用户信息扩展
DROP TABLE IF EXISTS `CO_PEOPLE_EXTEN`;
CREATE TABLE `CO_PEOPLE_EXTEN` (
	`seq_id` BIGINT NOT NULL  auto_increment,
	`people_id` VARCHAR(100) NOT NULL COMMENT '用户id',
	`exten_key` VARCHAR(100) NOT NULL COMMENT '扩展键',
	`exten_value` VARCHAR(2000) NULL COMMENT '扩展值',
	 PRIMARY KEY (`seq_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='人员用户信息扩展';	

-- 默认admin的角色与权限
insert into `CO_PEOPLE_EXTEN` (`people_id`, `exten_key`, `exten_value`)
	values('admin','role','Administrator');
insert into `CO_PEOPLE_EXTEN` (`people_id`, `exten_key`, `exten_value`)
	values('admin','permission','read');	
	
	
-- 图片、音频（包括文本）等 资源项
DROP TABLE IF EXISTS `RS_ITEM`;
CREATE TABLE `RS_ITEM` (
  `seq_id` INT NOT NULL auto_increment,
  `iid` VARCHAR(128) NOT NULL COMMENT '项目标识；唯一键可以使用任意算法生成。',
  `name` VARCHAR(512) NULL COMMENT '名称或抬头',
  `url` VARCHAR(512) NULL COMMENT '访问定位路径',
  `access_type` INT NULL COMMENT '访问类型',
  `add_ts` DATETIME NULL COMMENT '新增时间戳',
  `modify_ts` DATETIME NULL COMMENT '最后更新时间戳',
  `status` INT NULL COMMENT '项目状态；正常、删除、隐藏等',
  `text` VARCHAR(2000) NULL COMMENT '文本内容',
  `binary` LONGBLOB NULL  COMMENT '图片、音频、压缩包等',
  `thumbnail` MEDIUMBLOB NULL COMMENT '缩略图或其他缩略数据',
  `owner_id` VARCHAR(100) NULL COMMENT '关联用户id',
  PRIMARY KEY (`seq_id`),
--  FOREIGN KEY (`owner_id`) REFERENCES CO_PEOPLE (`id`) ON DELETE SET NULL , --有没有都没有关系 对于之后可能用mongodb应该都没有类似功能
  UNIQUE (`iid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='数据信息具体项目图片、文本、声音等';

-- for RS_ITEM default data
insert into `rs_item` (`seq_id`,`iid`, `name`, `url`, `access_type`, `add_ts`, `text`) values('1','_default_1_','测试内容1','http://www.shgtj.gov.cn/hdpt/gzcy/sj/201208/W020120830595827523916.jpg',NULL,NULL,'测试内容111111 这个内容就多了 从整体上来看 这个非常关键的发挥发的海风好大夫哈发放大方地将阿凡达发酵法大解放大房间dajfdafdafjaljfdas');
insert into `rs_item` (`seq_id`,`iid`, `name`, `url`, `access_type`, `add_ts`, `text`) values('2','_default_2_','测试内容2','./jquery/lightbox/images/next.png',NULL,NULL,'测试内容2222');

-- 资源项信息扩展
DROP TABLE IF EXISTS `RS_ITEM_EXTEN`;
CREATE TABLE `RS_ITEM_EXTEN` (
	`seq_id` BIGINT NOT NULL auto_increment,
	`item_iid` VARCHAR(128) NOT NULL COMMENT '资源项iid',
	`exten_key` VARCHAR(100) NOT NULL COMMENT '扩展键',
	`exten_value` VARCHAR(2000) NULL COMMENT '扩展值',
	 PRIMARY KEY (`seq_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='资源项信息扩展';	
-- ...
insert into `RS_ITEM_EXTEN` (`item_iid`, `exten_key`, `exten_value`)
		values('_default_1_','PUB','y');
insert into `RS_ITEM_EXTEN` (`item_iid`, `exten_key`, `exten_value`)
		values('_default_2_','PUB','y');
--	insert into `RS_ITEM_EXTEN` (`people_id`, `exten_key`, `exten_value`)
--		values('admin','permission','read');	


--  资源项评论或回复 暂时没有用。
DROP TABLE IF EXISTS `RS_COMMENT`;
CREATE TABLE `RS_COMMENT` (
  `seq_id` BIGINT(20) NOT NULL auto_increment COMMENT '自增id，对应程序应该为long类型', 
  `type` INT NULL COMMENT '类型',
  `add_ts` DATETIME NOT NULL COMMENT '新增时间戳',
  `modify_ts` DATETIME NULL COMMENT '最后更新时间戳',
  `status` INT NULL COMMENT '项目状态；正常、删除、隐藏、审核等',
  `text` VARCHAR(2000) NULL COMMENT '文本内容',
-- `target_type` '被评论到底为何物,当评论可以回复任何物件 比如可以评论tag 或 user 或者其他实体时使用',
  `target_id` VARCHAR(128) NULL COMMENT '一般为项目标识，对应到iid，注意iid为item_id首选。（被评论目标必须存在一个 128位以下的唯一键值）',
  `parent_id` BIGINT(20) NULL COMMENT '父评论',
  `owner_id` VARCHAR(512) NULL COMMENT '作者id，一般为用户登录id',
  `owner_name` VARCHAR(256) NULL COMMENT '作者名称',
--  `owner_email` VARCHAR(256) NULL COMMENT '作者email',
--  `owner_agent` VARCHAR(256) NULL COMMENT '作者浏览器类型',
--  `owner_ip` VARCHAR(128) NULL COMMENT '作者ip地址',
  PRIMARY KEY (`seq_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='资源项评论或回复';
-- for RS_ITEM default data


-- 资源项的标签 RS_TAG
-- DROP TABLE IF EXISTS `RS_TAG`;
-- CREATE TABLE `RS_TAG` (
--   `seq_id` INT NOT NULL auto_increment,
--   `tag_id` VARCHAR(128) NOT NULL COMMENT 'id，可以用显示名称代替之',
--  `name` VARCHAR(128) NULL COMMENT '显示名称',
--  `categroy_id` INT NULL COMMENT '分类',
--  `categroy_name` VARCHAR(256) NULL COMMENT '分类',
--   `descr` VARCHAR(1000) NULL COMMENT '描述',
--   PRIMARY KEY (`seq_id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='资源项评论或回复';
-- for RS_TAG default data

-- 资源项的标签同时又是与资源关系 RS_TAG2TARGET
DROP TABLE IF EXISTS `RS_TAGGED`;
CREATE TABLE `RS_TAGGED` (
  `seq_id` BIGINT(20) NOT NULL auto_increment COMMENT '自增id，对应程序应该为long类型', 
--  `ttid` VARCHAR(128) NOT NULL COMMENT '唯一标示，方便评论关联。',
  `tag_id` VARCHAR(256) NOT NULL COMMENT 'id，可以用显示名称代替之,TAG可以当做评论用么？',
-- `target_type` '被评论到底为何物,当评论可以回复任何物件 比如可以评论tag 或 user 或者其他实体时使用',
  `target_id` VARCHAR(128) NOT NULL COMMENT '一般为项目标识，对应到iid，注意iid为item_id首选。（目标必须存在一个 128位以下的唯一键值）',
--  `name` VARCHAR(128) NULL COMMENT '显示名称',
--  `categroy_id` INT NULL COMMENT '分类',
--  `categroy_name` VARCHAR(256) NULL COMMENT '分类'
--  `descr` VARCHAR(1000) NULL COMMENT '描述'
  PRIMARY KEY (`seq_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='资源项评论或回复';
-- 



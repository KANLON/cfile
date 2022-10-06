-- 创建收集文件数据库
CREATE DATABASE  /*!32312 IF NOT EXISTS*/ `cfile` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE cfile;

DROP TABLE IF EXISTS teacher ;

-- 创建教师用户表
DROP TABLE IF EXISTS teacher;
CREATE TABLE IF NOT EXISTS teacher(
  uid INT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '主键自增id',  
  username VARCHAR(20) NOT NULL COMMENT '用户名',
  PASSWORD CHAR(32) NOT NULL COMMENT '密码',
  salt CHAR(24) NOT NULL COMMENT '用于密码md5加密的盐',
  nickname VARCHAR(20) COMMENT '昵称',
  email VARCHAR(50) COMMENT '邮箱',
  -- 由于不能同时创建两个default timestamp默认值所以将创建时间的默认值修改为'0000-00-00 00:00:00'
  ctime TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  mtime TIMESTAMP NOT NULL DEFAULT NOW() COMMENT '修改时间',
  dr INT(1) NOT NULL DEFAULT 0 COMMENT '是否有效,标记删除'
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='教师用户表';

-- 插入一条默认用户数据
INSERT INTO teacher(username,PASSWORD,salt,nickname,email,ctime) VALUES('admin','ea48576f30be1669971699c09ad05c94','123456','默认用户','s19961234@126.com','2018-11-28 23:14:00');
-- 截断表
TRUNCATE TABLE teacher;
-- 查询表
SELECT * FROM teacher;

USE cfile;
-- 创建任务表
DROP TABLE IF EXISTS `task`;
CREATE TABLE IF NOT EXISTS  task(
  tid INT(11) PRIMARY KEY AUTO_INCREMENT COMMENT '主键自增id', 
  uid INT(11) NOT NULL COMMENT '对应的用户id', 
  task_name VARCHAR(30) NOT NULL COMMENT '任务名',
  dendline DATETIME DEFAULT '2099-01-01 00:00:00' NOT NULL COMMENT '提交截止时间',
  file_type VARCHAR(10) DEFAULT 'all' NOT NULL COMMENT '限定提交的文件类型',
  submit_num INT(11) DEFAULT '10000' NOT NULL COMMENT '限定提交人数',
  submiting_num INT(11) DEFAULT '0' NOT NULL COMMENT '已经提交的人数',
  remark VARCHAR(500) DEFAULT '' NOT NULL COMMENT '备注',
  authentication TINYINT(1) DEFAULT '0' NOT NULL COMMENT '是否经过验证',
  submiting_list text COMMENT '提交了名单',
  -- 由于不能同时创建两个default timestamp默认值所以将创建时间的默认值修改为'0000-00-00 00:00:00'
  ctime TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '创建时间',
  mtime TIMESTAMP NOT NULL DEFAULT NOW() COMMENT '修改时间',
  dr INT(1) NOT NULL DEFAULT 0 COMMENT '是否有效,标记删除'
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

TRUNCATE TABLE task;

-- 插入一个测试任务
INSERT INTO task(uid,task_name,ctime) VALUES(1,'测试任务','2018-12-01 00:00:00');

USE cfile;
SELECT * FROM task;

SELECT * FROM teacher;

SELECT COUNT(*) num  FROM task WHERE uid=1 AND task_name='测试任务';

-- 更新提交的人数
UPDATE task SET  submiting_num=0 WHERE tid=1;
-- 更新是否通过验证
UPDATE task SET authentication=1 WHERE tid=1;

-- 还原所有任务数据
UPDATE task SET submiting_num=0;










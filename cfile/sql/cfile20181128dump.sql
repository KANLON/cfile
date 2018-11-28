-- 创建收集文件数据库
CREATE DATABASE `cfile` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE cfile;

DROP TABLE teacher ;

-- 创建教师用户表

CREATE TABLE teacher(
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
) ENGINE=INNODB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COMMENT='教师用户表';

SELECT MD5(123456123456);
-- 插入一条默认用户数据
INSERT INTO teacher(username,PASSWORD,salt,nickname,email,ctime) VALUES('admin','ea48576f30be1669971699c09ad05c94','123456','默认用户','s19961234@126.com','2018-11-28 23:14:00')
-- 截断表
TRUNCATE TABLE teacher;
-- 查询表
SELECT * FROM teacher;












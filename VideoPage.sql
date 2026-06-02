# 使用数据库
USE VideosPage;

# 1. 用户表（user）
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` VARCHAR(32) NOT NULL COMMENT '主键，UUID自动生成',
  `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
  `email` VARCHAR(100) NOT NULL COMMENT '邮箱（登录凭证）',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `role` VARCHAR(20) DEFAULT 'user' COMMENT '用户：user / admin',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',

#   预留空字段（备用）
  `reserved1` VARCHAR(255) DEFAULT NULL COMMENT '预留字段1',
  `reserved2` VARCHAR(255) DEFAULT NULL COMMENT '预留字段2',
  `reserved3` VARCHAR(255) DEFAULT NULL COMMENT '预留字段3',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

# 2. 视频表（video）
DROP TABLE IF EXISTS `video`;
CREATE TABLE `video` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键，自动递增',
  `title` VARCHAR(200) NOT NULL COMMENT '视频标题',
  `category` VARCHAR(20) NOT NULL COMMENT '分类：动漫 / 体育 / 游戏',
  `cover_path` VARCHAR(500) DEFAULT NULL COMMENT '封面图本地路径',
  `video_path` VARCHAR(500) DEFAULT NULL COMMENT '视频文件本地路径',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '视频描述',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',

#   预留空字段（备用）
  `reserved1` VARCHAR(255) DEFAULT NULL COMMENT '预留字段1',
  `reserved2` VARCHAR(255) DEFAULT NULL COMMENT '预留字段2',
  `reserved3` VARCHAR(255) DEFAULT NULL COMMENT '预留字段3',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='视频表';

# 3. 插入一条示例视频（封面和视频路径为空，后续管理员上传）
INSERT INTO `video` (`title`, `category`, `description`) VALUES
('示例视频 - 请上传封面和视频文件', '动漫', '这是一个示例视频，请管理员在后台编辑并上传封面图和视频文件');

# 4. 插入管理员账号（可选，如需预置）
-- 注意：密码是明文 admin123
INSERT INTO `user` (`id`, `nickname`, `email`, `password`, `role`) VALUES
('admin001', '系统管理员', 'admin@example.com', 'admin123', 'admin');

# 5. 查看表结构确认
DESC `user`;
DESC `video`;

# 6. 查看已有数据
SELECT * FROM `user`;
SELECT * FROM `video`;



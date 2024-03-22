
-- ----------------------------
-- Table structure for system_dict
-- ----------------------------
DROP TABLE IF EXISTS `system_dict`;
CREATE TABLE `system_dict`
(
    `id`             bigint                                                         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `dict_type`      varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '字典类型',
    `dict_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '字典类型名称',
    `dict_label`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '字典中文名称',
    `dict_value`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '字典值',
    `dict_value2`    varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典值2',
    `dict_value3`    varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典值3',
    `dict_value4`    varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典值4',
    `dict_sort`      int                                                            NULL DEFAULT NULL COMMENT '排序',
    `state`          bit(1)                                                         NULL DEFAULT b'1' COMMENT '状态(1-有效，0-无效)',
    `create_user_id` bigint                                                         NULL DEFAULT NULL COMMENT '创建用户id',
    `create_time`    datetime                                                       NULL DEFAULT NULL COMMENT '创建时间',
    `update_user_id` bigint                                                         NULL DEFAULT NULL COMMENT '更新用户id',
    `update_time`    datetime                                                       NULL DEFAULT NULL COMMENT '更新用户时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_mix` (`dict_type` ASC, `dict_value` ASC, `dict_sort` ASC, `state` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统字典表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_menu_info
-- ----------------------------
DROP TABLE IF EXISTS `system_menu_info`;
CREATE TABLE `system_menu_info`
(
    `id`             bigint                                                         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `menu_type`      int                                                            NULL DEFAULT NULL COMMENT '菜单类型(0-菜单，1-iframe，2-外链，3-按钮)',
    `menu_title`     varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NULL DEFAULT NULL COMMENT '菜单名称',
    `menu_name`      varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL COMMENT '路由名称',
    `menu_path`      varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '路由地址',
    `parent_id`      bigint                                                         NULL DEFAULT NULL COMMENT '父id',
    `icon`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '图标',
    `extra_icon`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '右侧图标',
    `rank`           int                                                            NULL DEFAULT NULL COMMENT '排序',
    `auths`          varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '权限标识',
    `frame_src`      varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '外链地址',
    `keep_alive`     bit(1)                                                         NULL DEFAULT NULL COMMENT '缓存页面(1-缓存，0-不缓存)',
    `show_link`      bit(1)                                                         NULL DEFAULT NULL COMMENT '是否展示页面(1-展示，0-不展示)',
    `show_parent`    bit(1)                                                         NULL DEFAULT NULL COMMENT '是否展示父菜单(1-展示，0-不展示)',
    `create_user_id` bigint                                                         NULL DEFAULT NULL COMMENT '创建人id',
    `create_time`    datetime                                                       NULL DEFAULT NULL COMMENT '创建时间',
    `update_user_id` bigint                                                         NULL DEFAULT NULL COMMENT '修改人id',
    `update_time`    datetime                                                       NULL DEFAULT NULL COMMENT '修改时间',
    `state`          bit(1)                                                         NULL DEFAULT b'1' COMMENT '状态(1-有效，0-无效)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `UNI_MENU_NAME` (`menu_name` ASC) USING BTREE COMMENT '路由名称唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_menu_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `system_menu_role_rel`;
CREATE TABLE `system_menu_role_rel`
(
    `id`             bigint   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `menu_id`        bigint   NULL DEFAULT NULL COMMENT '菜单id',
    `role_id`        bigint   NULL DEFAULT NULL COMMENT '角色id',
    `create_user_id` bigint   NULL DEFAULT NULL COMMENT '创建用户id',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_user_id` bigint   NULL DEFAULT NULL COMMENT '修改用户id',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '修改时间',
    `state`          bit(1)   NULL DEFAULT b'1' COMMENT '状态(1-有效，0-无效)',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_menu_state` (`menu_id` ASC, `state` ASC) USING BTREE COMMENT '菜单id和状态联合索引',
    INDEX `index_role_state` (`role_id` ASC, `state` ASC) USING BTREE COMMENT '角色id和状态联合索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 15
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单角色关联关系表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_role_info
-- ----------------------------
DROP TABLE IF EXISTS `system_role_info`;
CREATE TABLE `system_role_info`
(
    `id`             bigint                                                       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_name`      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色名称',
    `role_code`      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色标识',
    `create_user_id` bigint                                                       NULL DEFAULT NULL COMMENT '创建用户id',
    `create_time`    datetime                                                     NULL DEFAULT NULL COMMENT '创建时间',
    `update_user_id` bigint                                                       NULL DEFAULT NULL COMMENT '修改用户id',
    `update_time`    datetime                                                     NULL DEFAULT NULL COMMENT '修改时间',
    `state`          bit(1)                                                       NULL DEFAULT b'1' COMMENT '状态(1-有效，0-无效)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表'
  ROW_FORMAT = Dynamic;

/*
 * MIT License
 * Copyright 2024-present cht
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

-- ----------------------------
-- Table structure for system_user_info
-- ----------------------------
DROP TABLE IF EXISTS `system_user_info`;
CREATE TABLE `system_user_info`
(
    `id`             bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_name`      varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '登录名',
    `password`       varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
    `salt`           varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '盐',
    `nick_name`      varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '用户名',
    `phone`          varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '手机号',
    `email`          varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NULL DEFAULT NULL COMMENT '电子邮箱',
    `create_user_id` bigint                                                        NULL DEFAULT NULL COMMENT '创建用户id',
    `create_time`    datetime                                                      NULL DEFAULT NULL COMMENT '创建时间',
    `update_user_id` bigint                                                        NULL DEFAULT NULL COMMENT '修改用户id',
    `update_time`    datetime                                                      NULL DEFAULT NULL COMMENT '修改时间',
    `state`          bit(1)                                                        NULL DEFAULT b'1' COMMENT '状态(0-无效，1-有效)',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_username_state` (`user_name` ASC, `state` ASC) USING BTREE COMMENT '用户名和状态联合索引，主要用于登录时用户查询'
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表'
  ROW_FORMAT = Dynamic;

/*
 * MIT License
 * Copyright 2024-present cht
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

-- ----------------------------
-- Table structure for system_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `system_user_role_rel`;
CREATE TABLE `system_user_role_rel`
(
    `id`             bigint   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`        bigint   NULL DEFAULT NULL COMMENT '用户id',
    `role_id`        bigint   NULL DEFAULT NULL COMMENT '角色id',
    `create_user_id` bigint   NULL DEFAULT NULL COMMENT '创建用户id',
    `create_time`    datetime NULL DEFAULT NULL COMMENT '创建时间',
    `update_user_id` bigint   NULL DEFAULT NULL COMMENT '修改用户id',
    `update_time`    datetime NULL DEFAULT NULL COMMENT '修改时间',
    `state`          bit(1)   NULL DEFAULT b'1' COMMENT '状态(1-有效，0-无效)',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `index_user_state` (`user_id` ASC, `state` ASC) USING BTREE COMMENT '用户id和状态联合索引',
    INDEX `index_role_state` (`role_id` ASC, `state` ASC) USING BTREE COMMENT '角色id和状态联合索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联关系表'
  ROW_FORMAT = Dynamic;


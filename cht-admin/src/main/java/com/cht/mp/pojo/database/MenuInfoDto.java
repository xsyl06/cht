package com.cht.mp.pojo.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统菜单表
 * @TableName system_menu_info
 */
@TableName(value ="system_menu_info")
@Data
public class MenuInfoDto implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 菜单类型(0-菜单，1-iframe，2-外链，3-按钮)
     */
    private Integer menuType;

    /**
     * 菜单名称
     */
    private String menuTitle;

    /**
     * 路由名称
     */
    private String menuName;

    /**
     * 路由地址
     */
    private String menuPath;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 图标
     */
    private String icon;

    /**
     * 右侧图标
     */
    private String extraIcon;

    /**
     * 排序
     */
    @TableField(value = "`rank`")
    private Integer rank;

    /**
     * 权限标识
     */
    private String auths;

    /**
     * 外链地址
     */
    private String frameSrc;

    /**
     * 缓存页面(1-缓存，0-不缓存)
     */
    private Boolean keepAlive;

    /**
     * 是否展示页面(1-展示，0-不展示)
     */
    private Boolean showLink;

    /**
     * 是否展示父菜单(1-展示，0-不展示)
     */
    private Boolean showParent;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人id
     */
    private Long updateUserId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 状态(1-有效，0-无效)
     */
    private Boolean state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
package com.cht.mp.pojo.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 菜单角色关联关系表
 * @TableName system_menu_role_rel
 */
@TableName(value ="system_menu_role_rel")
@Data
public class MenuRoleRelDto implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 创建用户id
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改用户id
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
package com.cht.mp.pojo.database;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 系统字典表
 * @TableName system_dict
 */
@TableName(value ="system_dict")
@Data
public class DictDto implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 字典类型
     */
    private String dictType;

    /**
     * 字典类型名称
     */
    private String dictTypeName;

    /**
     * 字典中文名称
     */
    private String dictLabel;

    /**
     * 字典值
     */
    private String dictValue;

    /**
     * 字典值2
     */
    private String dictValue2;

    /**
     * 字典值3
     */
    private String dictValue3;

    /**
     * 字典值4
     */
    private String dictValue4;

    /**
     * 排序
     */
    private Integer dictSort;

    /**
     * 状态(1-有效，0-无效)
     */
    private Boolean state;

    /**
     * 创建用户id
     */
    private Long createUserId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新用户id
     */
    private Long updateUserId;

    /**
     * 更新用户时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
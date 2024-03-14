package com.cht.admin.pojo;

import lombok.Data;

import java.util.List;

@Data
public class BasePageVo<T> {

    /**
     * 数据
     */
    private List<T> list;
    /**
     * 总数
     */
    private Long total;
    /**
     * 当前页
     */
    private Long pageSize;
    /**
     * 每页数量
     */
    private Long currentPage;
}

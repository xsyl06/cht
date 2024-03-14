package com.cht.admin.pojo.system;

import com.cht.mp.pojo.database.RoleInfoDto;
import lombok.Data;

import java.io.Serial;
import java.util.List;

@Data
public class RoleInfoVo extends RoleInfoDto {
    @Serial
    private static final long serialVersionUID = -1848972164591356738L;
    private String createUserName;
    private List<Long> menuList;
    /**
     * 每页数量
     */
    private Long pageSize;
    /**
     * 当前页
     */
    private Long currentPage;

}

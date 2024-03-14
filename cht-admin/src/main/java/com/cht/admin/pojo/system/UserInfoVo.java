package com.cht.admin.pojo.system;

import com.cht.mp.pojo.database.UserInfoDto;
import lombok.Data;

import java.io.Serial;
import java.util.List;

@Data
public class UserInfoVo extends UserInfoDto {
    @Serial
    private static final long serialVersionUID = 8982580872468754667L;
    private String createUserName;
    private List<Long> roleList;
    /**
     * 每页数量
     */
    private Long pageSize;
    /**
     * 当前页
     */
    private Long currentPage;

}

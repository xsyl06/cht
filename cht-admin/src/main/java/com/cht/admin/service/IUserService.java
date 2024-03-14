package com.cht.admin.service;

import com.cht.admin.pojo.BasePageVo;
import com.cht.admin.pojo.system.UserInfoVo;

import java.util.List;

public interface IUserService {

    BasePageVo<UserInfoVo> getUserInfo(UserInfoVo vo);

    boolean changeUserState(UserInfoVo vo);
    boolean saveUser(UserInfoVo vo);
    List<Long> getUserRoleList(Long userId);

    boolean updateUser(UserInfoVo vo);

    boolean resetPwd(Long userId);
}

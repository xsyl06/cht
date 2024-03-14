package com.cht.admin.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollectionUtil;
import com.cht.admin.pojo.LoginUserInfo;
import com.cht.admin.service.BaseService;

import java.util.List;

public class StpInterfaceImpl extends BaseService implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        if (CollectionUtil.isNotEmpty(loginUserInfo.getAuths())) {
            return loginUserInfo.getAuths();
        }
        return null;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        if (CollectionUtil.isNotEmpty(loginUserInfo.getRoles())) {
            return loginUserInfo.getRoles();
        }
        return null;
    }
}

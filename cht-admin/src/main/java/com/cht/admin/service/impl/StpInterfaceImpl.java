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

package com.cht.admin.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollectionUtil;
import com.cht.admin.pojo.LoginUserInfo;
import com.cht.admin.service.BaseService;

import java.util.List;
/**
 * @description: so-token登录鉴权方法
 * @author Wang
 * @date 2024/3/18 11:18
 * @version 1.0
 */
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

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

package com.cht.admin.service;

import com.cht.admin.pojo.LoginUserInfo;
import com.cht.admin.pojo.R;
/**
 * @description: 登录的service类，主要用于处理登录相关的一些信息
 * @author Wang
 * @date 2024/3/15 15:20
 * @version 1.0
 */
public interface ILoginService {

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    R login(String username, String password);

    /**
     * 获取登录用户角色所拥有的菜单
     * @return
     */
    R getAsyncRoutes();

    /**
     * 刷新用户的token
     * @param input refreshToken刷新token
     * @return 返回参数，包括refreshToken和accessToken
     */
    R refreshToken(LoginUserInfo input);
}

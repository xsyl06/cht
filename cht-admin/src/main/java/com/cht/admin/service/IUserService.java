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

import com.cht.admin.pojo.BasePageVo;
import com.cht.admin.pojo.system.UserInfoVo;
import com.cht.enums.ReturnEnum;

import java.util.List;
/**
 * @description: 用户管理的service类
 * @author Wang
 * @date 2024/3/15 15:37
 * @version 1.0
 */
public interface IUserService {
    /**
     * 获取用户信息列表
     * @param vo 登录名、手机号、用户状态
     * @return 分页查询的用户列表
     */
    BasePageVo<UserInfoVo> getUserInfo(UserInfoVo vo);

    /**
     * 根据用户id更改用户状态
     * @param vo 用户id
     * @return 是否修改成功
     */
    boolean changeUserState(UserInfoVo vo);

    /**
     * 保存用户信息
     * @param vo 用户信息
     * @return 是否保存成功
     */
    boolean saveUser(UserInfoVo vo);

    /**
     * 根据用户id获取用户角色列表
     * @param userId 用户id
     * @return 用户角色id集合
     */
    List<Long> getUserRoleList(Long userId);

    /**
     * 更新用户信息
     * @param vo 用户信息
     * @return 是否更新成功
     */
    boolean updateUser(UserInfoVo vo);

    /**
     * 重置用户密码
     * @param userId 用户id
     * @return 是否重置成功
     */
    boolean resetPwd(Long userId);

    /**
     * 更改用户密码
     *
     * @param vo 用户id、旧密码、新密码
     * @return 是否更改成功
     */
    ReturnEnum changePwd(UserInfoVo vo);
}

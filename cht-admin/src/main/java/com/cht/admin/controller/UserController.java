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

package com.cht.admin.controller;

import com.alibaba.fastjson2.JSON;
import com.cht.admin.pojo.BasePageVo;
import com.cht.admin.pojo.R;
import com.cht.admin.pojo.system.UserInfoVo;
import com.cht.enums.ReturnEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @description: 用户管理控制器
 * @author Wang
 * @date 2024/3/15 14:50
 * @version 1.0 
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController extends BaseController {

    /**
     * 分页查询用户列表
     * @param vo 登录名、手机号、状态、当前页码、每页条数
     * @return 分页查询出的用户列表
     */
    @PostMapping("/list")
    public R getUserList(@RequestBody UserInfoVo vo) {
        BasePageVo<UserInfoVo> userInfo = userService.getUserInfo(vo);
        return R.SUCCESS(userInfo);
    }

    /**
     * 修改用户状态
     * @param vo 用户id、需要修改的用户状态
     * @return 是否修改成功
     */
    @PostMapping("/changeState")
    public R changeUserState(@RequestBody UserInfoVo vo) {
        log.info("开始修改用户id为[{}]的状态到:[{}]", vo.getId(), vo.getState());
        // 修改用户状态
        if (userService.changeUserState(vo)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.CHANGE_USER_STATE_FAIL);
        }
    }

    /**
     * 根据用户id获取用户角色信息
     * @param id 用户id
     * @return 用户角色的id列表
     */
    @GetMapping("/getUserRoles/{id}")
    public R getUserRoles(@PathVariable("id") Long id) {
        List<Long> userRoleList = userService.getUserRoleList(id);
        return R.SUCCESS(userRoleList);
    }

    /**
     * 新增用户接口
     * @param vo 需要新增用户的信息
     * @return 是否新增成功
     */
    @PostMapping("/add")
    public R addUser(@RequestBody UserInfoVo vo){
        log.info("开始添加用户：{}", JSON.toJSONString(vo));
        if (userService.saveUser(vo)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.ADD_USER_FAIL);
        }
    }

    /**
     * 修改用户信息
     * @param vo 需要修改的用户信息
     * @return 是否修改成功
     */
    @PostMapping("/update")
    public R updateUser(@RequestBody UserInfoVo vo) {
        log.info("开始修改用户：{}",JSON.toJSONString(vo));
        if (userService.updateUser(vo)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.UPDATE_USER_FAIL);
        }
    }

    /**
     * 根据用户id重置用户密码
     * @param id 用户id
     * @return 是否重置成功
     */
    @GetMapping("/resetPwd/{id}")
    public R resetPwd(@PathVariable("id") Long id) {
        log.info("开始重置用户id为[{}]的密码", id);
        if (userService.resetPwd(id)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.RESET_USER_PWD_FAIL);
        }
    }
    @PostMapping("/changePwd")
    public R changePwd(@RequestBody UserInfoVo vo) {
        log.info("开始修改用户密码：{}", JSON.toJSONString(vo));
        ReturnEnum returnEnum = userService.changePwd(vo);
        if (returnEnum.isSuccess()) {
            return R.SUCCESS();
        }else {
            return R.FAIL(returnEnum);
        }
    }


}

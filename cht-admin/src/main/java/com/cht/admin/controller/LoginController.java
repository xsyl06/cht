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

import com.cht.admin.pojo.LoginUserInfo;
import com.cht.admin.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Wang
 * @version 1.0
 * @description: 登录相关控制类
 * @date 2024/3/15 14:04
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class LoginController extends BaseController {
    /**
     * 用户登录接口
     *
     * @param input 包括用户名、密码
     * @return 用户登录信息
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginUserInfo input) {
        return loginService.login(input.getUserName(), input.getPassword());
    }

    /**
     * 刷新token接口，当token失效时通过该接口刷新token
     *
     * @param loginUserInfo 登录用户信息
     * @return 刷新后的token
     */
    @PostMapping("/refreshToken")
    public R refreshToken(@RequestBody LoginUserInfo loginUserInfo) {
        return loginService.refreshToken(loginUserInfo);
    }

    /**
     * 根据登录账号获取该账号的菜单路由，只返回有效状态的角色和菜单
     *
     * @return 树形结构的菜单
     */
    @GetMapping("/getAsyncRoutes")
    public R getAsyncRoutes() {
        return loginService.getAsyncRoutes();
    }

    /**
     * 测试接口，用于测试服务状态是否正常
     * @return 成功
     */
    @GetMapping("/testAction")
    public R testAction() {
        return R.SUCCESS();
    }
}

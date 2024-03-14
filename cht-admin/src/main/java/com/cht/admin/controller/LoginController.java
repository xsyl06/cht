package com.cht.admin.controller;

import com.cht.admin.pojo.LoginUserInfo;
import com.cht.admin.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class LoginController extends BaseController {
    /**
     * 用户登录接口
     * @param input 包括用户名、密码
     * @return 用户登录信息
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginUserInfo input) {
        return loginService.login(input.getUserName(), input.getPassword());
    }

    @PostMapping("/refreshToken")
    public R refreshToken(@RequestBody LoginUserInfo input) {
        return loginService.refreshToken(input);
    }

    @GetMapping("/getAsyncRoutes")
    public R getAsyncRoutes() {
        return loginService.getAsyncRoutes();
    }
}

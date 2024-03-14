package com.cht.admin.service;

import com.cht.admin.pojo.LoginUserInfo;
import com.cht.admin.pojo.R;

public interface ILoginService {

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return
     */
    R login(String username, String password);

    R getAsyncRoutes();

    R refreshToken(LoginUserInfo input);
}

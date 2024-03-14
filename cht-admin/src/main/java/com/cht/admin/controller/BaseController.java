package com.cht.admin.controller;

import com.cht.admin.service.ILoginService;
import com.cht.admin.service.IMenuService;
import com.cht.admin.service.IRoleService;
import com.cht.admin.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaseController {

    @Autowired
    ILoginService loginService;

    @Autowired
    IUserService userService;

    @Autowired
    IMenuService menuService;

    @Autowired
    IRoleService roleService;

}

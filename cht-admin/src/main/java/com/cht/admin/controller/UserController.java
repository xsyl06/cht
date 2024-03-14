package com.cht.admin.controller;

import com.alibaba.fastjson2.JSON;
import com.cht.admin.pojo.BasePageVo;
import com.cht.admin.pojo.R;
import com.cht.admin.pojo.system.UserInfoVo;
import com.cht.enums.ReturnEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController extends BaseController {


    @PostMapping("/list")
    public R getUserList(@RequestBody UserInfoVo vo) {
        BasePageVo<UserInfoVo> userInfo = userService.getUserInfo(vo);
        return R.SUCCESS(userInfo);
    }

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

    @GetMapping("/getUserRoles/{id}")
    public R getUserRoles(@PathVariable("id") Long id) {
        List<Long> userRoleList = userService.getUserRoleList(id);
        return R.SUCCESS(userRoleList);
    }
    @PostMapping("/add")
    public R addUser(@RequestBody UserInfoVo vo){
        log.info("开始添加用户：{}", JSON.toJSONString(vo));
        if (userService.saveUser(vo)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.ADD_USER_FAIL);
        }
    }

    @PostMapping("/update")
    public R updateUser(@RequestBody UserInfoVo vo) {
        log.info("开始修改用户：{}",JSON.toJSONString(vo));
        if (userService.updateUser(vo)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.UPDATE_USER_FAIL);
        }
    }

    @GetMapping("/resetPwd/{id}")
    public R resetPwd(@PathVariable("id") Long id) {
        log.info("开始重置用户id为[{}]的密码", id);
        if (userService.resetPwd(id)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.RESET_USER_PWD_FAIL);
        }
    }


}

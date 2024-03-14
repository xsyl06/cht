package com.cht.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.cht.admin.pojo.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.cht.enums.ReturnEnum.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {NotPermissionException.class})
    @ResponseBody
    public R handleNotPermissionException(NotPermissionException e) {
        log.error("[{}]缺少[{}]权限，未通过校验",e.getLoginType(),e.getPermission());
        return R.FAIL(PERMISSION_FAIL);
    }

    @ExceptionHandler(value = {NotRoleException.class})
    @ResponseBody
    public R handleNotRoleException(NotRoleException e) {
        log.error("[{}]缺少[{}]角色，未通过校验",e.getLoginType(),e.getRole());
        return R.FAIL(ROLE_FAIL);
    }
    @ExceptionHandler(value = {NotLoginException.class})
    @ResponseBody
    public R handleNotLoginException(NotLoginException e) {
        log.error(e.getMessage());
        R r;
        switch (e.getType()) {
            case NotLoginException.NOT_TOKEN -> {
                r = R.FAIL(NOT_TOKEN);
            }
            case NotLoginException.INVALID_TOKEN -> {
                r = R.FAIL(INVALID_TOKEN);
            }
            case NotLoginException.TOKEN_TIMEOUT -> {
                r = R.FAIL(TOKEN_TIMEOUT);
            }
            case NotLoginException.BE_REPLACED -> {
                r = R.FAIL(BE_REPLACED);
            }
            case NotLoginException.KICK_OUT -> {
                r = R.FAIL(KICK_OUT);
            }
            case NotLoginException.TOKEN_FREEZE -> {
                r = R.FAIL(TOKEN_FREEZE);
            }
            default -> r = R.FAIL(NOT_TOKEN);
        }
        return r;
    }
    @ExceptionHandler
    @ResponseBody
    public R handleNotRoleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.FAIL();
    }
}

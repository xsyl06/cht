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

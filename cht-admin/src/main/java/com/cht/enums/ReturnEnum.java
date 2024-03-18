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

package com.cht.enums;

import lombok.Getter;

@Getter
public enum ReturnEnum {
    SUCCESS("000000", true, "交易成功"),
    FAIL("999999", false, "交易失败"),
    USER_OR_PWD_ERROR("100001", false, "用户名或密码错误"),
    ROLE_FAIL("200001", false, "角色校验失败，您暂时无权使用该功能"),
    PERMISSION_FAIL("200002", false, "权限校验失败，您暂时无权使用该功能"),
    NOT_TOKEN("200011", false, "登录状态校验失败，您暂时无权限使用该功能"),
    INVALID_TOKEN("200012", false, "登录无效"),
    TOKEN_TIMEOUT("200013", false, "登录已过期，请重新登录"),
    BE_REPLACED("200014", false, "账号在其他地方登录，您已下线"),
    KICK_OUT("200015", false, "您已被踢出，请重新登录"),
    TOKEN_FREEZE("20016",false,"您的账号已被冻结，请刷新或联系管理员"),
    MENU_LIST_FAIL("30001",false,"菜单列表查询为空"),
    ADD_MENU_FAIL("30002",false,"新增菜单失败"),
    UPDATE_MENU_FAIL("30003",false,"更新菜单失败"),
    DELETE_MENU_FAIL("30004",false,"删除菜单失败"),
    ADD_ROLE_FAIL("30012",false,"新增角色失败"),
    UPDATE_ROLE_FAIL("30013",false,"更新角色失败"),
    DELETE_ROLE_FAIL("30014",false,"删除角色失败"),
    CHANGE_ROLE_STATE_FAIL("30015",false,"修改角色状态失败"),
    ADD_USER_FAIL("30022",false,"新增用户失败"),
    UPDATE_USER_FAIL("30023",false,"更新用户失败"),
    CHANGE_USER_STATE_FAIL("30025",false,"修改用户状态失败"),
    RESET_USER_PWD_FAIL("30026", false, "重置用户密码失败"),
    USER_STATE_FAIL("30027", false, "用户状态为无效，无法操作"),
    TWO_PSW_DIFF("30028", false, "原密码不正确"),
    UPDATE_USER_PWD_FAIL("30029", false, "修改密码失败"),
    ;


    private String code;
    private boolean success;

    private String message;

    ReturnEnum(String code, boolean success, String message) {
        this.code = code;
        this.success = success;
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

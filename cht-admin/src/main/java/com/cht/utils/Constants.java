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

package com.cht.utils;

public class Constants {
    public static final String SYSTEM_REDIS_KEY_PREFIX = "SYSTEM";
    public static final String LOGIN_USER = "LOGIN:USER";
    public static final String REDIS_KEY_DICT = "DICT";
    public static final String REFRESH_KEY = "REFRESH";
    public static final String BUSINESS_REDIS_KEY_PREFIX = "BUSINESS";
    public static final String ENCRYPT_PREFIX = "encrypt";

    public interface Permission {
        String MENU_ADD = "menu:btn_add";
        String MENU_UPDATE = "menu:btn_update";
        String MENU_DELETE = "menu:btn_delete";
        String ROLE_ADD = "role:btn_add";
        String ROLE_UPDATE = "role:btn_update";
        String ROLE_DELETE = "role:btn_delete";
        String USER_ADD = "user:btn_add";
        String USER_UPDATE = "user:btn_update";
        String USER_RESETPWD = "user:btn_resetpwd";
    }

}

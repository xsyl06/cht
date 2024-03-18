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

package com.cht.admin.service;

import com.cht.mp.pojo.database.MenuInfoDto;

import java.util.List;
/**
 * @description: 菜单管理的service类
 * @author Wang
 * @date 2024/3/15 15:21
 * @version 1.0
 */
public interface IMenuService {
    /**
     * 获取菜单信息列表
     * @return 菜单列表
     */
    List<MenuInfoDto> getMenuList();

    /**
     * 保存菜单信息
     * @param input 菜单信息
     * @return 保存结果
     */
    boolean saveMenu(MenuInfoDto input);

    /**
     * 更新菜单信息
     * @param input 菜单信息
     * @return 更新结果
     */
    boolean updateMenu(MenuInfoDto input);

    /**
     * 删除菜单信息
     * @param id 菜单ID
     * @return 删除结果
     */
    boolean deleteMenu(Long id);
}

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

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.cht.admin.pojo.R;
import com.cht.enums.ReturnEnum;
import com.cht.mp.pojo.database.MenuInfoDto;
import com.cht.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @description: 菜单管理模块控制器
 * @author Wang
 * @date 2024/3/15 14:34
 * @version 1.0 
 */
@RestController
@RequestMapping("/api/menu")
@Slf4j
public class MenuController extends BaseController{
    /**
     * 查询菜单列表
     * @return 菜单列表，为列表结构，由前端转为树结构
     */
    @GetMapping("/list")
    public R getMenuList() {
        log.info("开始查询菜单列表");
        List<MenuInfoDto> menuList = menuService.getMenuList();
        R r;
        if (CollectionUtil.isNotEmpty(menuList)) {
            r = R.SUCCESS(menuList);
        }else {
            r = R.FAIL(ReturnEnum.MENU_LIST_FAIL);
        }
        log.info("查询菜单列表出参：{}", JSON.toJSONString(r));
        return r;
    }

    /**
     * 保存菜单接口
     *
     * @param input 菜单信息入参
     * @return 是否成功
     */
    @PostMapping("/save")
    @SaCheckPermission(Constants.Permission.MENU_ADD)
    public R saveMenu(@RequestBody MenuInfoDto input) {
        log.info("开始保存菜单信息，入参：{}", JSON.toJSONString(input));
        if (menuService.saveMenu(input)) {
            return R.SUCCESS();
        } else {
            return R.FAIL(ReturnEnum.ADD_MENU_FAIL);
        }
    }

    /**
     * 更新菜单信息接口
     * @param input 更新菜单信息
     * @return 是否成功
     */
    @PostMapping("/update")
    @SaCheckPermission(Constants.Permission.MENU_UPDATE)
    public R updateMenu(@RequestBody MenuInfoDto input){
        log.info("开始更新菜单信息，入参：{}", JSON.toJSONString(input));
        if (menuService.updateMenu(input)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.UPDATE_MENU_FAIL);
        }
    }

    /**
     * 根据ID删除菜单信息接口
     * @param id 菜单id
     * @return 是否成功
     */
    @GetMapping("/delete/{id}")
    @SaCheckPermission(Constants.Permission.MENU_DELETE)
    public R deleteMenu(@PathVariable("id") Long id) {
        log.info("开始删除编号为[{}]的菜单", id);
        if (menuService.deleteMenu(id)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.DELETE_MENU_FAIL);
        }
    }

}

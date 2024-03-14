package com.cht.admin.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.cht.admin.pojo.R;
import com.cht.enums.ReturnEnum;
import com.cht.mp.pojo.database.MenuInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Slf4j
public class MenuController extends BaseController{

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
    @PostMapping("/save")
    public R saveMenu(@RequestBody MenuInfoDto input) {
        log.info("开始保存菜单信息，入参：{}", JSON.toJSONString(input));
        if (menuService.saveMenu(input)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.ADD_MENU_FAIL);
        }
    }

    @PostMapping("/update")
    public R updateMenu(@RequestBody MenuInfoDto input){
        log.info("开始更新菜单信息，入参：{}", JSON.toJSONString(input));
        if (menuService.updateMenu(input)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.UPDATE_MENU_FAIL);
        }
    }

    @GetMapping("/delete/{id}")
    public R deleteMenu(@PathVariable("id") Long id) {
        log.info("开始删除编号为[{}]的菜单", id);
        if (menuService.deleteMenu(id)) {
            return R.SUCCESS();
        }else {
            return R.FAIL(ReturnEnum.DELETE_MENU_FAIL);
        }
    }

}

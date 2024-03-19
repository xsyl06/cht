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
import com.alibaba.fastjson2.JSON;
import com.cht.admin.pojo.BasePageVo;
import com.cht.admin.pojo.R;
import com.cht.admin.pojo.system.RolMenuVo;
import com.cht.admin.pojo.system.RoleInfoVo;
import com.cht.enums.ReturnEnum;
import com.cht.mp.pojo.database.RoleInfoDto;
import com.cht.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @description: 角色管理处理控制器
 * @author Wang
 * @date 2024/3/15 14:40
 * @version 1.0
 */
@RestController
@RequestMapping("/api/role")
@Slf4j
public class RoleController extends BaseController {

    /**
     * 分页查询查询角色列表
     * @param vo 角色名称、角色标识、当前页码、每页条数
     * @return 角色列表和总条数
     */
    @PostMapping("/list")
    public R getRoleList(@RequestBody RoleInfoVo vo) {
        log.info("查询角色列表入参：{}", JSON.toJSONString(vo));
        BasePageVo<RoleInfoVo> roleList = roleService.getRoleList(vo);
        log.info("查询角色列表出参：{}", JSON.toJSONString(roleList));
        return R.SUCCESS(roleList);
    }

    /**
     * 角色拥有菜单列表查询
     * @param id 角色id
     * @return 角色拥有的菜单id(包括禁用)
     */
    @GetMapping("/roleMenuList/{id}")
    public R getMenuListByRoleId(@PathVariable("id") Long id) {
        log.info("查询角色菜单列表入参：{}", id);
        List<Long> menuList = roleService.getMenuListByRoleId(id);
        log.info("查询角色菜单列表出参：{}", JSON.toJSONString(menuList));
        return R.SUCCESS(menuList);
    }

    /**
     * 橘色新增和修改时查询所有菜单信息
     * @return 返回所有菜单信息
     */
    @GetMapping("/getAllMenuList")
    public R getAllMenuList() {
        log.info("角色新增和修改时开始查询所有菜单");
        List<RolMenuVo> list = roleService.getAllMenuList();
        return R.SUCCESS(list);
    }

    /**
     * 新增角色信息
     * @param vo 角色信息
     * @return 是否成功
     */
    @PostMapping("/add")
    @SaCheckPermission(Constants.Permission.ROLE_ADD)
    public R addRole(@RequestBody RoleInfoVo vo) {
        log.info("新增角色入参：{}", JSON.toJSONString(vo));
        R r;
        if (roleService.addRole(vo)) {
            r = R.SUCCESS();
        } else {
            r = R.FAIL(ReturnEnum.ADD_ROLE_FAIL);
        }
        return r;
    }

    /**
     * 更新角色信息
     * @param vo 需要更新的角色信息
     * @return 是否成功
     */
    @PostMapping("/update")
    @SaCheckPermission(Constants.Permission.ROLE_UPDATE)
    public R updateRole(@RequestBody RoleInfoVo vo) {
        log.info("修改角色入参：{}", JSON.toJSONString(vo));
        R r;
        if (roleService.updateRole(vo)) {
            r = R.SUCCESS();
        }else {
            r = R.FAIL(ReturnEnum.UPDATE_ROLE_FAIL);
        }
        return r;
    }

    /**
     * 修改角色状态
     * @param vo 角色id、需要修改成的状态
     * @return 是否修改成功
     */
    @PostMapping("/changeState")
    @SaCheckPermission(Constants.Permission.ROLE_UPDATE)
    public R changeRoleState(@RequestBody RoleInfoVo vo) {
        log.info("修改角色状态入参：{}", JSON.toJSONString(vo));
        R r;
        if (roleService.changeRoleState(vo)) {
            r = R.SUCCESS();
        }else {
            r = R.FAIL(ReturnEnum.CHANGE_ROLE_STATE_FAIL);
        }
        return r;
    }

    /**
     * 根据id删除角色信息
     * @param id 角色id
     * @return 是否删除成功
     */
    @GetMapping("/deleteRole/{id}")
    @SaCheckPermission(Constants.Permission.ROLE_DELETE)
    public R deleteRoleById(@PathVariable("id") Long id) {
        log.info("删除角色入参：{}", id);
        R r;
        if (roleService.deleteRoleById(id)) {
            r = R.SUCCESS();
        }else {
            r = R.FAIL(ReturnEnum.DELETE_ROLE_FAIL);
        }
        return r;
    }

    /**
     * 用户管理中获取所有角色
     * @return 角色列表
     */
    @GetMapping("/getAllRole")
    public R getAllRole() {
        log.info("获取所有角色");
        List<RoleInfoDto> roleInfoDtos = roleService.getAllRole();
        return R.SUCCESS(roleInfoDtos);
    }
}

package com.cht.admin.controller;

import com.alibaba.fastjson2.JSON;
import com.cht.admin.pojo.BasePageVo;
import com.cht.admin.pojo.R;
import com.cht.admin.pojo.system.RolMenuVo;
import com.cht.admin.pojo.system.RoleInfoVo;
import com.cht.enums.ReturnEnum;
import com.cht.mp.pojo.database.RoleInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@Slf4j
public class RoleController extends BaseController {

    @PostMapping("/list")
    public R getRoleList(@RequestBody RoleInfoVo vo) {
        log.info("查询角色列表入参：{}", JSON.toJSONString(vo));
        BasePageVo<RoleInfoVo> roleList = roleService.getRoleList(vo);
        log.info("查询角色列表出参：{}", JSON.toJSONString(roleList));
        return R.SUCCESS(roleList);
    }

    @GetMapping("/roleMenuList/{id}")
    public R getMenuListByRoleId(@PathVariable("id") Long id) {
        log.info("查询角色菜单列表入参：{}", id);
        List<Long> menuList = roleService.getMenuListByRoleId(id);
        log.info("查询角色菜单列表出参：{}", JSON.toJSONString(menuList));
        return R.SUCCESS(menuList);
    }

    @GetMapping("/getAllMenuList")
    public R getAllMenuList() {
        log.info("角色新增和删除时开始查询所有菜单");
        List<RolMenuVo> list = roleService.getAllMenuList();
        return R.SUCCESS(list);
    }

    @PostMapping("/add")
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

    @PostMapping("/update")
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

    @PostMapping("/changeState")
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

    @GetMapping("/deleteRole/{id}")
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

    @GetMapping("/getAllRole")
    public R getAllRole() {
        log.info("获取所有角色");
        List<RoleInfoDto> roleInfoDtos = roleService.getAllRole();
        return R.SUCCESS(roleInfoDtos);
    }
}

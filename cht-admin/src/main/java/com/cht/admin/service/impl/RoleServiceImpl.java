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

package com.cht.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cht.admin.mapper.WrapperFactory;
import com.cht.admin.pojo.BasePageVo;
import com.cht.admin.pojo.LoginUserInfo;
import com.cht.admin.pojo.system.RolMenuVo;
import com.cht.admin.pojo.system.RoleInfoVo;
import com.cht.admin.service.BaseService;
import com.cht.admin.service.IRoleService;
import com.cht.mp.pojo.database.MenuInfoDto;
import com.cht.mp.pojo.database.MenuRoleRelDto;
import com.cht.mp.pojo.database.RoleInfoDto;
import com.cht.mp.service.MenuRoleRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
@Slf4j
@Service
public class RoleServiceImpl extends BaseService implements IRoleService {
    /**
     * mybatis-plus的service服务，用于批量插入
     */
    @Autowired
    private MenuRoleRelService menuRoleRelService;

    /**
     * 分页获取角色列表
     * @param vo 角色查询的信息
     * @return 角色列表
     */
    @Override
    public BasePageVo<RoleInfoVo> getRoleList(RoleInfoVo vo) {
        IPage<RoleInfoVo> page = roleInfoMapper.selectJoinPage(new Page<>(vo.getCurrentPage(),vo.getPageSize()), RoleInfoVo.class,WrapperFactory.queryRoleList(vo));
        BasePageVo<RoleInfoVo> pageVo = new BasePageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        return pageVo;
    }

    /**
     * 根据角色id获取角色菜单列表
     * @param roleId 角色id
     * @return
     */
    @Override
    public List<Long> getMenuListByRoleId(Long roleId) {
        List<MenuRoleRelDto> menuIdList = menuRoleRelMapper.selectList(WrapperFactory.queryMenuListByRoleId(roleId));
        return menuIdList.stream().map(MenuRoleRelDto::getMenuId).collect(Collectors.toList());
    }

    /**
     * 角色新增或更新时，获取所有菜单列表
     * @return 菜单信息的列表
     */
    @Override
    public List<RolMenuVo> getAllMenuList() {
        List<MenuInfoDto> menus = menuInfoMapper.selectList(WrapperFactory.getAllMenus());
        List<RolMenuVo> list = new ArrayList<>();
        handlerTree(menus,list);
        log.info("角色新增或更新时，获取所有菜单列表：{}", JSON.toJSONString(list));
        return list;
    }

    /**
     * 添加角色
     * @param vo 角色信息
     * @return 是否添加成功
     */
    @Override
    @Transactional
    public boolean addRole(RoleInfoVo vo){
        log.info("service层开始新增角色");
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        vo.setCreateUserId(loginUserInfo.getId());
        vo.setCreateTime(new Date(System.currentTimeMillis()));
        int count = roleInfoMapper.insert(vo);
        boolean roleAddFlag = count > 0;
        if (roleAddFlag) {
            Long id = vo.getId();
            List<MenuRoleRelDto> collect = vo.getMenuList().stream().map(menuId -> {
                MenuRoleRelDto menuRoleRelDto = new MenuRoleRelDto();
                menuRoleRelDto.setMenuId(menuId);
                menuRoleRelDto.setState(true);
                menuRoleRelDto.setRoleId(id);
                menuRoleRelDto.setCreateUserId(loginUserInfo.getId());
                menuRoleRelDto.setCreateTime(new Date(System.currentTimeMillis()));
                return menuRoleRelDto;
            }).collect(Collectors.toList());
            log.info("service层开始新增角色对应权限，{}", JSON.toJSONString(collect));
            if (CollectionUtil.isNotEmpty(collect)) {
                menuRoleRelService.saveBatch(collect);
            }
        }
        return roleAddFlag;
    }

    /**
     * 更新角色
     * @param vo 角色信息
     * @return 是否更新成功
     */
    @Override
    @Transactional
    public boolean updateRole(RoleInfoVo vo) {
        log.info("service层开始更新角色信息");
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        vo.setUpdateUserId(loginUserInfo.getId());
        vo.setUpdateTime(new Date(System.currentTimeMillis()));
        boolean roleAddFlag = roleInfoMapper.update(WrapperFactory.updateRole(vo)) > 0;
        if (roleAddFlag) {
            Long roleId = vo.getId();
            //先删除，再新增关联关系
            log.info("service层开始删除原有角色菜单关联关系");
            menuRoleRelService.remove(WrapperFactory.deleteByRoleId(vo));
            List<MenuRoleRelDto> collect = vo.getMenuList().stream().map(menuId ->{
                MenuRoleRelDto dto = new MenuRoleRelDto();
                dto.setRoleId(roleId);
                dto.setState(true);
                dto.setMenuId(menuId);
                dto.setCreateTime(new Date(System.currentTimeMillis()));
                dto.setCreateUserId(loginUserInfo.getId());
                return dto;
            }).collect(Collectors.toList());
            log.info("service层开始新增角色菜单关联关系");
            menuRoleRelService.saveBatch(collect);
        }
        return roleAddFlag;
    }

    /**
     * 变更角色状态
     * @param vo 角色状态
     * @return 是否变更成功
     */
    @Override
    public boolean changeRoleState(RoleInfoVo vo) {
        log.info("service层开始更新角色id[{}]状态到[{}]", vo.getId(), vo.getState());
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        vo.setUpdateTime(new Date(System.currentTimeMillis()));
        vo.setUpdateUserId(loginUserInfo.getId());
        int update = roleInfoMapper.update(WrapperFactory.changeRoleState(vo));
        return update > 0;
    }

    /**
     * 根据id删除角色信息，会删除用户角色关联关系表和角色菜单关联关系表中数据
     * @param roleId 角色id
     * @return
     */
    @Override
    public boolean deleteRoleById(Long roleId) {
        log.info("service层开始删除角色id[{}]", roleId);
        int deleteCount = roleInfoMapper.delete(WrapperFactory.deleteRoleById(roleId));
        if (deleteCount > 0) {
            log.info("service层删除角色id[{}]的菜单权限", roleId);
            RoleInfoVo vo = new RoleInfoVo();
            vo.setId(roleId);
            menuRoleRelService.remove(WrapperFactory.deleteByRoleId(vo));
            userRoleRelMapper.delete(WrapperFactory.deleteURRByRoleId(vo));
        }
        return deleteCount > 0;
    }

    /**
     * 新增修改用户时查询所有角色
     * @return
     */
    @Override
    public List<RoleInfoDto> getAllRole() {
        log.info("service层开始查询所有角色");
        return roleInfoMapper.selectList(WrapperFactory.getAllRole());
    }

    /**
     * 将菜单处理成树结构
     * @param menus 需要处理的菜单列表
     * @param list 返回的树形列表
     */
    private void handlerTree(List<MenuInfoDto> menus,List<RolMenuVo> list) {
        Map<Long, RolMenuVo> alreadyMap = new HashMap<>();
        Map<Long, List<RolMenuVo>> tmpMap = new HashMap<>();
        for (MenuInfoDto menu : menus) {
            RolMenuVo rolMenuVo= new RolMenuVo(menu.getId(), menu.getMenuTitle(),menu.getState());
            if (menu.getParentId() == null) {
                list.add(rolMenuVo);
            } else {
                if (alreadyMap.containsKey(menu.getParentId())) {
                    RolMenuVo parent = alreadyMap.get(menu.getParentId());
                    if (parent.getChildren() != null) {
                        parent.getChildren().add(rolMenuVo);
                    }else {
                        List<RolMenuVo> l = new ArrayList<>();
                        l.add(rolMenuVo);
                        parent.setChildren(l);
                    }
                }else {
                    if (tmpMap.containsKey(menu.getParentId())) {
                        tmpMap.get(menu.getParentId()).add(rolMenuVo);
                    }else {
                        List<RolMenuVo> l = new ArrayList<>();
                        l.add(rolMenuVo);
                        tmpMap.put(menu.getParentId(),l);
                    }
                }
            }
            if (tmpMap.containsKey(menu.getId())) {
                rolMenuVo.setChildren(tmpMap.get(menu.getId()));
            }
            alreadyMap.put(menu.getId(), rolMenuVo);
        }

    }
}

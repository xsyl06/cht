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
    @Autowired
    private MenuRoleRelService menuRoleRelService;
    @Override
    public BasePageVo<RoleInfoVo> getRoleList(RoleInfoVo vo) {
        IPage<RoleInfoVo> page = roleInfoMapper.selectJoinPage(new Page<>(vo.getCurrentPage(),vo.getPageSize()), RoleInfoVo.class,WrapperFactory.queryRoleList(vo));
        BasePageVo<RoleInfoVo> pageVo = new BasePageVo<>();
        pageVo.setList(page.getRecords());
        pageVo.setTotal(page.getTotal());
        return pageVo;
    }

    @Override
    public List<Long> getMenuListByRoleId(Long roleId) {
        List<MenuRoleRelDto> menuIdList = menuRoleRelMapper.selectList(WrapperFactory.queryMenuListByRoleId(roleId));
        return menuIdList.stream().map(MenuRoleRelDto::getMenuId).collect(Collectors.toList());
    }

    @Override
    public List<RolMenuVo> getAllMenuList() {
        List<MenuInfoDto> menus = menuInfoMapper.selectList(WrapperFactory.getAllMenus());
        List<RolMenuVo> list = new ArrayList<>();
        handlerTree(menus,list);
        log.info("角色新增或更新时，获取所有菜单列表：{}", JSON.toJSONString(list));
        return list;
    }
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

    @Override
    public boolean changeRoleState(RoleInfoVo vo) {
        log.info("service层开始更新角色id[{}]状态到[{}]", vo.getId(), vo.getState());
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        vo.setUpdateTime(new Date(System.currentTimeMillis()));
        vo.setUpdateUserId(loginUserInfo.getId());
        int update = roleInfoMapper.update(WrapperFactory.changeRoleState(vo));
        return update > 0;
    }
    @Override
    public boolean deleteRoleById(Long roleId) {
        log.info("service层开始删除角色id[{}]", roleId);
        int deleteCount = roleInfoMapper.delete(WrapperFactory.deleteRoleById(roleId));
        if (deleteCount > 0) {
            log.info("service层删除角色id[{}]的菜单权限", roleId);
            RoleInfoVo vo = new RoleInfoVo();
            vo.setId(roleId);
            menuRoleRelService.remove(WrapperFactory.deleteByRoleId(vo));
        }
        return deleteCount > 0;
    }

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
            RolMenuVo rolMenuVo= new RolMenuVo(menu.getId(), menu.getMenuTitle());
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

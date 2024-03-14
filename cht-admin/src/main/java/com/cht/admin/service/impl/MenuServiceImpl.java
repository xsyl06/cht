package com.cht.admin.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.cht.admin.mapper.WrapperFactory;
import com.cht.admin.pojo.LoginUserInfo;
import com.cht.admin.service.BaseService;
import com.cht.admin.service.IMenuService;
import com.cht.mp.pojo.database.MenuInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MenuServiceImpl extends BaseService implements IMenuService {

    @Override
    public List<MenuInfoDto> getMenuList() {
        return menuInfoMapper.selectList(WrapperFactory.queryMenuList());
    }

    @Override
    public boolean saveMenu(MenuInfoDto input) {
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        input.setCreateTime(new Date(System.currentTimeMillis()));
        input.setCreateUserId(loginUserInfo.getId());
        int insert = menuInfoMapper.insert(input);
        return insert > 0;
    }

    @Override
    public boolean updateMenu(MenuInfoDto input) {
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        input.setUpdateUserId(loginUserInfo.getId());
        input.setUpdateTime(new Date(System.currentTimeMillis()));
        int update = menuInfoMapper.update(WrapperFactory.updateMenuById(input));
        return update > 0;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean deleteMenu(Long id) {
        List<MenuInfoDto> idList = getMenuAndChildList(id);
        List<Long> collect = idList.stream().map(MenuInfoDto::getId).collect(Collectors.toList());
        collect.add(id);
        log.info("查询出的需要删除的菜单与角色关联关系表中的菜单id为：{}", JSON.toJSONString(collect));
        //先删除角色下的菜单
        if(CollectionUtil.isNotEmpty(collect)){
            menuRoleRelMapper.delete(WrapperFactory.deleteMenuRoleRelByMenuId(collect));
        }
        //再删除该id的菜单和父id为这个的子菜单和按钮
        int deleteNum = menuInfoMapper.delete(WrapperFactory.deleteMenuByIds(collect));
        return deleteNum > 0;
    }

    private List<MenuInfoDto> getMenuAndChildList(Long id) {
        List<MenuInfoDto> returnList = new ArrayList<>();
        List<MenuInfoDto> idList = menuInfoMapper.selectList(WrapperFactory.queryMenuIdByParentId(id));
        if (!CollectionUtil.isEmpty(idList)) {
            returnList.addAll(idList);
            for (MenuInfoDto menuInfoDto : idList) {
                List<MenuInfoDto> menuInfoDtos = getMenuAndChildList(menuInfoDto.getId());
                if (CollectionUtil.isNotEmpty(menuInfoDtos)) {
                    returnList.addAll(menuInfoDtos);
                }
            }
        }
        return returnList;
    }
}

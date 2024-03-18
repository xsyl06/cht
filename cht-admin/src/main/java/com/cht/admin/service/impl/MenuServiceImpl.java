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
/**
 * @description: 菜单服务实现类
 * @author Wang
 * @date 2024/3/15 15:50
 * @version 1.0
 */
@Service
@Slf4j
public class MenuServiceImpl extends BaseService implements IMenuService {

    @Override
    public List<MenuInfoDto> getMenuList() {
        return menuInfoMapper.selectList(WrapperFactory.queryMenuList());
    }

    /**
     * 保存菜单
     * @param input 菜单信息
     * @return 是否保存成功
     */
    @Override
    public boolean saveMenu(MenuInfoDto input) {
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        input.setCreateTime(new Date(System.currentTimeMillis()));
        input.setCreateUserId(loginUserInfo.getId());
        int insert = menuInfoMapper.insert(input);
        return insert > 0;
    }

    /**
     * 更新菜单
     * @param input 菜单信息
     * @return 是否更新成功
     */
    @Override
    public boolean updateMenu(MenuInfoDto input) {
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        input.setUpdateUserId(loginUserInfo.getId());
        input.setUpdateTime(new Date(System.currentTimeMillis()));
        int update = menuInfoMapper.update(WrapperFactory.updateMenuById(input));
        return update > 0;
    }

    /**
     * 删除菜单及其下所有子菜单和权限(同步删除菜单与角色关联关系)
     * @param id 菜单ID
     * @return 是否删除成功
     */
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

    /**
     * 递归处理菜单和子菜单列表
     * @param id 菜单id
     * @return 菜单列表
     */
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

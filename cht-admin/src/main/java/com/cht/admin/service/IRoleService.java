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

import com.cht.admin.pojo.BasePageVo;
import com.cht.admin.pojo.system.RolMenuVo;
import com.cht.admin.pojo.system.RoleInfoVo;
import com.cht.mp.pojo.database.RoleInfoDto;

import java.util.List;
/**
 * @description: 角色管理service类
 * @author Wang
 * @date 2024/3/15 15:23
 * @version 1.0 
 */
public interface IRoleService {
    /**
     * @description: 获取角色列表
     * @param roleInfoVo 角色查询的信息
     * @author Wang
     * @return BasePageVo<RoleInfoVo> 分页的角色列表
     */
    BasePageVo<RoleInfoVo> getRoleList(RoleInfoVo roleInfoVo);

    /**
     * @description: 根据角色id获取菜单的id集合
     * @param roleId 角色id
     * @return 菜单的id列表
     */
    List<Long> getMenuListByRoleId(Long roleId);

    /**
     * 获取所有菜单信息
     * @return 菜单信息列表
     */
    List<RolMenuVo> getAllMenuList();

    /**
     * @description: 添加角色
     * @param vo 角色信息
     * @return true 添加成功 false 添加失败
     */
    boolean addRole(RoleInfoVo vo);

    /**
     * @description: 修改角色信息
     * @param vo 角色信息
     * @return true 修改成功 false 修改失败
     */
    boolean updateRole(RoleInfoVo vo);

    /**
     * @description: 修改角色状态
     * @param vo 角色状态
     * @return true 修改成功 false 修改失败
     */
    boolean changeRoleState(RoleInfoVo vo);

    /**
     * @description: 根据角色id删除角色
     * @param roleId 角色id
     * @return true 删除成功 false 删除失败
     */
    boolean deleteRoleById(Long roleId);

    /**
     * 获取所有角色信息
     * @return 角色信息列表
     */
    List<RoleInfoDto> getAllRole();
}

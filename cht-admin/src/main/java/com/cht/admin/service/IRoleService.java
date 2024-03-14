package com.cht.admin.service;

import com.cht.admin.pojo.BasePageVo;
import com.cht.admin.pojo.system.RolMenuVo;
import com.cht.admin.pojo.system.RoleInfoVo;
import com.cht.mp.pojo.database.RoleInfoDto;

import java.util.List;

public interface IRoleService {

    BasePageVo<RoleInfoVo> getRoleList(RoleInfoVo roleInfoVo);

    List<Long> getMenuListByRoleId(Long roleId);

    List<RolMenuVo> getAllMenuList();

    boolean addRole(RoleInfoVo vo);

    boolean updateRole(RoleInfoVo vo);

    boolean changeRoleState(RoleInfoVo vo);

    boolean deleteRoleById(Long roleId);

    List<RoleInfoDto> getAllRole();
}

package com.cht.admin.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cht.admin.pojo.system.RoleInfoVo;
import com.cht.admin.pojo.system.UserInfoVo;
import com.cht.mp.pojo.database.*;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.List;

/**
 * wrapper工厂，统一处理mybatis-plus的wrapper
 *
 * @author xsyl06
 * @date 2024-02-28
 */
public class WrapperFactory {

    private static final int EFFECTIVE = 1;
    private static final int INVALID = 0;

    /**
     * 根据用户名(账号)获取数据库system_user_info表中的用户信息,只查询有效数据
     *
     * @param username 登录名(账号)
     * @return mybatis-plus的查询wrapper
     */
    public static LambdaQueryWrapper<UserInfoDto> queryLoginUserWrapper(String username) {
        // 构建查询条件
        LambdaQueryWrapper<UserInfoDto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserInfoDto::getUserName, username)
                .eq(UserInfoDto::getState, EFFECTIVE);
        return queryWrapper;
    }

    /**
     * 根据字典类型和字典值获取数据库system_dict表中的字典信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @return mybatis-plus的查询wrapper
     */
    public static LambdaQueryWrapper<DictDto> queryDict(String dictType, String dictValue) {
        LambdaQueryWrapper<DictDto> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictDto::getDictType, dictType)
                .eq(DictDto::getDictValue, dictValue);
        return queryWrapper;
    }

    /**
     * 获取用户的角色code列表
     * 关联system_user_role_rel喝system_role_info表，关联关系为角色id
     *
     * @param userId 查询条件，用户id
     * @return mybatis-plus的查询wrapper
     */
    public static MPJLambdaWrapper<UserRoleRelDto> queryRoleListByUserId(Long userId) {
        MPJLambdaWrapper<UserRoleRelDto> wrappers = new MPJLambdaWrapper<UserRoleRelDto>()
                .select(RoleInfoDto::getRoleCode)
                .leftJoin(RoleInfoDto.class, RoleInfoDto::getId, UserRoleRelDto::getRoleId)
                .eq(UserRoleRelDto::getUserId, userId)
                .eq(RoleInfoDto::getState, EFFECTIVE);
        return wrappers;
    }

    public static MPJLambdaWrapper<MenuInfoDto> queryMenuListByRoleCode(List<String> roleCodeList) {
        MPJLambdaWrapper<MenuInfoDto> wrappers = new MPJLambdaWrapper<MenuInfoDto>().distinct()
                .selectAll(MenuInfoDto.class)
                .rightJoin(MenuRoleRelDto.class, MenuRoleRelDto::getMenuId, MenuInfoDto::getId)
                .rightJoin(RoleInfoDto.class, RoleInfoDto::getId, MenuRoleRelDto::getRoleId)
                .in(RoleInfoDto::getRoleCode, roleCodeList)
                .eq(MenuInfoDto::getState, EFFECTIVE)
                .eq(RoleInfoDto::getState, EFFECTIVE)
                .orderBy(true,true,MenuInfoDto::getRank);
        return wrappers;
    }

    /**
     * 获取登录用户权限
     * @param userId 用户id
     * @return mybatis-plus的查询wrapper
     */
    public static MPJLambdaWrapper<MenuRoleRelDto> queryLoginUserPermissions(Long userId) {
        MPJLambdaWrapper<MenuRoleRelDto> wrappers = new MPJLambdaWrapper<MenuRoleRelDto>()
                .select(MenuInfoDto::getAuths)
                .leftJoin(MenuInfoDto.class, MenuInfoDto::getId, MenuRoleRelDto::getMenuId)
                .leftJoin(UserRoleRelDto.class, UserRoleRelDto::getRoleId, MenuRoleRelDto::getRoleId)
                .leftJoin(RoleInfoDto.class, RoleInfoDto::getId, UserRoleRelDto::getRoleId)
                .eq(UserRoleRelDto::getUserId, userId)
                .eq(MenuInfoDto::getState, EFFECTIVE)
                .eq(RoleInfoDto::getState, EFFECTIVE);
        return wrappers;
    }

    public static LambdaQueryWrapper<MenuInfoDto> queryMenuList() {
        LambdaQueryWrapper<MenuInfoDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                MenuInfoDto::getId,
                MenuInfoDto::getParentId,
                MenuInfoDto::getMenuTitle,
                MenuInfoDto::getExtraIcon,
                MenuInfoDto::getFrameSrc,
                MenuInfoDto::getAuths,
                MenuInfoDto::getMenuPath,
                MenuInfoDto::getMenuName,
                MenuInfoDto::getIcon,
                MenuInfoDto::getKeepAlive,
                MenuInfoDto::getMenuType,
                MenuInfoDto::getShowParent,
                MenuInfoDto::getState,
                MenuInfoDto::getShowLink,
                MenuInfoDto::getRank
        );
        return wrapper;
    }

    public static LambdaUpdateWrapper<MenuInfoDto> updateMenuById(MenuInfoDto menuInfoDto) {
        LambdaUpdateWrapper<MenuInfoDto> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(MenuInfoDto::getId, menuInfoDto.getId())
                .set(StrUtil.isNotBlank(menuInfoDto.getAuths()), MenuInfoDto::getAuths, menuInfoDto.getAuths())
                .set(StrUtil.isNotBlank(menuInfoDto.getMenuPath()), MenuInfoDto::getMenuPath, menuInfoDto.getMenuPath())
                .set(menuInfoDto.getMenuType() != null, MenuInfoDto::getMenuType, menuInfoDto.getMenuType())
                .set(StrUtil.isNotBlank(menuInfoDto.getMenuName()), MenuInfoDto::getMenuName, menuInfoDto.getMenuName())
                .set(menuInfoDto.getParentId() != null, MenuInfoDto::getParentId, menuInfoDto.getParentId())
                .set(StrUtil.isNotBlank(menuInfoDto.getIcon()), MenuInfoDto::getIcon, menuInfoDto.getIcon())
                .set(StrUtil.isNotBlank(menuInfoDto.getExtraIcon()), MenuInfoDto::getExtraIcon, menuInfoDto.getExtraIcon())
                .set(StrUtil.isNotBlank(menuInfoDto.getFrameSrc()), MenuInfoDto::getFrameSrc, menuInfoDto.getFrameSrc())
                .set(menuInfoDto.getRank() != null, MenuInfoDto::getRank, menuInfoDto.getRank())
                .set(menuInfoDto.getKeepAlive() != null, MenuInfoDto::getKeepAlive, menuInfoDto.getKeepAlive())
                .set(menuInfoDto.getShowLink() != null, MenuInfoDto::getShowLink, menuInfoDto.getShowLink())
                .set(menuInfoDto.getShowParent() != null, MenuInfoDto::getShowParent, menuInfoDto.getShowParent())
                .set(menuInfoDto.getState() != null, MenuInfoDto::getState, menuInfoDto.getState())
                .set(MenuInfoDto::getUpdateUserId, menuInfoDto.getUpdateUserId())
                .set(StrUtil.isNotBlank(menuInfoDto.getMenuTitle()), MenuInfoDto::getMenuTitle, menuInfoDto.getMenuTitle())
                .set(MenuInfoDto::getUpdateTime, menuInfoDto.getUpdateTime());
        return wrapper;
    }

    public static LambdaQueryWrapper<MenuInfoDto> queryMenuIdByParentId(Long id) {
        LambdaQueryWrapper<MenuInfoDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(MenuInfoDto::getId).eq(MenuInfoDto::getParentId, id);
        return wrapper;
    }

    public static LambdaQueryWrapper<MenuRoleRelDto> deleteMenuRoleRelByMenuId(List<Long> menuIds) {
        LambdaQueryWrapper<MenuRoleRelDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MenuRoleRelDto::getMenuId, menuIds);
        return wrapper;
    }

    public static LambdaQueryWrapper<MenuInfoDto> deleteMenuByIds(List<Long> menuIds) {
        LambdaQueryWrapper<MenuInfoDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(MenuInfoDto::getId, menuIds);
        return wrapper;
    }

    public static MPJLambdaWrapper<RoleInfoDto> queryRoleList(RoleInfoVo vo) {
        MPJLambdaWrapper<RoleInfoDto> wrapper = new MPJLambdaWrapper<>();
        wrapper.selectAll(RoleInfoDto.class)
                .selectAs(UserInfoDto::getNickName, RoleInfoVo::getCreateUserName)
                .leftJoin(UserInfoDto.class, UserInfoDto::getId, RoleInfoDto::getCreateUserId)
                .eq(StrUtil.isNotBlank(vo.getRoleName()), RoleInfoDto::getRoleName, vo.getRoleName())
                .eq(StrUtil.isNotBlank(vo.getRoleCode()), RoleInfoDto::getRoleCode, vo.getRoleCode())
                .eq(vo.getState() != null, RoleInfoDto::getState, vo.getState());
        return wrapper;
    }

    public static LambdaQueryWrapper<MenuRoleRelDto> queryMenuListByRoleId(Long id) {
        LambdaQueryWrapper<MenuRoleRelDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(MenuRoleRelDto::getMenuId)
                .eq(MenuRoleRelDto::getRoleId, id);
        return wrapper;
    }

    public static LambdaQueryWrapper<MenuInfoDto> getAllMenus() {
        LambdaQueryWrapper<MenuInfoDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
                MenuInfoDto::getId,
                MenuInfoDto::getParentId,
                MenuInfoDto::getMenuTitle
        );
        return wrapper;
    }

    public static LambdaUpdateWrapper<RoleInfoDto> updateRole(RoleInfoVo vo) {
        LambdaUpdateWrapper<RoleInfoDto> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(StrUtil.isNotBlank(vo.getRoleName()), RoleInfoDto::getRoleName, vo.getRoleName())
                .set(StrUtil.isNotBlank(vo.getRoleCode()), RoleInfoDto::getRoleCode, vo.getRoleCode())
                .set(vo.getState() != null, RoleInfoDto::getState, vo.getState())
                .set(RoleInfoDto::getUpdateUserId, vo.getUpdateUserId())
                .set(RoleInfoDto::getUpdateTime, vo.getUpdateTime())
                .eq(RoleInfoDto::getId, vo.getId());
        return wrapper;
    }

    public static LambdaQueryWrapper<MenuRoleRelDto> deleteByRoleId(RoleInfoVo vo) {
        LambdaQueryWrapper<MenuRoleRelDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MenuRoleRelDto::getRoleId, vo.getId());
        return wrapper;
    }

    public static LambdaUpdateWrapper<RoleInfoDto> changeRoleState(RoleInfoVo vo) {
        LambdaUpdateWrapper<RoleInfoDto> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(RoleInfoDto::getState, vo.getState())
                .set(RoleInfoDto::getUpdateUserId, vo.getUpdateUserId())
                .set(RoleInfoDto::getUpdateTime, vo.getUpdateTime())
                .eq(RoleInfoDto::getId, vo.getId());
        return wrapper;
    }

    public static LambdaQueryWrapper<RoleInfoDto> deleteRoleById(Long id) {
        return new LambdaQueryWrapper<RoleInfoDto>().eq(RoleInfoDto::getId, id);
    }

    public static LambdaQueryWrapper<RoleInfoDto> getAllRole(){
        return new LambdaQueryWrapper<RoleInfoDto>().select(RoleInfoDto::getId, RoleInfoDto::getRoleName,RoleInfoDto::getState);
    }

    public static MPJLambdaWrapper<UserInfoDto> getUserList(UserInfoVo vo) {
        MPJLambdaWrapper<UserInfoDto> wrapper = new MPJLambdaWrapper<>();
        wrapper.select(
                UserInfoDto::getId,
                        UserInfoDto::getUserName,
                        UserInfoDto::getNickName,
                        UserInfoDto::getCreateTime,
                        UserInfoDto::getEmail,
                        UserInfoDto::getPhone,
                        UserInfoDto::getState)
                .selectAs("u", UserInfoDto::getNickName, UserInfoVo::getCreateUserName)
                .leftJoin(UserInfoDto.class, "u", UserInfoDto::getId, UserInfoDto::getCreateUserId)
                .eq(StrUtil.isNotBlank(vo.getUserName()), UserInfoDto::getUserName, vo.getUserName())
                .eq(StrUtil.isNotBlank(vo.getPhone()),UserInfoDto::getPhone, vo.getPhone())
                .eq(vo.getState()!=null,UserInfoDto::getState, vo.getState());
        return wrapper;
    }

    public static LambdaUpdateWrapper<UserInfoDto> changeUserState(UserInfoVo vo) {
        LambdaUpdateWrapper<UserInfoDto> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(UserInfoDto::getState, vo.getState())
                .set(UserInfoDto::getUpdateUserId, vo.getUpdateUserId())
                .set(UserInfoDto::getUpdateTime, vo.getUpdateTime())
                .eq(UserInfoDto::getId, vo.getId());
        return wrapper;
    }

    public static LambdaQueryWrapper<UserRoleRelDto> getRolesByUserId(Long userId) {
        LambdaQueryWrapper<UserRoleRelDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(UserRoleRelDto::getRoleId)
                .eq(UserRoleRelDto::getUserId, userId);
        return wrapper;
    }

    public static LambdaQueryWrapper<UserRoleRelDto> deleteUserRole(List<Long> collect, Long userId) {
        LambdaQueryWrapper<UserRoleRelDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(UserRoleRelDto::getRoleId, collect)
                .eq(UserRoleRelDto::getUserId, userId);
        return wrapper;
    }

    public static LambdaUpdateWrapper<UserInfoDto> updateUser(UserInfoVo vo) {
        LambdaUpdateWrapper<UserInfoDto> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(StrUtil.isNotBlank(vo.getNickName()), UserInfoDto::getNickName, vo.getNickName())
                .set(StrUtil.isNotBlank(vo.getPhone()), UserInfoDto::getPhone, vo.getPhone())
                .set(StrUtil.isNotBlank(vo.getEmail()), UserInfoDto::getEmail, vo.getEmail())
                .set(StrUtil.isNotBlank(vo.getPassword()),UserInfoDto::getPassword, vo.getPassword())
                .set(vo.getState() != null, UserInfoDto::getState, vo.getState())
                .set(UserInfoDto::getUpdateUserId, vo.getUpdateUserId())
                .set(UserInfoDto::getUpdateTime, vo.getUpdateTime())
                .eq(UserInfoDto::getId, vo.getId());
        return wrapper;
    }

    public static LambdaQueryWrapper<UserInfoDto> resetPwdQueryUserInfo(Long userId) {
        LambdaQueryWrapper<UserInfoDto> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(UserInfoDto::getId, UserInfoDto::getUserName, UserInfoDto::getSalt).eq(UserInfoDto::getId, userId);
        return wrapper;
    }

}

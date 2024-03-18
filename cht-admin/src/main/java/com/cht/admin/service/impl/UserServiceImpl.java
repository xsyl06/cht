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
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cht.admin.mapper.WrapperFactory;
import com.cht.admin.pojo.BasePageVo;
import com.cht.admin.pojo.LoginUserInfo;
import com.cht.admin.pojo.system.UserInfoVo;
import com.cht.admin.service.BaseService;
import com.cht.admin.service.IUserService;
import com.cht.enums.ReturnEnum;
import com.cht.mp.pojo.database.UserInfoDto;
import com.cht.mp.pojo.database.UserRoleRelDto;
import com.cht.mp.service.UserRoleRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
/**
 * @description: 用户管理service实现类
 * @author Wang
 * @date 2024/3/18 11:26
 * @version 1.0
 */
@Service
@Slf4j
public class UserServiceImpl extends BaseService implements IUserService {
    /**
     * mybatis-plus的service类，用户实现批量插入
     */
    private final UserRoleRelService userRoleRelService;
    @Autowired
    public UserServiceImpl(UserRoleRelService userRoleRelService) {
        this.userRoleRelService = userRoleRelService;
    }

    /**
     * 分页获取用户信息
     * @param vo 登录名、手机号、用户状态
     * @return 用户信息列表
     */
    @Override
    public BasePageVo<UserInfoVo> getUserInfo(UserInfoVo vo) {
        IPage<UserInfoVo> page = userInfoMapper.selectJoinPage(
                new Page<>(vo.getCurrentPage(), vo.getPageSize()),
                UserInfoVo.class,
                WrapperFactory.getUserList(vo));
        BasePageVo<UserInfoVo> basePageVo = new BasePageVo<>();
        basePageVo.setTotal(page.getTotal());
        basePageVo.setList(page.getRecords());
        // 调用mapper查询数据
        return basePageVo;
    }

    /**
     * 根据用户id修改用户状态
     * @param vo 用户id
     * @return 是否修改成功
     */
    @Override
    public boolean changeUserState(UserInfoVo vo) {
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        vo.setUpdateUserId(loginUserInfo.getId());
        vo.setUpdateTime(new Date(System.currentTimeMillis()));
        int update = userInfoMapper.update(WrapperFactory.changeUserState(vo));
        return update > 0;
    }

    /**
     * 新增用户信息(同时批量保存用户与角色关联关系)
     * @param vo 用户信息
     * @return 是否新增成功
     */
    @Override
    @Transactional
    public boolean saveUser(UserInfoVo vo) {
        log.info("service层开始新增用户[{}]",vo.getNickName());
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        vo.setCreateUserId(loginUserInfo.getId());
        vo.setCreateTime(new Date(System.currentTimeMillis()));
        vo.setPassword(getDefaultPwd(vo));
        encodePwd(vo);
        int insert = userInfoMapper.insert(vo);
        boolean userAddFlag = insert > 0;
        //保存角色信息
        if(userAddFlag){
            Long userId = vo.getId();
            List<UserRoleRelDto> collection = vo.getRoleList().stream().map(roleId->{
                UserRoleRelDto dto = new UserRoleRelDto();
                dto.setUserId(userId);
                dto.setRoleId(roleId);
                dto.setCreateUserId(loginUserInfo.getId());
                dto.setCreateTime(new Date(System.currentTimeMillis()));
                return dto;
            }).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collection)) {
                userRoleRelService.saveBatch(collection);
            }
        }
        return userAddFlag;
    }

    /**
     * 根据用户id获取用户拥有的角色
     * @param userId 用户id
     * @return 角色列表
     */
    @Override
    public List<Long> getUserRoleList(Long userId){
        log.info("获取用户id[{}]的角色列表", userId);
        List<UserRoleRelDto> userRoleRelDtos = userRoleRelMapper.selectList(WrapperFactory.getRolesByUserId(userId));
        List<Long> list = null;
        if (CollectionUtil.isNotEmpty(userRoleRelDtos)) {
            list = userRoleRelDtos.stream().map(UserRoleRelDto::getRoleId).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 更新用户，同时更新用户的角色
     * @param vo 用户信息
     * @return
     */
    @Override
    @Transactional
    public boolean updateUser(UserInfoVo vo) {
        log.info("service层开始更新用户[{}]", vo.getId());
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        vo.setUpdateUserId(loginUserInfo.getId());
        vo.setUpdateTime(new Date(System.currentTimeMillis()));
        int update = userInfoMapper.update(WrapperFactory.updateUser(vo));
        if (CollectionUtil.isNotEmpty(vo.getRoleList())) {
            //查询数据
            List<UserRoleRelDto> userRoleRelDtos = userRoleRelMapper.selectList(WrapperFactory.getRolesByUserId(vo.getId()));
            if (CollectionUtil.isNotEmpty(userRoleRelDtos)) {
                //更新用户对应的角色信息，利用set做差集
                Set<Long> collect = userRoleRelDtos.stream().map(UserRoleRelDto::getRoleId).collect(Collectors.toSet());
                Set<Long> htmlCollect = new HashSet<>(vo.getRoleList());
                Set<Long> result = new HashSet<>(collect);
                result.removeAll(htmlCollect);
                //现在result中是需要删除的
                if (CollectionUtil.isNotEmpty(result)) {
                    userRoleRelMapper.delete(WrapperFactory.deleteUserRole(new ArrayList<>(result), vo.getId()));
                }
                result.clear();
                result.addAll(htmlCollect);
                result.removeAll(collect);
                //现在result中是需要新增的
                if (CollectionUtil.isNotEmpty(result)) {
                    List<UserRoleRelDto> collect1 = result.stream().map(a -> {
                        UserRoleRelDto userRoleRelDto = new UserRoleRelDto();
                        userRoleRelDto.setUserId(vo.getId());
                        userRoleRelDto.setRoleId(a);
                        userRoleRelDto.setCreateTime(new Date(System.currentTimeMillis()));
                        userRoleRelDto.setCreateUserId(loginUserInfo.getId());
                        return userRoleRelDto;
                    }).collect(Collectors.toList());
                    userRoleRelService.saveBatch(collect1);
                }
            }
        }
        return update > 0;

    }

    /**
     * 重置用户密码
     * @param userId 用户id
     * @return 是否重置成功
     */
    @Override
    public boolean resetPwd(Long userId) {
        log.info("重置用户密码，userId:{}", userId);
        UserInfoDto userInfoDto = userInfoMapper.selectOne(WrapperFactory.resetPwdQueryUserInfo(userId));
        if (ObjectUtil.isNotEmpty(userInfoDto)) {
            UserInfoVo vo = new UserInfoVo();
            BeanUtils.copyProperties(userInfoDto, vo);
            String defaultPwd = getDefaultPwd(vo);
            vo.setPassword(defaultPwd);
            encodePwd(vo);
            LoginUserInfo loginUserInfo = getLoginUserInfo();
            vo.setUpdateTime(new Date(System.currentTimeMillis()));
            vo.setUpdateUserId(loginUserInfo.getId());
            int update = userInfoMapper.update(WrapperFactory.updateUser(vo));
            return update > 0;
        }else {
            return false;
        }
    }

    /**
     * 修改密码
     * @param vo 用户id、旧密码、新密码
     * @return 是否修改成功
     */
    @Override
    public ReturnEnum changePwd(UserInfoVo vo) {
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        log.info("开始修改用户[{}]密码",loginUserInfo.getId());
        //先从数据库查询用户信息
        UserInfoDto userInfoDto = userInfoMapper.selectOne(WrapperFactory.resetPwdQueryUserInfo(loginUserInfo.getId()));
        //判断该用户是否被禁用
        if(userInfoDto.getState()){
            //判断输入的原密码是否一致
            if (verifyPwd(vo.getOldPwd(), userInfoDto)) {
                vo.setSalt(userInfoDto.getSalt());
                //进行密码加密
                encodePwd(vo);
                vo.setId(loginUserInfo.getId());
                //更新密码
                int update = userInfoMapper.update(WrapperFactory.updateUserPwd(vo));
                if (update > 0) {
                    return ReturnEnum.SUCCESS;
                }else {
                    return ReturnEnum.UPDATE_USER_PWD_FAIL;
                }
            }else{
                return ReturnEnum.TWO_PSW_DIFF;
            }
        }else{
            return ReturnEnum.USER_STATE_FAIL;
        }
    }
}

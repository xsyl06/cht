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

@Service
@Slf4j
public class UserServiceImpl extends BaseService implements IUserService {
    @Autowired
    private UserRoleRelService userRoleRelService;
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

    @Override
    public boolean changeUserState(UserInfoVo vo) {
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        vo.setUpdateUserId(loginUserInfo.getId());
        vo.setUpdateTime(new Date(System.currentTimeMillis()));
        int update = userInfoMapper.update(WrapperFactory.changeUserState(vo));
        return update > 0;
    }

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
}

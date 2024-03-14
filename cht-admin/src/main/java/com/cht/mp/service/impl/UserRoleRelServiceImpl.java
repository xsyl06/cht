package com.cht.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cht.mp.pojo.database.UserRoleRelDto;
import com.cht.mp.service.UserRoleRelService;
import com.cht.mp.mapper.UserRoleRelMapper;
import org.springframework.stereotype.Service;

/**
* @author Wang
* @description 针对表【system_user_role_rel(用户角色关联关系表)】的数据库操作Service实现
* @createDate 2024-03-01 17:03:36
*/
@Service
public class UserRoleRelServiceImpl extends ServiceImpl<UserRoleRelMapper, UserRoleRelDto>
    implements UserRoleRelService{

}





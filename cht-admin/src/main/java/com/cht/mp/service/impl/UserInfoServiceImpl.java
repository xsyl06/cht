package com.cht.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cht.mp.pojo.database.UserInfoDto;
import com.cht.mp.service.UserInfoService;
import com.cht.mp.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Wang
* @description 针对表【system_user_info(系统用户表)】的数据库操作Service实现
* @createDate 2024-03-01 17:03:36
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoDto>
    implements UserInfoService{

}





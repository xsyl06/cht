package com.cht.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cht.mp.pojo.database.RoleInfoDto;
import com.cht.mp.service.RoleInfoService;
import com.cht.mp.mapper.RoleInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Wang
* @description 针对表【system_role_info(系统角色表)】的数据库操作Service实现
* @createDate 2024-03-01 17:03:36
*/
@Service
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfoDto>
    implements RoleInfoService{

}





package com.cht.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cht.mp.pojo.database.MenuInfoDto;
import com.cht.mp.service.MenuInfoService;
import com.cht.mp.mapper.MenuInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Wang
* @description 针对表【system_menu_info(系统菜单表)】的数据库操作Service实现
* @createDate 2024-03-01 17:03:35
*/
@Service
public class MenuInfoServiceImpl extends ServiceImpl<MenuInfoMapper, MenuInfoDto>
    implements MenuInfoService{

}





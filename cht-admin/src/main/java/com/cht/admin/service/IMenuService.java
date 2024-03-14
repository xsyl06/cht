package com.cht.admin.service;

import com.cht.mp.pojo.database.MenuInfoDto;

import java.util.List;

public interface IMenuService {
    List<MenuInfoDto> getMenuList();
    boolean saveMenu(MenuInfoDto input);

    boolean updateMenu(MenuInfoDto input);

    boolean deleteMenu(Long id);
}

package com.cht.admin.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.cht.enums.MenuTypeEnum;
import com.cht.admin.mapper.WrapperFactory;
import com.cht.admin.pojo.LoginUserInfo;
import com.cht.admin.pojo.R;
import com.cht.admin.pojo.RoutesVo;
import com.cht.admin.service.BaseService;
import com.cht.admin.service.ILoginService;
import com.cht.mp.pojo.database.MenuInfoDto;
import com.cht.mp.pojo.database.UserInfoDto;
import com.cht.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.cht.enums.ReturnEnum.*;
import static com.cht.utils.Constants.LOGIN_USER;
import static com.cht.utils.Constants.SYSTEM_REDIS_KEY_PREFIX;

@Service
@Slf4j
public class LoginServiceImpl extends BaseService implements ILoginService {
    @Override
    public R login(String username, String password) {
        log.info("[{}]开始登录", username);
        //先查询数据库中是否存在登录用户名信息的数据
        UserInfoDto userInfoDto = userInfoMapper.selectOne(WrapperFactory.queryLoginUserWrapper(username));
        R r;
        if (ObjectUtil.isNotEmpty(userInfoDto) && verifyPwd(password, userInfoDto)) {
            //当不为空，则表示存在该登录用户名信息的用户
            //密码校验返回true，则表示验证通过
            log.info("[{}]密码校验通过", username);
            StpUtil.login(userInfoDto.getId());
            log.info("[{}]登录成功", username);
            //创建登录用户信息,该信息会保存在redis中
            LoginUserInfo loginUserInfo = BeanUtil.copyProperties(userInfoDto, LoginUserInfo.class);
            loginUserInfo.setAccessToken(StpUtil.getTokenValue());
            loginUserInfo.setExpires(DateUtil.current()+StpUtil.getTokenActiveTimeout()*1000);
            log.info("expires:{}", DateUtil.date(loginUserInfo.getExpires()).toString(DatePattern.NORM_DATETIME_MS_PATTERN));
            //根据id查询用户角色code列表
            List<String> list = userRoleRelMapper.selectJoinList(String.class, WrapperFactory.queryRoleListByUserId(userInfoDto.getId()));
            loginUserInfo.setRoles(list);
            //生成refreshToken用来续主token
            String refreshKey = redisUtils.getKey(Constants.SYSTEM_REDIS_KEY_PREFIX,
                    Constants.REFRESH_KEY, RandomUtil.randomString(32));
            redisUtils.setString(refreshKey, StpUtil.getTokenValue(), REFRESH_TIMEOUT, TimeUnit.DAYS);
            loginUserInfo.setRefreshToken(refreshKey);
            //相关密码信息不返回前端
            loginUserInfo.setPassword(null);
            loginUserInfo.setSalt(null);
            //处理登录人员权限相关信息
            List<String> permissions = menuRoleRelMapper.selectJoinList(String.class, WrapperFactory.queryLoginUserPermissions(userInfoDto.getId()));
            List<String> collect = permissions.stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
            String loginUserKey = redisUtils.getKey(SYSTEM_REDIS_KEY_PREFIX, LOGIN_USER, StpUtil.getTokenValue());
            loginUserInfo.setAuths(collect);
            redisUtils.setString(loginUserKey, JSON.toJSONString(loginUserInfo), REFRESH_TIMEOUT, TimeUnit.DAYS);
            r = R.SUCCESS(loginUserInfo);
        } else {
            //数据库没有和密码错误均返回相同的错误信息，避免暴力破解
            r = R.FAIL(USER_OR_PWD_ERROR);
        }
        return r;
    }

    @Override
    public R getAsyncRoutes() {
        //获取当前登录用户信息
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        //获取当前登录用户角色code列表
        List<String> roleCodes = loginUserInfo.getRoles();
        //获取当前登录用户角色code对应的菜单信息
        List<MenuInfoDto> menuInfoDtos = menuInfoMapper.selectJoinList(MenuInfoDto.class, WrapperFactory.queryMenuListByRoleCode(roleCodes));
        //处理查询列表为路由树
        List<RoutesVo> list = new ArrayList<>();
        log.info(JSON.toJSONString(menuInfoDtos));
        routesHandler(menuInfoDtos, list);
        return R.SUCCESS(list);
    }
    @Override
    public R refreshToken(LoginUserInfo input) {
        log.info("开始刷新token");
        String refresh = redisUtils.getString(input.getRefreshToken());
        if (StrUtil.isNotBlank(refresh)) {
            StpUtil.stpLogic.updateLastActiveToNow(refresh);
            input.setAccessToken(refresh);
            input.setExpires(DateUtil.current()+StpUtil.getTokenActiveTimeout()*1000);
            log.info("expires:{}", DateUtil.date(input.getExpires()).toString(DatePattern.NORM_DATETIME_MS_PATTERN));
        }else{
            return R.FAIL(NOT_TOKEN);
        }
        LoginUserInfo loginUserInfo = getLoginUserInfo();
        log.info("{}刷新token:[{}]成功",loginUserInfo.getUserName(),StpUtil.getTokenValue());
        return R.SUCCESS(input);
    }


    public void routesHandler(List<MenuInfoDto> menuInfoDtos, List<RoutesVo> list) {
        //设置两级缓存 保存已经处理过的菜单 和保存父菜单还没处理的子菜单的集合
        //已经处理过的菜单缓存，key为菜单id
        Map<Long, RoutesVo> alreadyHandlerMenuMap = new HashMap<>(menuInfoDtos.size());
        //父菜单还没处理的子菜单的集合,key为父菜单的id
        Map<Long,List<RoutesVo>> childMenuMap = new HashMap<>();
        //菜单中按钮权限列表缓存
        Map<Long, List<String>> menuPermissionMap = new HashMap<>();
        for (MenuInfoDto menuInfoDto : menuInfoDtos) {
            //判断menu类型进行处理
            if (menuInfoDto.getMenuType() == MenuTypeEnum.BUTTON.getType()) {
                authsHandler(menuInfoDto, alreadyHandlerMenuMap, menuPermissionMap);
            }else if (menuInfoDto.getMenuType() == MenuTypeEnum.IFRAME.getType()
                    ||menuInfoDto.getMenuType() == MenuTypeEnum.LINK.getType()){
                linkAndIframHandler(menuInfoDto, alreadyHandlerMenuMap, childMenuMap);
            }else{
                menuHandler(list, menuInfoDto, childMenuMap, alreadyHandlerMenuMap, menuPermissionMap);
            }
        }
    }

    /**
     * 菜单处理方法
     * @param list 返回前端的菜单列表
     * @param menuInfoDto 菜单信息
     * @param childMenuMap 父菜单还没处理的子菜单的集合,key为父菜单的id
     * @param alreadyHandlerMenuMap 已经处理过的菜单缓存，key为菜单id
     * @param menuPermissionMap 菜单中按钮权限列表缓存
     */
    private void menuHandler(List<RoutesVo> list, MenuInfoDto menuInfoDto, Map<Long, List<RoutesVo>> childMenuMap, Map<Long, RoutesVo> alreadyHandlerMenuMap, Map<Long, List<String>> menuPermissionMap) {
        //先判断是否是顶级菜单
        if (menuInfoDto.getParentId() == null) {
            //是顶级菜单，直接添加到list中
            RoutesVo routesVo = new RoutesVo();
            routesVo.setPath(menuInfoDto.getMenuPath());
            RoutesVo.Meta meta = new RoutesVo.Meta();
            meta.setTitle(menuInfoDto.getMenuTitle());
            meta.setRank(menuInfoDto.getRank());
            meta.setKeepAlive(menuInfoDto.getKeepAlive());
            meta.setShowLink(menuInfoDto.getShowLink());
            meta.setShowParent(menuInfoDto.getShowParent());
            if (StrUtil.isNotBlank(menuInfoDto.getIcon())) {
                meta.setIcon(menuInfoDto.getIcon());
            }
            routesVo.setMeta(meta);
            if (childMenuMap.containsKey(menuInfoDto.getId())) {
                routesVo.setChildren(childMenuMap.get(menuInfoDto.getId()));
                childMenuMap.remove(menuInfoDto.getId());
            }
            alreadyHandlerMenuMap.put(menuInfoDto.getId(), routesVo);
            list.add(routesVo);
        }else{
            //处理自己
            RoutesVo routesVo = new RoutesVo();
            routesVo.setPath(menuInfoDto.getMenuPath());
            routesVo.setName(menuInfoDto.getMenuName());
            RoutesVo.Meta meta = new RoutesVo.Meta();
            meta.setTitle(menuInfoDto.getMenuTitle());
            meta.setRank(menuInfoDto.getRank());
            meta.setKeepAlive(menuInfoDto.getKeepAlive());
            meta.setShowLink(menuInfoDto.getShowLink());
            meta.setShowParent(menuInfoDto.getShowParent());
            if (StrUtil.isNotBlank(menuInfoDto.getIcon())) {
                meta.setIcon(menuInfoDto.getIcon());
            }
            if (StrUtil.isNotBlank(meta.getExtraIcon())) {
                meta.setExtraIcon(meta.getExtraIcon());
            }
            routesVo.setMeta(meta);
            //检查子菜单列表是否处理
            if (childMenuMap.containsKey(menuInfoDto.getId())) {
                routesVo.setChildren(childMenuMap.get(menuInfoDto.getId()));
                childMenuMap.remove(menuInfoDto.getId());
            }
            //检查按钮权限是否处理
            if (menuPermissionMap.containsKey(menuInfoDto.getId())) {
                routesVo.getMeta().setAuths(menuPermissionMap.get(menuInfoDto.getId()));
                menuPermissionMap.remove(menuInfoDto.getId());
            }
            //检查父类菜单是否处理过
            if (alreadyHandlerMenuMap.containsKey(menuInfoDto.getParentId())) {
                RoutesVo parentRoutesVo = alreadyHandlerMenuMap.get(menuInfoDto.getParentId());
                if (CollectionUtil.isNotEmpty(parentRoutesVo.getChildren())) {
                    parentRoutesVo.getChildren().add(routesVo);
                }else {
                    List<RoutesVo> childList = new ArrayList<>();
                    childList.add(routesVo);
                    parentRoutesVo.setChildren(childList);
                }
            }else{
                //父类菜单未处理,放入子菜单的集合
                if (childMenuMap.containsKey(menuInfoDto.getParentId())) {
                    childMenuMap.get(menuInfoDto.getParentId()).add(routesVo);
                }else {
                    List<RoutesVo> childList = new ArrayList<>();
                    childList.add(routesVo);
                    childMenuMap.put(menuInfoDto.getParentId(), childList);
                }
            }
            alreadyHandlerMenuMap.put(menuInfoDto.getId(), routesVo);
        }
    }

    /**
     * 外链和iframe处理方法
     * @param menuInfoDto 菜单信息
     * @param alreadyHandlerMenuMap 已经处理过的菜单缓存，key为菜单id
     * @param childMenuMap 父菜单还没处理的子菜单的集合,key为父菜单的id
     */
    private  void linkAndIframHandler(MenuInfoDto menuInfoDto, Map<Long, RoutesVo> alreadyHandlerMenuMap, Map<Long, List<RoutesVo>> childMenuMap) {
        //先处理下自己
        RoutesVo routesVo = new RoutesVo();
        routesVo.setPath(menuInfoDto.getMenuPath());
        routesVo.setName(menuInfoDto.getMenuName());
        RoutesVo.Meta meta = new RoutesVo.Meta();
        meta.setTitle(menuInfoDto.getMenuTitle());
        meta.setRank(menuInfoDto.getRank());
        meta.setKeepAlive(menuInfoDto.getKeepAlive());
        meta.setShowLink(menuInfoDto.getShowLink());
        meta.setShowParent(menuInfoDto.getShowParent());
        if (StrUtil.isNotBlank(menuInfoDto.getIcon())) {
            meta.setIcon(menuInfoDto.getIcon());
        }
        if (StrUtil.isNotBlank(meta.getExtraIcon())) {
            meta.setExtraIcon(meta.getExtraIcon());
        }
        meta.setFrameSrc(menuInfoDto.getFrameSrc());
        routesVo.setMeta(meta);
        //检查父类菜单是否处理
        if (alreadyHandlerMenuMap.containsKey(menuInfoDto.getParentId())) {
            //已经处理自己接着挂上去
            RoutesVo parentRoutesVo = alreadyHandlerMenuMap.get(menuInfoDto.getParentId());
            if (CollectionUtil.isNotEmpty(parentRoutesVo.getChildren())) {
                parentRoutesVo.getChildren().add(routesVo);
            }else {
                List<RoutesVo> childList = new ArrayList<>();
                childList.add(routesVo);
                parentRoutesVo.setChildren(childList);
            }

        } else {
            //父类菜单未处理,放入子菜单的集合
            if (childMenuMap.containsKey(menuInfoDto.getParentId())) {
                childMenuMap.get(menuInfoDto.getParentId()).add(routesVo);
            }else {
                List<RoutesVo> childList = new ArrayList<>();
                childList.add(routesVo);
                childMenuMap.put(menuInfoDto.getParentId(), childList);
            }
        }
    }

    /**
     * 菜单内按钮权限处理方法
     * @param menuInfoDto 菜单信息
     * @param alreadyHandlerMenuMap 已经处理过的菜单缓存，key为菜单id
     * @param menuPermissionMap 菜单中按钮权限列表缓存
     */
    private void authsHandler(MenuInfoDto menuInfoDto,
                              Map<Long, RoutesVo> alreadyHandlerMenuMap,
                              Map<Long, List<String>> menuPermissionMap) {
        RoutesVo parent = alreadyHandlerMenuMap.get(menuInfoDto.getParentId());
        //判断父类是否处理过
        if (ObjectUtil.isNotEmpty(parent)) {
            //父类已经处理过，将该按钮权限加入页面的权限列表
            List<String> auths = new ArrayList<>();
            auths.add(menuInfoDto.getAuths());
            parent.getMeta().setAuths(auths);
        }else{
            //父菜单未处理过，检擦按钮权限列表缓存是否存在
            if (menuPermissionMap.containsKey(menuInfoDto.getParentId())) {
                menuPermissionMap.get(menuInfoDto.getParentId()).add(menuInfoDto.getAuths());
            }else{
                List<String> auths = new ArrayList<>();
                auths.add(menuInfoDto.getAuths());
                menuPermissionMap.put(menuInfoDto.getParentId(), auths);
            }
        }
    }
}

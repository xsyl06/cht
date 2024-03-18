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

package com.cht.admin.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.SM2;
import com.alibaba.fastjson2.JSON;
import com.cht.admin.pojo.LoginUserInfo;
import com.cht.admin.pojo.system.UserInfoVo;
import com.cht.mp.mapper.*;
import com.cht.mp.pojo.database.DictDto;
import com.cht.mp.pojo.database.UserInfoDto;
import com.cht.utils.DictUtils;
import com.cht.utils.RedisUtils;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import static com.cht.utils.Constants.LOGIN_USER;
import static com.cht.utils.Constants.SYSTEM_REDIS_KEY_PREFIX;
/**
 * @description: 基础service类 主要用于引入mapper，使得继承类不需要再重复引入
 * @author Wang
 * @date 2024/3/15 15:15
 * @version 1.0
 */
@Service
@Slf4j
public class BaseService {
    @Autowired
    protected UserInfoMapper userInfoMapper;
    @Autowired
    protected MenuInfoMapper menuInfoMapper;
    @Autowired
    protected RoleInfoMapper roleInfoMapper;
    @Autowired
    protected UserRoleRelMapper userRoleRelMapper;
    @Autowired
    protected MenuRoleRelMapper menuRoleRelMapper;
    @Autowired
    protected DictMapper dictMapper;
    @Autowired
    protected RedisUtils redisUtils;
    @Autowired
    protected DictUtils dictUtils;
    /**
     * 刷新token的redis超时时间
     */
    protected static final long REFRESH_TIMEOUT = 1l;
    /**
     * 默认密码的前缀
     */
    @Value("${env.pwdPre}")
    private String pwdPre;
    /**
     * 默认密码生成的策略
     */
    @Value("${env.default-pwd-policy}")
    private int defaultPwdPolicy;
    SM2 sm2 = null;

    /**
     * 在spring依赖注入之后，初始化该bean时生成sm2加密对象
     */
    @PostConstruct
    public void init() {
        if (ObjectUtil.isEmpty(sm2)) {
            DictDto keyPair = dictUtils.getKeyPair();
            sm2 = SmUtil.sm2(keyPair.getDictValue2(), keyPair.getDictValue3());
        }
    }

    /**
     * 校验用户密码
     *
     * @param password    前端传入的密码
     * @param userInfoDto 查询数据库获得的用户对象
     * @return true-校验通过，false-校验失败
     */
    protected boolean verifyPwd(String password, UserInfoDto userInfoDto) {
        log.info("校验用户[{}]密码", userInfoDto.getUserName());
        String tmp = password + userInfoDto.getSalt();
        return sm2.verify(tmp.getBytes(StandardCharsets.UTF_8), HexUtil.decodeHex(userInfoDto.getPassword()));
    }

    /**
     * 加密用户密码
     * @param userInfoDto 用户信息，主要需要用户的盐和明文密码，执行该方法后userInfoDto的密码会被加密
     */
    protected void encodePwd(UserInfoDto userInfoDto) {
        //生成盐（如果对象中没有，则新增一个16位的盐，如果有，使用对象中的）
        String salt;
        if (StrUtil.isBlank(userInfoDto.getSalt())) {
            salt = RandomUtil.randomString(16);
            userInfoDto.setSalt(salt);
        } else {
            salt = userInfoDto.getSalt();
        }
        String tmpPwd = userInfoDto.getPassword() + salt;
        byte[] signByte = sm2.sign(tmpPwd.getBytes(StandardCharsets.UTF_8));
        String sign = HexUtil.encodeHexStr(signByte);
        userInfoDto.setPassword(sign);
    }

    /**
     * 获取默认密码
     * @param vo 用户信息
     * @return 默认密码
     */
    protected String getDefaultPwd(UserInfoVo vo) {
        if (defaultPwdPolicy == 2) {
            return pwdPre + vo.getPhone().substring(-4);
        }
        return pwdPre + vo.getUserName();
    }

    /**
     * 获取登录用户信息(会从redis中获取，如果没获取到则会返回null)
     * @return 登录用户信息
     */
    protected LoginUserInfo getLoginUserInfo() {
        String loginUserKey = redisUtils.getKey(SYSTEM_REDIS_KEY_PREFIX, LOGIN_USER, StpUtil.getTokenValue());
        String loginUserStr = redisUtils.getString(loginUserKey);
        if (StrUtil.isBlank(loginUserStr)) {
            return null;
        }else{
            return JSON.parseObject(loginUserStr, LoginUserInfo.class);
        }
    }

}

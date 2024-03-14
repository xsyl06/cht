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
    protected static final long REFRESH_TIMEOUT = 1l;
    @Value("${env.pwdPre}")
    private String pwdPre;
    @Value("${env.default-pwd-policy}")
    private int defaultPwdPolicy;
    SM2 sm2 = null;

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

    protected String getDefaultPwd(UserInfoVo vo) {
        if (defaultPwdPolicy == 2) {
            return pwdPre + vo.getPhone().substring(-4);
        }
        return pwdPre + vo.getUserName();
    }

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

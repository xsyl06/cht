package com.cht.config;

import cn.dev33.satoken.dao.SaTokenDaoRedisFastjson2;
import cn.dev33.satoken.dao.alone.SaAloneRedisInject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class LettuceConnectionValidConfig implements InitializingBean {

    public SaAloneRedisInject alone;
    @Autowired
    public LettuceConnectionValidConfig(SaAloneRedisInject alone) {
        this.alone = alone;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        SaTokenDaoRedisFastjson2 dao = (SaTokenDaoRedisFastjson2) alone.saTokenDao;
        RedisConnectionFactory connectionFactory = dao.stringRedisTemplate.getConnectionFactory();
        if (connectionFactory instanceof LettuceConnectionFactory c) {
            log.info("设置lettuce检查链接完成");
            c.setValidateConnection(true);
        }

    }
}

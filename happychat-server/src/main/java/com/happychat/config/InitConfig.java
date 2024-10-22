package com.happychat.config;

import com.happychat.utils.RedisUtils;
import com.happychat.websocket.netty.NettyWebSocketServer;
import org.redisson.client.RedisException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;

@Component("initConfig")
public class InitConfig implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(InitConfig.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private NettyWebSocketServer nettyWebSocketServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            dataSource.getConnection();
            redisUtils.get("test");
            new Thread(nettyWebSocketServer).start();
            logger.info("服务启动成功");
        } catch (SQLException e) {
            logger.error("数据库连接异常");
        } catch (RedisConnectionFailureException e) {
            e.printStackTrace();
            logger.error("redis连接异常");
        } catch (Exception e) {
            logger.error("服务启动异常");
        }
    }
}

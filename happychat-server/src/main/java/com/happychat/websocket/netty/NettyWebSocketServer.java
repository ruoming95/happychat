package com.happychat.websocket.netty;

import com.happychat.config.AppConfig;
import com.happychat.utils.StringTools;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Component
public class NettyWebSocketServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NettyWebSocketServer.class);
    private static EventLoopGroup bossGroup = new NioEventLoopGroup();
    private static EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @PreDestroy
    public void cleanup() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    @Override
    public void run() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
//                            添加对http协议的支持，编码器、解码器
                            pipeline.addLast(new HttpServerCodec())
//                                    将接收到的多个 HTTP 消息片段（如请求行、请求头、请求体等）聚合成一个单一的 HttpObject
                                    .addLast(new HttpObjectAggregator(64 * 1024 * 1024))
                                    .addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS))
                                    .addLast(new HeartbeatHandler())
//                                    添加websocket支持
                                    .addLast(new WebSocketServerProtocolHandler("/ws", null, true, 64 * 1024, true, true, 10000L))
                                    .addLast(webSocketHandler);
                        }
                    });
            Integer wsPort = appConfig.getWsPort();
            String wsPortStr = System.getProperty("ws.port");
            if (!StringTools.isEmpty(wsPortStr)) {
                wsPort = Integer.valueOf(wsPortStr);
            }
            ChannelFuture channelFuture = bootstrap.bind(wsPort).sync();
            logger.info("netty启动成功");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("启动netty失败", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
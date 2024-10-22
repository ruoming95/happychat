package com.happychat.websocket.netty;

import com.happychat.entity.dto.TokenUserInfoDto;
import com.happychat.utils.RedisComponent;
import com.happychat.utils.StringTools;
import com.happychat.websocket.ChannelContextUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandler.class);

    @Autowired
    private RedisComponent redisComponent;

    @Autowired
    private ChannelContextUtils channelContextUtils;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info(ctx.channel().remoteAddress() + "上线了");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        channelContextUtils.removeLastConn(ctx.channel());
        logger.info(ctx.channel().remoteAddress() + "下线了");
    }

    //    接收心跳
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        Attribute<String> attr = channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId = attr.get();
        logger.info("用户{}发送: {}", userId, msg.text());
        redisComponent.saveHeartBeat(userId);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 握手完成事件
            WebSocketServerProtocolHandler.HandshakeComplete handshakeEvent =
                    (WebSocketServerProtocolHandler.HandshakeComplete) evt;

            // 获取 WebSocket URL
            String url = handshakeEvent.requestUri();
            String token = getToken(url);
            TokenUserInfoDto tokenUserInfoDto = redisComponent.getTokenUserInfoDto(token);
            if (tokenUserInfoDto == null) {
                ctx.channel().close();
                return;
            }
            //建立channel和userId的映射关系
            channelContextUtils.addContext(tokenUserInfoDto.getUserId(), ctx.channel());
        }
        super.userEventTriggered(ctx, evt);
    }

    private String getToken(String url) {
        if (StringTools.isEmpty(url) || url.indexOf("?") == -1) {
            return null;
        }
        String[] split = url.split("\\?");
        if (split.length != 2) {
            return null;
        }
        String[] split1 = split[1].split("=");
        return split1.length == 2 ? split1[1] : null;
    }
}

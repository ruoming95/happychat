package com.happychat.websocket.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                Channel channel = ctx.channel();
                Attribute<String> attr = channel.attr(AttributeKey.valueOf(channel.id().toString()));
                String userId = attr.get();
                logger.info("心跳超时，强制下线，userId:{}", userId);
            } else {
                ctx.writeAndFlush("pong");
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}


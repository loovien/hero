package com.hero.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * created 5/10/2021 5:07 PM
 *
 * @author luowen <loovien@163.com>
 */
@Slf4j
public class GameHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        log.info("receive from client: {} message: {}", channelHandlerContext.channel().remoteAddress(), o);
    }
}

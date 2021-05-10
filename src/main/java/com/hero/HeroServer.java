package com.hero;


import com.hero.handler.GameHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * created 5/10/2021 5:00 PM
 *
 * @author luowen <loovien@163.com>
 */
@Slf4j
public class HeroServer {

    public static void main(String[] args) {

        NioEventLoopGroup acceptorLoopGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup connLoopGroup = new NioEventLoopGroup(2);

        try {
            ChannelFuture channelFuture = new ServerBootstrap()
                    .group(acceptorLoopGroup, connLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(
                                    new HttpServerCodec(),
                                    new HttpObjectAggregator(65535),
                                    new WebSocketServerProtocolHandler("/websocket"),
                                    new GameHandler()
                            );
                        }
                    }).childOption(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .bind("127.0.0.1", 8888).sync();
            log.info("game server startup listen at: {}", "127.0.0.1:8888");
            channelFuture.channel().closeFuture().sync(); // wait
        } catch (InterruptedException e) {
            log.error("receive interrupter exception: {}", e.getMessage());
        } finally {
            acceptorLoopGroup.shutdownGracefully();
            acceptorLoopGroup.shutdownGracefully();
        }
    }
}

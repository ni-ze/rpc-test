package com.tencent.zeni.rpctest.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final Map<String, Channel> CHANNEL_MAPPING = new ConcurrentHashMap<>();

    public ServerBootstrap start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        //初始化server
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ServerInitializer());
        return  serverBootstrap;


    }

    public ChannelFuture bind(ServerBootstrap serverBootstrap, int port){
        return serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 绑定成功！");
            } else {
                System.out.println(new Date() + ": 绑定失败！");
            }
        });
    }

    public static Channel getChannel(String host) {
        return CHANNEL_MAPPING.get(host);
    }

    public static void putChannel(String host, Channel channel) {
        CHANNEL_MAPPING.put(host, channel);
    }
}

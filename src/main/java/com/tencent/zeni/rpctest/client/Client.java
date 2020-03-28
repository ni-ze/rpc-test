package com.tencent.zeni.rpctest.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.tencent.zeni.rpctest.util.CommonUtil.MAX_RETRY;

public class Client {

    public Bootstrap start() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ClientInitializer());

        return bootstrap;
    }

    public static ChannelFuture connect(Bootstrap bootstrap, String host, int port, int retry) throws InterruptedException {
        return bootstrap.connect(host, port).addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println(new Date() + ": 链接成功！");
                    } else if (retry == 0) {
                        System.err.println(" 重试次数已用完，放弃连接！");
                    } else {
                        int order = (MAX_RETRY - retry) + 1;
                        int delay = 1 << order;
                        bootstrap.group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
                    }
                });

    }
}

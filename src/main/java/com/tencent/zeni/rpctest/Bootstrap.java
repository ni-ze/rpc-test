package com.tencent.zeni.rpctest;

import com.tencent.zeni.rpctest.client.Client;
import com.tencent.zeni.rpctest.protocal.codec.PacketCodec;
import com.tencent.zeni.rpctest.protocal.packet.Model;
import com.tencent.zeni.rpctest.protocal.packet.MsgRepository;
import com.tencent.zeni.rpctest.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import static com.tencent.zeni.rpctest.util.CommonUtil.*;

public class Bootstrap {
    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServerBootstrap serverBootstrap = server.start();
        ChannelFuture serverChannelFuture = server.bind(serverBootstrap, PORT);
        serverChannelFuture.await();

        Client client = new Client();
        io.netty.bootstrap.Bootstrap clientBootstrap = client.start();
        ChannelFuture clientChannelFuture = Client.connect(clientBootstrap, HOST, PORT, MAX_RETRY);
        clientChannelFuture.await();



        for (int i = 0; i < COUNT_LEVEL_3; i++) {
            Model one = MsgRepository.getInstance().getZhangPacket(MSG_SESSION_ONE);
            Model two = MsgRepository.getInstance().getLiPacket(MSG_SESSION_TWO);
            Model three = MsgRepository.getInstance().getLiPacket(MSG_SESSION_THREE);

            sendMsg(Server.getChannel(HOST), one);
            sendMsg(clientChannelFuture.channel(), two);
            sendMsg(clientChannelFuture.channel(), three);
        }

    }

    private static void sendMsg(Channel channel, Model model) throws Exception {

        ByteBuf encode = PacketCodec.INSTANCE.encode(model);

        channel.writeAndFlush(encode);
    }
}

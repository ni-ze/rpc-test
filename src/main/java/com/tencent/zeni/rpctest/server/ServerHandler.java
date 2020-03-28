package com.tencent.zeni.rpctest.server;

import com.tencent.zeni.rpctest.protocal.codec.PacketCodec;
import com.tencent.zeni.rpctest.protocal.packet.Model;
import com.tencent.zeni.rpctest.protocal.packet.MsgRepository;
import com.tencent.zeni.rpctest.protocal.packet.PacketData;
import com.tencent.zeni.rpctest.util.MsgCounter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.tencent.zeni.rpctest.util.CommonUtil.*;


public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String remoteAddress = getRemoteAddress(ctx);

        System.out.println("CLIENT: "+remoteAddress+" 接入链接");

        Server.putChannel(getHost(ctx),ctx.channel());
    }

    private String getRemoteAddress(ChannelHandlerContext ctx){
        return ctx.channel().remoteAddress().toString().replace("/","");
    }

    private String getHost(ChannelHandlerContext ctx){
        String remoteAddress = getRemoteAddress(ctx);
        return remoteAddress.substring(0,remoteAddress.indexOf(":"));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        PacketData packetData = PacketCodec.INSTANCE.decode(byteBuf);
        if (! (packetData instanceof Model)){
            return;
        }
        Model decode = (Model) packetData;
        int session = decode.getSession();
        switch (session){
            case MSG_SESSION_ONE:
                MsgCounter.count();
                break;
            case MSG_SESSION_TWO: {
                MsgCounter.count();
                sendMsg(ctx, MSG_SESSION_TWO);
                break;
            }
            case MSG_SESSION_THREE: {
                MsgCounter.count();
                sendMsg(ctx,MSG_SESSION_THREE);
                break;
            }

            default: {
                break;
            }
        }
    }


    private void sendMsg(ChannelHandlerContext ctx, int session) throws Exception{
        Model zhangPacket = MsgRepository.getInstance().getZhangPacket(session);
        ByteBuf byteBuf = PacketCodec.INSTANCE.encode(zhangPacket);
        ctx.writeAndFlush(byteBuf);
    }
}

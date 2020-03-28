package com.tencent.zeni.rpctest.client;


import com.tencent.zeni.rpctest.protocal.codec.PacketCodec;
import com.tencent.zeni.rpctest.protocal.packet.Model;
import com.tencent.zeni.rpctest.protocal.packet.MsgRepository;
import com.tencent.zeni.rpctest.protocal.packet.PacketData;
import com.tencent.zeni.rpctest.util.MsgCounter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static com.tencent.zeni.rpctest.util.CommonUtil.*;


public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        PacketData packetData = PacketCodec.INSTANCE.decode(byteBuf);

        if (! (packetData instanceof Model)){
            return;
        }

        Model decodeModel = (Model) packetData;
        int session = decodeModel.getSession();
        switch (session){
            case MSG_SESSION_ONE: {
                MsgCounter.count();
                sendMsg(ctx,MSG_SESSION_ONE);
                break;
            }
            case MSG_SESSION_TWO:
            case MSG_SESSION_THREE: {
                MsgCounter.count();
                break;
            }

            default: {
                break;
            }
        }
    }

    private void sendMsg(ChannelHandlerContext ctx,int session) throws Exception{
        Model liPacket = MsgRepository.getInstance().getLiPacket(session);
        ByteBuf encode = PacketCodec.INSTANCE.encode(liPacket);
        ctx.writeAndFlush(encode);
    }
}

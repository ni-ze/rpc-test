package com.tencent.zeni.rpctest.protocal.codec;

import com.tencent.zeni.rpctest.protocal.packet.Model;
import com.tencent.zeni.rpctest.protocal.packet.PacketData;
import com.tencent.zeni.rpctest.util.CommonUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

/**
 * 通信协议
 *
 *   +--------+-------------+-------------------+----------------+---------+
 *   |魔数(4) | 序列化算法(1) |classTypeLength(4) | totalLength(4) | content |{10000=5147}
 *   +--------+-------------+-------------------+----------------+---------+
 */
public class PacketCodec {

    public static final  PacketCodec INSTANCE = new PacketCodec(new ProtoStuffSerializer());

    private Serializer serializer ;
    private static final int MAGIC_NUMBER = 0x12345678;
    private static final Map<Byte, Serializer> serializerMap;
    private static final Map<Byte,Class<? extends PacketData>> classTypeMap;

    static {
        serializerMap = new HashMap<>();
        JsonSerializer jsonSerializer = new JsonSerializer();
        serializerMap.put(jsonSerializer.getSerializerAlgorithm(),jsonSerializer);

        ProtoStuffSerializer protoStuffSerializer = new ProtoStuffSerializer();
        serializerMap.put(protoStuffSerializer.getSerializerAlgorithm(), protoStuffSerializer);

        classTypeMap = new HashMap<>();
        classTypeMap.put(CommonUtil.classType,Model.class);
    }

    public PacketCodec(Serializer serializer) {
        this.serializer = serializer;
    }


    public ByteBuf encode(PacketData packetData) throws Exception {
        byte[] bytes = serializer.serialize(packetData);

        ByteBuf buffer = ByteBufAllocator.DEFAULT.ioBuffer();


        buffer.writeInt(MAGIC_NUMBER);
        buffer.writeByte(packetData.getVersion());
        buffer.writeByte(serializer.getSerializerAlgorithm());
        buffer.writeByte(packetData.getDataType());
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);

        return buffer;
    }

    @SuppressWarnings("unchecked")
    public PacketData decode(ByteBuf byteBuf) throws Exception {
        //1.跳过魔数
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        //读取序列化方式
        byte serializerType = byteBuf.readByte();
        //读取对象类型
        byte classType = byteBuf.readByte();
        Class<? extends PacketData> targetClass2 = classTypeMap.get(classType);
        //读取总长度
        int totalLength = byteBuf.readInt();

        //读取实际数据
        byte[] data = new byte[totalLength];
        byteBuf.readBytes(data);

        if (targetClass2 != null && getSerializer(serializerType) != null){
            return serializer.deserialize(targetClass2, data);
        }

        return null;
    }

    private Serializer getSerializer(byte serializerType){
        return serializerMap.get(serializerType);
    }

}

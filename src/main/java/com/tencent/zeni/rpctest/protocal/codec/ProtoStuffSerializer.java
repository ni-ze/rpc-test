package com.tencent.zeni.rpctest.protocal.codec;

import com.tencent.zeni.rpctest.util.CommonUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.IOException;

public class ProtoStuffSerializer implements Serializer {
    @Override
    public byte getSerializerAlgorithm() {
        return CommonUtil.PROTOBUF;
    }

    @Override
    public byte[] serialize(Object object) throws Exception {
        Schema<Object> schema = (Schema<Object>) RuntimeSchema.getSchema(object.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(512);

        return ProtostuffIOUtil.toByteArray(object,schema, buffer);
    }

    @Override
    public <T> T deserialize(Class<T> className, byte[] origin) throws IOException {
        Schema<T> schema = RuntimeSchema.getSchema(className);
        T t = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(origin,t,schema);
        return t;
    }


}

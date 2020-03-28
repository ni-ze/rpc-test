package com.tencent.zeni.rpctest.protocal.codec;


import com.alibaba.fastjson.JSON;
import com.tencent.zeni.rpctest.util.CommonUtil;

import java.io.IOException;

public class JsonSerializer implements Serializer {


    @Override
    public byte getSerializerAlgorithm() {
        return CommonUtil.JSON;
    }

    @Override
    public byte[] serialize(Object object) throws Exception {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> className, byte[] origin) throws IOException {
        return JSON.parseObject(origin, className);
    }
}

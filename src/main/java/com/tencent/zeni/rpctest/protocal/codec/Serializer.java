package com.tencent.zeni.rpctest.protocal.codec;


import java.io.IOException;

public interface Serializer {

    /**
     * 用户标示具体序列化算法，传给对端，他才知道你用的那种算法。
     */
    byte getSerializerAlgorithm();

    /**
     * 把对象序列化成二进制
     */
    byte[] serialize(Object object) throws Exception;

    /**
     * 将二进制转化成对象
     *
     */
    <T> T deserialize(Class<T> className, byte[] origin) throws IOException;

}

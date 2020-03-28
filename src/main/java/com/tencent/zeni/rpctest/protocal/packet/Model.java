package com.tencent.zeni.rpctest.protocal.packet;


import com.tencent.zeni.rpctest.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Model extends PacketData{

    private String name;

    private int order;

    private int session;

    private String content;


    @Override
    public Byte getDataType() {
        return CommonUtil.classType;
    }
}

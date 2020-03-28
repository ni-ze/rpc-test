package com.tencent.zeni.rpctest.protocal.packet;

import lombok.Data;

@Data
public abstract class PacketData {
   private Byte version = 1;
   public abstract Byte getDataType();
}

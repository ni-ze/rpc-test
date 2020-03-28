package com.tencent.zeni.rpctest.protocal.packet;

import java.util.HashMap;
import java.util.Map;

import static com.tencent.zeni.rpctest.util.CommonUtil.*;

public class MsgRepository {
    private static MsgRepository repository = new MsgRepository();

    private static final Map<Integer, Model> ZHANG_MAP = new HashMap<>(3);

    private static final Map<Integer, Model> LI_MAP = new HashMap<>(3);

    static {
        ZHANG_MAP.put(MSG_SESSION_ONE, new Model("ZHANG", 1, MSG_SESSION_ONE,"吃了没，您呐？"));
        ZHANG_MAP.put(MSG_SESSION_TWO, new Model("ZHANG", 2, MSG_SESSION_TWO,"嗨，没事儿溜溜弯儿。"));
        ZHANG_MAP.put(MSG_SESSION_THREE, new Model("ZHANG", 3, MSG_SESSION_THREE,"回头去给老太太请安！"));

        LI_MAP.put(MSG_SESSION_ONE, new Model("LI", 4, MSG_SESSION_ONE,"刚吃。"));
        LI_MAP.put(MSG_SESSION_TWO, new Model("LI", 5, MSG_SESSION_TWO,"您这，嘛去？"));
        LI_MAP.put(MSG_SESSION_THREE, new Model("LI", 6, MSG_SESSION_THREE,"有空家里坐坐啊。"));
    }

    private MsgRepository() {

    }

    public static MsgRepository getInstance() {
        return repository;
    }

    public Model getZhangPacket(Integer sessionId) {
        return ZHANG_MAP.get(sessionId);
    }

    public Model getLiPacket(Integer sessionId) {
        return LI_MAP.get(sessionId);
    }
}

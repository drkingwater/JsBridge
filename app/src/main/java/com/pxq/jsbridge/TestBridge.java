package com.pxq.jsbridge;

import com.pxq.jsbridge.annotation.Bridge;
import com.pxq.jsbridge.annotation.UnHandle;

@Bridge(name = "android", jsMethod = "action")
public class TestBridge {

    @UnHandle
    public void handle(String action){

    }

}

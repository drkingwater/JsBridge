package com.pxq.jsbridge;

import android.webkit.JavascriptInterface;

import com.pxq.jsbridge.annotation.Bridge;
import com.pxq.jsbridge.annotation.JsConfig;
import com.pxq.jsbridge.annotation.JsError;
import com.pxq.jsbridge.annotation.JsFunc;
import com.pxq.jsbridge.annotation.UnHandle;

@Bridge(name = "androidSync")
@JsConfig
public class TestBridge {

    @UnHandle
    public void handle(String action){

    }

    @JsFunc("func")
    public String func(){
        return "test return data";
    }

    @UnHandle
    public void unhandle(String json){

    }

    @JsError
    public void errors(String json){

    }


}

package com.pxq.jsbridge;

import android.webkit.JavascriptInterface;

import com.pxq.jsbridge.annotation.Bridge;
import com.pxq.jsbridge.annotation.UnHandle;

@Bridge(name = "android")
public class TestBridge {

    @UnHandle
    public void handle(String action){

    }

    @JavascriptInterface
    public void func(){

    }

    @JavascriptInterface
    public void func(int a, int b){

    }

    @JavascriptInterface
    public void func1(int a, String b){

    }

}

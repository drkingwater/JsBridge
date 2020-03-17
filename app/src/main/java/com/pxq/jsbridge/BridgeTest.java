package com.pxq.jsbridge;

import android.util.Log;

import com.pxq.jsbridge.annotation.Bridge;
import com.pxq.jsbridge.annotation.JsAction;
import com.pxq.jsbridge.annotation.JsConfig;
import com.pxq.jsbridge.annotation.JsError;
import com.pxq.jsbridge.annotation.UnHandle;

@Bridge(name = "android")
@JsConfig(jsMethod = "call", actionName = "function", paramsName = "data")
public class BridgeTest {

    private static final String TAG = "BridgeTest";

    @JsAction("test")
    public void test(){
        Log.i(TAG, "test: ");
    }


    @JsAction("testData")
    public void testData(TestBean test){
        Log.i(TAG, "testData: " + test.name +" " +  test.data);
    }

    @UnHandle
    public void UnHandle(String request){
        Log.w(TAG, "UnHandle: " + request);
    }

    @JsError
    public void error(String request, Exception e){
        Log.e(TAG, "error: " +request, e);
    }

}

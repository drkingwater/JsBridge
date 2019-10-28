package com.pxq.jsbridge.core;

import android.annotation.SuppressLint;
import android.util.Log;
import android.webkit.WebView;

/**
 * Webview绑定JsBridge工具类
 * author : pxq
 * date : 19-10-26 上午2:34
 */
public class JsBridge {

    private static final String TAG = "JsBridge";

    private static final String JS_BRIDGE_SUFFIX = "$$Bridge";

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public static void bind(WebView webView, Object bridge){
        String className = bridge.getClass().getCanonicalName() + JS_BRIDGE_SUFFIX;
        Log.e(TAG, "bind: " + className);
        try {
            Class<?> jsBridgeClazz = Class.forName(className);
            IJsBridge jsBridge = (IJsBridge) jsBridgeClazz.getConstructor(bridge.getClass()).newInstance(bridge);
            Log.e(TAG, "bind: " + jsBridge.getName());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.addJavascriptInterface(jsBridge, jsBridge.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

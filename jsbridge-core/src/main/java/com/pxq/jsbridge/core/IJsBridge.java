package com.pxq.jsbridge.core;

/**
 * 约束类，用来获取webview.addJavascriptInterface(obj, name)的name
 * author : pxq
 * date : 19-10-26 上午2:20
 * @see android.webkit.WebView#addJavascriptInterface(Object, String)
 */
public interface IJsBridge {

    //获取JavascriptInterface名
    String getName();

}

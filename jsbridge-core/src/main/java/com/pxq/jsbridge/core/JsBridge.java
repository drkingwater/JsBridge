package com.pxq.jsbridge.core;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.WebView;

import java.lang.ref.WeakReference;

/**
 * Webview绑定JsBridge工具类
 * author : pxq
 * date : 19-10-26 上午2:34
 */
public class JsBridge {

    private static final String TAG = "JsBridge";

    private static final String JS_BRIDGE_SUFFIX = "$$Bridge";

    private static WeakReference<WebView> mWebViewRef;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    /**
     * 向webview添加处理器，允许多个
     * @param webView
     * @param handlers
     */
    @SuppressLint({"SetJavaScriptEnabled"})
    public static void bind(WebView webView, Object... handlers){
        try {
            mWebViewRef = new WeakReference<>(webView);
            webView.getSettings().setJavaScriptEnabled(true);
            for (Object handler : handlers) {
                addJavascriptInterface(webView, handler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 向webview添加处理器
     * @param webView
     * @param handler
     * @throws Exception
     */
    @SuppressLint("JavascriptInterface")
    private static void addJavascriptInterface(WebView webView, Object handler) throws Exception{
        String className = handler.getClass().getCanonicalName() + JS_BRIDGE_SUFFIX;
        Class<?> jsBridgeClazz = Class.forName(className);
        IJsBridge jsBridge = (IJsBridge) jsBridgeClazz.getConstructor(handler.getClass()).newInstance(handler);
        webView.addJavascriptInterface(jsBridge, jsBridge.getName());
        Log.d(TAG, "addJavascriptInterface: " + jsBridge.getName());
    }

    /**
     * java调用js方法
     * @param function
     * @param params
     */
    public static void callJS(final String function, final String params){
        if (mWebViewRef != null && mWebViewRef.get() != null){
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    mWebViewRef.get().loadUrl(String.format("javascript:%s('%s')", function, params));
                }
            });

        }
    }

    public static void unbind(){
        if (mWebViewRef != null){
            mWebViewRef.clear();
        }
        mWebViewRef = null;
    }

}

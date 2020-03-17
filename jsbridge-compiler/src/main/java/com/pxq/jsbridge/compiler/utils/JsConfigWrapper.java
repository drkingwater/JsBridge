package com.pxq.jsbridge.compiler.utils;

/**
 * Description: js配置封装
 * Author : pxq
 * Date : 2020/3/17 9:30 PM
 */
public class JsConfigWrapper {

    private String mMethodName;

    private String mActionName;

    private String mParamsName;

    public JsConfigWrapper(String methodName, String actionName, String dataName) {
        mMethodName = methodName;
        mActionName = actionName;
        mParamsName = dataName;
    }

    public String getMethodName() {
        return mMethodName;
    }

    public void setMethodName(String methodName) {
        mMethodName = methodName;
    }

    public String getActionName() {
        return mActionName;
    }

    public void setActionName(String actionName) {
        mActionName = actionName;
    }

    public String getParamsName() {
        return mParamsName;
    }

    public void setParamsName(String paramsName) {
        mParamsName = paramsName;
    }
}

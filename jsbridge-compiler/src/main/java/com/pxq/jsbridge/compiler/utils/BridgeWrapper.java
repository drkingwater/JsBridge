package com.pxq.jsbridge.compiler.utils;

/**
 * Description: bridge封装类
 * Author : pxq
 * Date : 2020/3/17 9:31 PM
 */
public class BridgeWrapper {

    private String mBridgeName;

    private String mPackageName;

    private String mClassName;

    private JsConfigWrapper mJsConfig;

    public BridgeWrapper(String bridgeName, String packageName, String className, JsConfigWrapper jsConfig) {
        mBridgeName = bridgeName;
        mPackageName = packageName;
        mClassName = className;
        mJsConfig = jsConfig;
    }

    public String getBridgeName() {
        return mBridgeName;
    }

    public void setBridgeName(String bridgeName) {
        mBridgeName = bridgeName;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(String className) {
        mClassName = className;
    }

    public JsConfigWrapper getJsConfig() {
        return mJsConfig;
    }

    public void setJsConfig(JsConfigWrapper jsConfig) {
        mJsConfig = jsConfig;
    }
}

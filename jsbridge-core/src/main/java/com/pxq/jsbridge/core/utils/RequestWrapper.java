package com.pxq.jsbridge.core.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description: js请求包装类
 * Author : pxq
 * Date : 2020/3/17 9:41 PM
 */
public class RequestWrapper {
    //请求方法
    private String mAction;
    //请求参数
    private String mParams;

    public RequestWrapper(String action, String params) {
        mAction = action;
        mParams = params;
    }

    public String getAction() {
        return mAction;
    }

    public String getParams() {
        return mParams;
    }

    /**
     * 生成请求包装类
     * @param json
     * @param actionKey
     * @param paramsKey
     * @return
     * @throws JSONException
     */
    public static RequestWrapper get(String json, String actionKey, String paramsKey) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        //action不可为空
        String action = jsonObject.getString(actionKey);
        String params = "";
        //params可以为空
        if (!jsonObject.isNull(paramsKey)){
            params = jsonObject.getString(paramsKey);
        }
        return new RequestWrapper(action, params);
    }
}

package com.pxq.jsbridge;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.pxq.jsbridge.annotation.ActionParser;
import com.pxq.jsbridge.core.utils.IJsonParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description: 自定义parser
 * Author : pxq
 * Date : 20-1-18 下午12:51
 */
@ActionParser
public class ObjParser implements IJsonParser {

    private static final String TAG = "ObjParser";

    @Override
    public <T> T getData(String json, Class<T> clazz) {
        Log.e(TAG, "getData: " + json);
        if (TextUtils.isEmpty(json)){
            return null;
        }
        return JSON.parseObject(json, clazz);
    }
}

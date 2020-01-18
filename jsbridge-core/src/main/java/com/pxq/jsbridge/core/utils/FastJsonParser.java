package com.pxq.jsbridge.core.utils;

import com.alibaba.fastjson.JSON;

/**
 * Description: fastjson解析
 * Author : pxq
 * Date : 20-1-18 下午12:37
 */
public class FastJsonParser implements IJsonParser {
    @Override
    public String getAction(String json) {
        return get(json, "action");
    }

    @Override
    public <T> T getData(String json, Class<T> clazz) {
        return JSON.parseObject(get(json, "data"), clazz);
    }

    private static String get(String json, String key){
        return JSON.parseObject(json).getString(key);
    }

}

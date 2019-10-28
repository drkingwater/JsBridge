package com.pxq.jsbridge.core.utils;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

public class JsonParser {

    public static String getAction(String json){
        return get(json, "action");
    }

    public static <T> T parse(String json, Class<T> clazz) throws IOException {
        return JSON.parseObject(get(json, "data"), clazz);
    }

    private static String get(String json, String key){
        return JSON.parseObject(json).getString(key);
    }

}

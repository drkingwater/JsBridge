package com.pxq.jsbridge.core.utils;

/**
 * Description: 自定义Json解析
 * Author : pxq
 * Date : 20-1-18 下午12:35
 */
public interface IJsonParser {

    String getAction(String json);

    <T> T getData(String json, Class<T> clazz);

}

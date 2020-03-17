package com.pxq.jsbridge.core.utils;

/**
 * Description: 自定义Json解析
 * Author : pxq
 * Date : 20-1-18 下午12:35
 */
public interface IJsonParser {

    /**
     * 解析params字段
     * @param json 交互json中的params字段，可能为空
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T getData(String json, Class<T> clazz);

}

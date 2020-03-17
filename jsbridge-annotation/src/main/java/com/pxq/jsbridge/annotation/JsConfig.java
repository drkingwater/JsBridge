package com.pxq.jsbridge.annotation;

/**
 * Description: bridge配置信息
 * Author : pxq
 * Date : 2020/3/17 9:25 PM
 */
public @interface JsConfig {
    //交互方法
    String jsMethod() default "request";
    //json action字段名
    String actionName() default "action";
    //json data字段名
    String paramsName() default "params";

}

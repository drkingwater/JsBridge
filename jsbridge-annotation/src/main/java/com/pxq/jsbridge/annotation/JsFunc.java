package com.pxq.jsbridge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 标记处理js同步请求的方法的注解(function : 返回值为String的请求)
 * Author : pxq
 * Date : 2020/4/4 7:53 PM
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface JsFunc {

    //function名
    String value();
}

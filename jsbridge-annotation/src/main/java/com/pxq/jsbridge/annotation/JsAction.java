package com.pxq.jsbridge.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * js请求android端的action
 * author : pxq
 * date : 19-10-24 下午9:38
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface JsAction {

    //action名
    String value();

}

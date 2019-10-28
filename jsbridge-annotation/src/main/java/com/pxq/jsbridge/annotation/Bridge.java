package com.pxq.jsbridge.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * js和android互相交互的js名
 * author : pxq
 * date : 19-10-24 下午9:39
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Bridge {
    //js交互名
    String name();
    //交互方法
    String jsMethod();

}

package com.pxq.jsbridge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 未处理的Js Request
 * author : pxq
 * date : 19-10-26 下午2:25
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface UnHandle {
}

package com.pxq.jsbridge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Js Request异常，如解析失败等
 * author : pxq
 * date : 19-10-26 下午2:27
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface JsError {
}

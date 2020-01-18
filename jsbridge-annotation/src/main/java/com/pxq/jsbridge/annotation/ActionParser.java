package com.pxq.jsbridge.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: json解析类，提供给外部自定义json解析
 * Author : pxq
 * Date : 20-1-18 下午12:34
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface ActionParser {
}

package com.pxq.jsbridge.compiler.utils;

import javax.lang.model.type.TypeMirror;

public class TypeChecker {
    //type
    static String TYPE_STRING = "java.lang.String";
    static String TYPE_EXCEPTION = "java.lang.Exception";

    public static boolean isString(TypeMirror typeMirror) {
        return typeMirror.toString().equals(TYPE_STRING);
    }

    public static boolean isException(TypeMirror typeMirror) {
        return typeMirror.toString().equals(TYPE_EXCEPTION);
    }


}

package com.pxq.jsbridge.compiler.utils;

public class Consts {

    static String PREFIX_OF_LOGGER = "JsBridge>>>";

    //默认的js方法配置
    static String JS_CONFIG_METHOD = "request";
    static String JS_CONFIG_ACTION_NAME = "action";
    static String JS_CONFIG_PARAMS_NAME = "params";

    //generate
    //生成的js交互类后缀
    static String JS_BRIDGE_SUFFIX = "$$Bridge";
    //交互方法 (添加了JavascriptInterface的方法)
    static String JS_BRIDGE_METHOD_NAME = "getName";
    //交互方法参数
    static final String JS_BRIDGE_METHOD_PARAM = "request";
    //持有的处理对象名
    static final String JS_FIELD_HANDLER_NAME = "mHandler";
    static final String JS_HANDLE_METHOD_NAME = "handleRequest";

    static final String VAR_ACTION_NAME = "requestWrapper";

    //js请求封装类
    static final String REQUEST_WRAPPER_PACKAGE = "com.pxq.jsbridge.core.utils";
    static final String REQUEST_WRAPPER_CLASS = "RequestWrapper";
    static final String REQUEST_WRAPPER_GETTER = "get";
    static final String REQUEST_WRAPPER_GET_ACTION = "getAction";
    static final String REQUEST_WRAPPER_GET_PARAMS = "getParams";


    //interface
    static String ANNOTATION_JSINTERFACE_PACKAGE = "android.webkit";
    static String ANNOTATION_JSINTERFACE_NAME = "JavascriptInterface";

    static String JS_BRIDGE_NAME_INTERFACE_PACKAGE = "com.pxq.jsbridge.core";
    static String JS_BRIDGE_NAME_INTERFACE_CLASSNAME = "IJsBridge";


    //json解析工具类
    static String PARSER_PACKAGE = "com.pxq.jsbridge.core.utils";
    static String PARSER_CLASS_NAME = "JsonParser";
    static String FAST_JSON_PARSER_CLASS_NAME = "FastJsonParser";
    static String IPARSER_CLASS_NAME = "IJsonParser";
    public static String IPARSER_INTERFACE = PARSER_PACKAGE + "." + IPARSER_CLASS_NAME;
    //持有的parser成员变量名
    static String JSON_PARSER_FIELD_NAME = "mActionParser";

    //Json解析方法
    static String PARSER_METHOD_GET_ACTION = "getAction";
    static String PARSER_METHOD_GET_DATA = "getData";
    static String PARSER_METHOD_PARSE = "parse";

}

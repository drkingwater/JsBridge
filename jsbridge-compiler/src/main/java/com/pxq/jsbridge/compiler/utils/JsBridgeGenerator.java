package com.pxq.jsbridge.compiler.utils;

import com.pxq.jsbridge.annotation.Bridge;
import com.pxq.jsbridge.annotation.JsAction;
import com.pxq.jsbridge.annotation.JsConfig;
import com.pxq.jsbridge.annotation.JsError;
import com.pxq.jsbridge.annotation.JsFunc;
import com.pxq.jsbridge.annotation.UnHandle;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;


/**
 * js交互类生成工具
 * author : pxq
 * date : 19-10-24 下午11:03
 */
public class JsBridgeGenerator {
    //被Bridge注解的类
    private Element mBridgeElement;
    //自定义Json 解析类
    private Element mJsonParserElement;

    private JsHandler mJsHandler;

    private BridgeConfigWrapper mBridgeConfigWrapper;

    private void init() {
        mBridgeConfigWrapper = null;
        mJsHandler = null;
    }

    public void setBridgeElement(Element bridgeElement) {
        mBridgeElement = bridgeElement;
    }


    public void setJsonParserElement(Element jsonParserElement) {
        mJsonParserElement = jsonParserElement;
    }

    /**
     * 生成JsBridge交互类
     *
     * @param filer
     * @throws IOException
     */
    public void generate(Filer filer) throws IOException {
        init();
        // 1、获取Bridge配置信息
        mBridgeConfigWrapper = getBridgeWrapper(mBridgeElement);

        // 2、生成一个js请求处理对象
        FieldSpec mHandlerField = FieldSpec.builder(TypeName.get(mBridgeElement.asType()), Consts.JS_FIELD_HANDLER_NAME, Modifier.PUBLIC).build();
        //生成构造方法给handler赋值
        MethodSpec.Builder constructMethodBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(mBridgeElement.asType()), Consts.JS_FIELD_HANDLER_NAME)
                .addStatement("this.$N = " + Consts.JS_FIELD_HANDLER_NAME, mHandlerField);//mHandler赋值
        // 3、创建一个json解析类
        ClassName iParserClassName = ClassName.get(Consts.PARSER_PACKAGE, Consts.IPARSER_CLASS_NAME);
        FieldSpec mParserField = FieldSpec.builder(iParserClassName, Consts.JSON_PARSER_FIELD_NAME, Modifier.PRIVATE).build();
        //给mJsonParser赋值
        if (mJsonParserElement == null) {
            //使用默认的json解析
            constructMethodBuilder.addStatement("this.$N = new $T()", mParserField, ClassName.get(Consts.PARSER_PACKAGE, Consts.FAST_JSON_PARSER_CLASS_NAME));
        } else {
            //使用自定义json解析
            constructMethodBuilder.addStatement("this.$N = new $T()", mParserField, mJsonParserElement.asType());
        }
        MethodSpec constructMethod = constructMethodBuilder.build();
        // 4、生成request处理方法
        // 4.1、获取bridge里的所有注解方法
        mJsHandler = JsHandler.create(mBridgeElement);
        // 4.2、生成handleRequest方法
        MethodSpec mHandleMethod = generateHandleMethod(mHandlerField);
        // 4.3、生成handelRequestSync方法
        MethodSpec mHandleSyncMethod = generateHandleSyncMethod(mHandlerField);

        //生成获取js名的方法
        MethodSpec jsNameMethod = MethodSpec.methodBuilder(Consts.JS_BRIDGE_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addStatement("return $S", mBridgeConfigWrapper.getBridgeName())
                .build();

        //生成js交互方法
        MethodSpec jsMethod = generateJsMethod(mBridgeConfigWrapper.getJsConfig().getMethodName(), mHandlerField, mHandleMethod);
        MethodSpec jsSyncMethod = generateJsSyncMethod(mBridgeConfigWrapper.getJsConfig().getSyncMethodName(), mHandlerField, mHandleSyncMethod);

        //生成js交互类
        TypeSpec jsClass = TypeSpec.classBuilder(mBridgeConfigWrapper.getClassName())  //类名
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(Consts.JS_BRIDGE_NAME_INTERFACE_PACKAGE, Consts.JS_BRIDGE_NAME_INTERFACE_CLASSNAME))  //IJsBridge接口
                .addField(mHandlerField)     //js请求处理类
                .addField(mParserField)      //json解析工具
                .addMethod(constructMethod)
                .addMethod(jsMethod)        //交互方法
                .addMethod(jsSyncMethod)    //同步交互方法
                .addMethod(jsNameMethod)    //获取bridge名的方法
                .addMethod(mHandleMethod)   //处理交互方法的方法
                .addMethod(mHandleSyncMethod)  //处理同步交互方法的方法
                .build();

        //生成类文件
        JavaFile.builder(mBridgeConfigWrapper.getPackageName(), jsClass)
                .build()
                .writeTo(filer);


    }


    /**
     * 获取bridge信息
     *
     * @param element
     * @return
     */
    private BridgeConfigWrapper getBridgeWrapper(Element element) {
        Bridge bridge = element.getAnnotation(Bridge.class);
        String bridgeName = bridge.value();
        String className = element.getSimpleName().toString() + Consts.JS_BRIDGE_SUFFIX;
        String packageName = element.getEnclosingElement().toString();
        JsConfigWrapper config = getConfig(element);
        return new BridgeConfigWrapper(bridgeName, packageName, className, config);
    }

    /**
     * 获取js配置信息
     *
     * @param element
     * @return
     */
    private JsConfigWrapper getConfig(Element element) {
        JsConfig jsConfig = element.getAnnotation(JsConfig.class);
        if (jsConfig == null) {
            return new JsConfigWrapper(Consts.JS_CONFIG_METHOD, Consts.JS_CONFIG_ACTION_NAME, Consts.JS_CONFIG_PARAMS_NAME);
        }
        return new JsConfigWrapper(jsConfig.jsMethod(), jsConfig.actionName(), jsConfig.paramsName());
    }


    /**
     * 生成处理request的方法 void handleRequest{}
     *
     * @param mHandlerField
     * @return
     */
    private MethodSpec generateHandleMethod(FieldSpec mHandlerField) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Consts.JS_HANDLE_METHOD_NAME)
                .addParameter(TypeName.get(String.class), Consts.JS_BRIDGE_METHOD_PARAM)
                //生成 String action = JsonParse.getAction(request)
                .addStatement("$T $N = $T.$N($N, $S, $S)",
                        ClassName.get(Consts.REQUEST_WRAPPER_PACKAGE, Consts.REQUEST_WRAPPER_CLASS),
                        Consts.VAR_ACTION_NAME,
                        ClassName.get(Consts.REQUEST_WRAPPER_PACKAGE, Consts.REQUEST_WRAPPER_CLASS),
                        Consts.REQUEST_WRAPPER_GETTER,
                        Consts.JS_BRIDGE_METHOD_PARAM,
                        mBridgeConfigWrapper.getJsConfig().getActionName(),
                        mBridgeConfigWrapper.getJsConfig().getParamsName());
        //填充方法
        TypeElement typeElement = (TypeElement) mBridgeElement;
        //开始switch
        if (!CollectionUtils.isEmpty(typeElement.getEnclosedElements())) {
            builder.beginControlFlow("switch($N.$N())", Consts.VAR_ACTION_NAME, Consts.REQUEST_WRAPPER_GET_ACTION);
        }

        //1、处理JsAction
        for (Element element : mJsHandler.getJsActionList()) {
            ExecutableElement executableElement = (ExecutableElement) element;
            JsAction jsAction = element.getAnnotation(JsAction.class);
            if (executableElement.getParameters().size() == 1) {
                VariableElement variableElement = executableElement.getParameters().get(0);
                //生成case "action" : JsonParser.parse(request, Class);
                builder.addStatement("case $S: \n$N.$N($N.$N($N.$N(), $T.class))",
                        jsAction.value(),
                        mHandlerField,
                        executableElement.getSimpleName(),
                        Consts.JSON_PARSER_FIELD_NAME,
                        Consts.PARSER_METHOD_GET_DATA,
                        Consts.VAR_ACTION_NAME,
                        Consts.REQUEST_WRAPPER_GET_PARAMS,
                        ClassName.get(variableElement.asType()));
            } else {
                builder.addStatement("case $S: \n$N.$N()", jsAction.value(), mHandlerField, executableElement.getSimpleName());
            }
            builder.addStatement("break");
        }

        //2、处理@UnHandle注解
        //生成switch 的default分支
        if (mJsHandler.getUnhandleElement() != null) {
            builder.addStatement("default : $N.$N(" + Consts.JS_BRIDGE_METHOD_PARAM + ")", mHandlerField, mJsHandler.getUnhandleElement().getSimpleName());
        }
        //结束switch
        if (!CollectionUtils.isEmpty(typeElement.getEnclosedElements())) {
            builder.endControlFlow();
        }
        return builder
                .addException(TypeName.get(Exception.class))
                .build();
    }

    /**
     * 生成处理同步请求的方法
     * <p>
     * {@code
     * String handleRequestSync(){
     *
     * }
     * }
     * </p>
     *
     * @param mHandlerField
     * @return
     */
    private MethodSpec generateHandleSyncMethod(FieldSpec mHandlerField) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(Consts.JS_HANDLE_SYNC_METHOD_NAME)
                .addParameter(TypeName.get(String.class), Consts.JS_BRIDGE_METHOD_PARAM)
                //RequestWrapper requestWrapper = RequestWrapper.get(request, "action", "data");
                .addStatement("$T $N = $T.$N($N, $S, $S)",
                        ClassName.get(Consts.REQUEST_WRAPPER_PACKAGE, Consts.REQUEST_WRAPPER_CLASS),
                        Consts.VAR_ACTION_NAME,
                        ClassName.get(Consts.REQUEST_WRAPPER_PACKAGE, Consts.REQUEST_WRAPPER_CLASS),
                        Consts.REQUEST_WRAPPER_GETTER,
                        Consts.JS_BRIDGE_METHOD_PARAM,
                        mBridgeConfigWrapper.getJsConfig().getActionName(),
                        mBridgeConfigWrapper.getJsConfig().getParamsName());
        //填充方法
        TypeElement typeElement = (TypeElement) mBridgeElement;
        //开始switch(action)
        if (!CollectionUtils.isEmpty(typeElement.getEnclosedElements())) {
            builder.beginControlFlow("switch($N.$N())", Consts.VAR_ACTION_NAME, Consts.REQUEST_WRAPPER_GET_ACTION);
        }

        //1、处理JsFunc 带有返回值
        for (Element element : mJsHandler.getJsFuncList()) {
            ExecutableElement executableElement = (ExecutableElement) element;
            JsFunc jsFunc = element.getAnnotation(JsFunc.class);
            if (executableElement.getParameters().size() == 1) {
                VariableElement variableElement = executableElement.getParameters().get(0);
                //生成case "action" : JsonParser.parse(request, Class);
                builder.addStatement("case $S: \n return $N.$N($N.$N($N.$N(), $T.class))",
                        jsFunc.value(),
                        mHandlerField,
                        executableElement.getSimpleName(),
                        Consts.JSON_PARSER_FIELD_NAME,
                        Consts.PARSER_METHOD_GET_DATA,
                        Consts.VAR_ACTION_NAME,
                        Consts.REQUEST_WRAPPER_GET_PARAMS,
                        ClassName.get(variableElement.asType()));
            } else {
                builder.addStatement("case $S: \n return $N.$N()", jsFunc.value(), mHandlerField, executableElement.getSimpleName());
            }
        }

        //2、处理@UnHandle注解
        //生成switch 的default分支
        if (mJsHandler.getUnhandleElement() != null) {
            builder.addStatement("default : $N.$N(" + Consts.JS_BRIDGE_METHOD_PARAM + ")", mHandlerField, mJsHandler.getUnhandleElement().getSimpleName());
        }
        //结束switch
        if (!CollectionUtils.isEmpty(typeElement.getEnclosedElements())) {
            builder.endControlFlow();
        }
        //默认返回值
        builder.addStatement("return $S", Consts.JS_SYNC_METHOD_RETURN);
        return builder
                .returns(String.class)
                .addException(TypeName.get(Exception.class))
                .build();
    }

    /**
     * 生成注解了@JavascriptInterface的方法,如request()
     *
     * @param jsMethodName
     * @param mHandlerField
     * @return
     */
    private MethodSpec generateJsMethod(String jsMethodName, FieldSpec mHandlerField, MethodSpec handleMethod) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder(jsMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get(Consts.ANNOTATION_JSINTERFACE_PACKAGE, Consts.ANNOTATION_JSINTERFACE_NAME)) //添加JavascriptInterface注解
                .addParameter(String.class, Consts.JS_BRIDGE_METHOD_PARAM)  //方法参数
                .beginControlFlow("try")
                .addStatement("$N(" + Consts.JS_BRIDGE_METHOD_PARAM + ")", handleMethod)
                .nextControlFlow("catch ($T e)", Exception.class);
        ExecutableElement mJsErrorElement = (ExecutableElement) mJsHandler.getErrorElement();
        if (mJsErrorElement != null) {
            switch (mJsErrorElement.getParameters().size()) {
                case 0:
                    builder.addStatement("$N.$N()", mHandlerField, mJsErrorElement.getSimpleName());
                    break;
                case 1:
                    if (TypeChecker.isString(mJsErrorElement.getParameters().get(0).asType())) {
                        Logger.info("params string .......");
                        builder.addStatement("$N.$N($N)", mHandlerField, mJsErrorElement.getSimpleName(), Consts.JS_BRIDGE_METHOD_PARAM);
                    }
                    if (TypeChecker.isException(mJsErrorElement.getParameters().get(0).asType())) {
                        builder.addStatement("$N.$N(e)", mHandlerField, mJsErrorElement.getSimpleName());
                    }
                    break;
                case 2:
                    builder.addStatement("$N.$N($N, e)", mHandlerField, mJsErrorElement.getSimpleName(), Consts.JS_BRIDGE_METHOD_PARAM);
                    break;
            }
        }
        builder.endControlFlow();

        return builder
                .returns(void.class)
                .build();
    }

    /**
     * 生成注解了@JavascriptInterface的同步方法，如requestSync()
     *
     * @param jsMethodName
     * @param mHandlerField
     * @return
     */
    private MethodSpec generateJsSyncMethod(String jsMethodName, FieldSpec mHandlerField, MethodSpec handleMethod) {

        MethodSpec.Builder builder = MethodSpec.methodBuilder(jsMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(ClassName.get(Consts.ANNOTATION_JSINTERFACE_PACKAGE, Consts.ANNOTATION_JSINTERFACE_NAME)) //添加JavascriptInterface注解
                .addParameter(String.class, Consts.JS_BRIDGE_METHOD_PARAM)  //方法参数
                .beginControlFlow("try")
                .addStatement("return $N(" + Consts.JS_BRIDGE_METHOD_PARAM + ")", handleMethod)
                .nextControlFlow("catch ($T e)", Exception.class);
        ExecutableElement mJsErrorElement = (ExecutableElement) mJsHandler.getErrorElement();
        if (mJsErrorElement != null) {
            switch (mJsErrorElement.getParameters().size()) {
                case 0:
                    builder.addStatement("$N.$N()", mHandlerField, mJsErrorElement.getSimpleName());
                    break;
                case 1:
                    if (TypeChecker.isString(mJsErrorElement.getParameters().get(0).asType())) {
                        builder.addStatement("$N.$N($N)", mHandlerField, mJsErrorElement.getSimpleName(), Consts.JS_BRIDGE_METHOD_PARAM);
                    }
                    if (TypeChecker.isException(mJsErrorElement.getParameters().get(0).asType())) {
                        builder.addStatement("$N.$N(e)", mHandlerField, mJsErrorElement.getSimpleName());
                    }
                    break;
                case 2:
                    builder.addStatement("$N.$N($N, e)", mHandlerField, mJsErrorElement.getSimpleName(), Consts.JS_BRIDGE_METHOD_PARAM);
                    break;
            }
        }
        builder.endControlFlow();
        //默认返回值
        builder.addStatement("return $S", Consts.JS_SYNC_METHOD_RETURN);

        return builder
                .returns(String.class)
                .build();
    }


}

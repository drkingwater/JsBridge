package com.pxq.jsbridge.compiler.utils;

import com.pxq.jsbridge.annotation.JsAction;
import com.pxq.jsbridge.annotation.JsError;
import com.pxq.jsbridge.annotation.JsFunc;
import com.pxq.jsbridge.annotation.UnHandle;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Description: 处理js请求的方法包装类
 * Author : pxq
 * Date : 2020/4/4 7:57 PM
 */
public class JsHandler {

    private Element mErrorElement;

    private Element mUnhandleElement;

    private List<Element> mJsActionList;

    private List<Element> mJsFuncList;

    public JsHandler() {
        mJsActionList = new ArrayList<>();
        mJsFuncList = new ArrayList<>();
    }

    public Element getErrorElement() {
        return mErrorElement;
    }

    public void setErrorElement(Element errorElement) {
        mErrorElement = errorElement;
    }

    public Element getUnhandleElement() {
        return mUnhandleElement;
    }

    public void setUnhandleElement(Element unhandleElement) {
        mUnhandleElement = unhandleElement;
    }

    public List<Element> getJsActionList() {
        return mJsActionList;
    }

    public void addJsActionElement(Element element) {
        mJsActionList.add(element);
    }

    public List<Element> getJsFuncList() {
        return mJsFuncList;
    }

    public void addJsFuncElement(Element element) {
        mJsFuncList.add(element);
    }

    public static JsHandler create(Element bridgeElement) {
        JsHandler jsHandler = new JsHandler();
        TypeElement typeElement = (TypeElement) bridgeElement;
        for (Element element : typeElement.getEnclosedElements()) {

            JsAction jsAction = element.getAnnotation(JsAction.class);
            UnHandle unHandle = element.getAnnotation(UnHandle.class);
            JsError jsError = element.getAnnotation(JsError.class);
            JsFunc jsFunc = element.getAnnotation(JsFunc.class);
            if (jsAction == null && unHandle == null && jsError == null && jsFunc == null) {
                continue;
            }

            //检查注解作用域
            if (element.getKind() != ElementKind.METHOD) {
                Logger.error("JsAction UnHandle JsError JsFunc注解必须作用在方法上");
                return null;
            }

            ExecutableElement executableElement = (ExecutableElement) element;
            //1、多个注解的情况，优先JsAction
            if (jsAction != null) {
                Logger.info(element.getSimpleName().toString() + " " + jsAction.value() + " ");
                //只允许一个参数
                if (executableElement.getParameters().size() > 1) {
                    Logger.error("@JsAction 作用的方法只允许一个参数");
                    return null;
                }
                jsHandler.addJsActionElement(element);
                continue;
            }

            //2、处理JsFunc
            if (jsFunc != null) {
                //只允许一个参数
                if (executableElement.getParameters().size() > 1) {
                    Logger.error("@JsFunc 作用的方法只允许一个参数");
                    return null;
                }
                //判断返回类型，必须是String
                if (!TypeChecker.isString(executableElement.getReturnType())) {
                    Logger.error("@JsFunc 作用的方法返回值必须是String");
                    return null;
                }
                jsHandler.addJsFuncElement(element);
                continue;
            }
            //3、处理JsError
            if (jsError != null) {
                VariableElement variableElement;
                switch (executableElement.getParameters().size()) {
                    case 0: //没有参数

                        break;
                    case 1: //只有一个参数，判断类型
                        variableElement = executableElement.getParameters().get(0);
                        //String类型判断
                        if (!TypeChecker.isString(variableElement.asType()) &&
                                !TypeChecker.isException(variableElement.asType())) {
                            Logger.error("@JsError 作用的方法参数个数为1时必须为String或Exception类型 " + variableElement.asType().toString());
                            return null;
                        }

                        break;
                    case 2: //2个参数，判断类型和参数顺序String Exception
                        if (!TypeChecker.isString(executableElement.getParameters().get(0).asType()) && !TypeChecker.isException(executableElement.getParameters().get(1).asType())) {
                            Logger.error("@JsError 作用的方法参数个数为2时必须为String和Exception类型 " +
                                    executableElement.getParameters().get(0).asType().toString() + " " +
                                    executableElement.getParameters().get(1).asType().toString());
                            return null;
                        }
                        break;
                    default:
                        Logger.error("@JsError 作用的方法参数个数不能超过2 : " + executableElement.getSimpleName().toString() + " " + executableElement.getParameters().size());
                        return null;
                }
                jsHandler.setErrorElement(element);
                continue;
            }
            //4、处理UnHandle
            if (unHandle != null) {
                //校验,参数只允许有一个且为String类型
                //只允许一个参数
                if (executableElement.getParameters().size() != 1) {
                    Logger.error("@UnHandle 作用的方法只允许一个参数: " + executableElement.getSimpleName());
                }
                VariableElement variableElement = executableElement.getParameters().get(0);
                //String类型判断
                if (!TypeChecker.isString(variableElement.asType())) {
                    Logger.error("@UnHandle 作用的方法只允许一个参数且为String 类型 " + variableElement.asType().toString());
                }
                jsHandler.setUnhandleElement(element);
            }

        }
        return jsHandler;
    }
}

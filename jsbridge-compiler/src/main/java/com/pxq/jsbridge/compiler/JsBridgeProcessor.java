package com.pxq.jsbridge.compiler;

import com.google.auto.service.AutoService;
import com.pxq.jsbridge.annotation.ActionParser;
import com.pxq.jsbridge.annotation.Bridge;
import com.pxq.jsbridge.annotation.JsAction;
import com.pxq.jsbridge.annotation.JsFunc;
import com.pxq.jsbridge.compiler.utils.Consts;
import com.pxq.jsbridge.compiler.utils.JsBridgeGenerator;
import com.pxq.jsbridge.compiler.utils.Logger;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.xml.ws.Action;

/**
 * 处理注解
 * author : pxq
 * date : 19-10-24 下午9:52
 */
@AutoService(Processor.class)
public class JsBridgeProcessor extends BaseProcessor {

    private JsBridgeGenerator mJsBridgeGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mJsBridgeGenerator = new JsBridgeGenerator();
    }

    @Override
    public Set<Class<? extends Annotation>> getSupportedAnnotations() {

        Set<Class<? extends Annotation>> annotationsSet = new LinkedHashSet<>();
        annotationsSet.add(Bridge.class);
        annotationsSet.add(ActionParser.class);
        annotationsSet.add(JsAction.class);
        annotationsSet.add(JsFunc.class);
        return annotationsSet;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        if (!CollectionUtils.isEmpty(set)) {
            getActionParser(roundEnvironment.getElementsAnnotatedWith(ActionParser.class));
            parseJsBridge(roundEnvironment.getElementsAnnotatedWith(Bridge.class));

        }

        return false;
    }

    /**
     * 获取自定义的JsonParser
     *
     * @param actionParserElements
     */
    private void getActionParser(Set<? extends Element> actionParserElements) {
        if (CollectionUtils.isEmpty(actionParserElements)) {
            return;
        }
        if (actionParserElements.size() > 1) {
            Logger.error("@ActionParser 注解只能存在一个; 当前存在个数：" + actionParserElements.size());
        }
        Element parserElement = (Element) actionParserElements.toArray()[0];
        //防止作用在接口上
        if (parserElement.getKind() != ElementKind.CLASS) {
            Logger.error("@ActionParser 注解只能用于类上 : " + parserElement.getSimpleName());
        }
        //判断是否实现了IJsonParser接口
        TypeElement parserTypeElement = (TypeElement) parserElement;
        List<? extends TypeMirror> interfaces = parserTypeElement.getInterfaces();
        boolean found = false;
        if (interfaces != null) {
            //获取IJsonParser接口对应的Element
            TypeElement typeElement = mElements.getTypeElement(Consts.IPARSER_INTERFACE);
            for (TypeMirror anInterface : interfaces) {
                //判断两个接口是否是同一个
                if (mTypes.isSameType(anInterface, typeElement.asType())) {
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            Logger.error("@ActionParser 注解的类必须要实现接口" + Consts.IPARSER_INTERFACE);
        }
//        Logger.info(">>>>>>> " + parserElement.getSimpleName() + " <<<<<<<");
        mJsBridgeGenerator.setJsonParserElement(parserElement);
    }

    /**
     * 解析注解JsBridge
     * @param bridgeElements
     */
    private void parseJsBridge(Set<? extends Element> bridgeElements) {
        if (CollectionUtils.isEmpty(bridgeElements)){
            return;
        }
        for (Element bridgeElement : bridgeElements) {

            //注解在类上
            if (bridgeElement.getKind() == ElementKind.CLASS){
                Logger.info("类名：" + bridgeElement.toString());
                Logger.info("包名：" + bridgeElement.getEnclosingElement().toString());
                mJsBridgeGenerator.setBridgeElement(bridgeElement);
                try {
                    mJsBridgeGenerator.generate(mFiler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else{
                Logger.error(Bridge.class.getSimpleName() + " 只能作用在类上");
            }

        }
    }


}

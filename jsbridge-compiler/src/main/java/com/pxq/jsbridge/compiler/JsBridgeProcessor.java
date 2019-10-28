package com.pxq.jsbridge.compiler;

import com.google.auto.service.AutoService;
import com.pxq.jsbridge.annotation.Bridge;
import com.pxq.jsbridge.annotation.JsAction;
import com.pxq.jsbridge.compiler.utils.JsBridgeGenerator;
import com.pxq.jsbridge.compiler.utils.Logger;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

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

        return annotationsSet;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        if (!CollectionUtils.isEmpty(set)) {

            parseJsBridge(roundEnvironment.getElementsAnnotatedWith(Bridge.class));

        }

        return false;
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

package com.pxq.jsbridge.compiler;


import com.pxq.jsbridge.compiler.utils.Logger;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * 一些初始化
 * author : pxq
 * date : 19-9-28 下午2:56
 */
public abstract class BaseProcessor extends AbstractProcessor {

    Elements mElements;
    Filer mFiler;
    Types mTypes;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElements = processingEnvironment.getElementUtils();
        mFiler = processingEnvironment.getFiler();
        mTypes = processingEnvironment.getTypeUtils();
        Logger.setMessager(processingEnvironment.getMessager());

        Logger.info(">>> processor start <<<");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    public abstract Set<Class<? extends Annotation>> getSupportedAnnotations();

}

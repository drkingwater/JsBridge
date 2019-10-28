package com.pxq.jsbridge.compiler.utils;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class Logger {

    private static Messager msg;

    public static void setMessager(Messager messager){
        msg = messager;
    }

    public Logger(Messager messager) {

    }

    /**
     * Print info log.
     */
    public static void info(CharSequence info) {
        if (StringUtils.isNotEmpty(info)) {
            msg.printMessage(Diagnostic.Kind.NOTE, Consts.PREFIX_OF_LOGGER + info);
        }
    }

    public static void error(String error) {
        if (StringUtils.isNotEmpty(error)) {
            msg.printMessage(Diagnostic.Kind.ERROR, Consts.PREFIX_OF_LOGGER + "An exception is encountered, [" + error + "]");
        }
    }

    public static void error(Throwable error) {
        if (null != error) {
            msg.printMessage(Diagnostic.Kind.ERROR, Consts.PREFIX_OF_LOGGER + "An exception is encountered, [" + error.getMessage() + "]" + "\n" + formatStackTrace(error.getStackTrace()));
        }
    }

    public static void warning(CharSequence warning) {
        if (StringUtils.isNotEmpty(warning)) {
            msg.printMessage(Diagnostic.Kind.WARNING, Consts.PREFIX_OF_LOGGER + warning);
        }
    }

    private static String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append("    at ").append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

}

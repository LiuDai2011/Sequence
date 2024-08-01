package Sequence.core;

import arc.util.Log;

public class SqLog {
    public static final String modPrefix = "[seq] ";

    public static void info(String text, Object... args) {
        Log.info(modPrefix + text, args);
    }

    public static void info(Object object) {
        info(String.valueOf(object), SqConst.emptyObjArr);
    }

    public static void err(String text, Object... args) {
        Log.err(modPrefix + text, args);
    }

    public static void err(Object object) {
        info(String.valueOf(object), SqConst.emptyObjArr);
    }

    public static void warn(String text, Object... args) {
        Log.warn(modPrefix + text, args);
    }

    public static void warn(Object object) {
        info(String.valueOf(object), SqConst.emptyObjArr);
    }
}

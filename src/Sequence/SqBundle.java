package Sequence;

import static arc.Core.bundle;

public class SqBundle {
    private static final StringBuilder builder = new StringBuilder();

    public static String format(String key, Object ...args) {
        return bundle.format(key, args);
    }

    public static String get(String key, String ...def) {
        return def.length > 0 ? bundle.get(key, def[0]) : bundle.get(key);
    }

    public static String cat(String ...args) {
        builder.setLength(0);
        for (String s : args) {
            builder.append(s).append('.');
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public static String modCat(String ...args) {
        return get(SeqMod.MOD.meta.name + "." + cat(args));
    }

    public static String catGet(String ...args) {
        return get(cat(args));
    }
}

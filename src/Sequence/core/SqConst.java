package Sequence.core;

import arc.files.Fi;
import mindustry.Vars;

public class SqConst {
    public static final Object[] emptyObjArr = {};
    public static Fi unzipDirectory;

    static {
        unzipDirectory = Vars.dataDirectory.child("unzip/");
    }
}

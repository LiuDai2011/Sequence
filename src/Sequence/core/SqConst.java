package Sequence.core;

import Sequence.world.entities.SpreadPointBulletType;
import arc.files.Fi;
import mindustry.Vars;
import mindustry.mod.ClassMap;

public class SqConst {
    public static final Object[] emptyObjArr = {};
    public static Fi unzipDirectory;

    static {
        unzipDirectory = Vars.dataDirectory.child("unzip/");
        ClassMap.classes.put("SpreadPointBulletType", SpreadPointBulletType.class);
    }
}

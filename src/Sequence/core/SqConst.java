package Sequence.core;

import Sequence.world.entities.SpreadPointBulletType;
import Sequence.world.util.ReflectionUtil;
import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.input.KeyCode;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Http;
import arc.util.Log;
import arc.util.Strings;
import arc.util.serialization.Jval;
import mindustry.Vars;
import mindustry.core.Version;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.mod.ClassMap;
import mindustry.mod.ModListing;
import mindustry.mod.Mods;
import mindustry.ui.BorderImage;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.ModsDialog;

import java.lang.reflect.InvocationTargetException;

import static mindustry.Vars.*;

public class SqConst {
    public static final Object[] emptyObjArr = {};
    public static Fi unzipDirectory;

    static {
        unzipDirectory = Vars.dataDirectory.child("unzip/");
        ClassMap.classes.put("SpreadPointBulletType", SpreadPointBulletType.class);
    }
}

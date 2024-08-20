package Sequence;

import Sequence.content.SqContent;
import Sequence.core.SqBundle;
import Sequence.core.SqLog;
import arc.util.Log;
import mindustry.mod.Mod;
import mindustry.mod.Mods;

import static mindustry.Vars.mods;

public class SeqMod extends Mod {
    public static final boolean dev = true;
    public static final String repo = "LiuDai2011/Sequence";
    public static Mods.LoadedMod MOD;

    public SeqMod() {
        SqLog.info("Loaded SeqMod constructor.");

        if (dev) {
            Log.level = Log.LogLevel.debug;
            SqLog.warn("In DEBUG mode!");
        }
    }

    public void loadMeta() {
        MOD = mods.getMod(getClass());

        MOD.meta.version = SqBundle.modCat("meta", "version");
        MOD.meta.author = SqBundle.modCat("meta", "author");
        MOD.meta.displayName = SqBundle.modCat("meta", "display-name");
        MOD.meta.description = SqBundle.modCat("meta", "description");
        MOD.setRepo(repo);
    }

    @Override
    public void loadContent() {
        SqLog.info("Loading seq content.");

        loadMeta();
        SqContent.load();
    }
}

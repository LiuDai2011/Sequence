package Sequence;

import Sequence.content.SqContent;
import arc.util.Log;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.mod.Mod;
import mindustry.mod.Mods;
import mindustry.world.blocks.defense.turrets.ItemTurret;

import static mindustry.Vars.mods;

public class SeqMod extends Mod {
    public static final boolean dev = false;
    public static final String repo = "https://github.com/LiuDai2011/Sequence";
    public static Mods.LoadedMod MOD;

    public SeqMod() {
        SqLog.info("Loaded SeqMod constructor.");

        if (dev) {
            Log.level = Log.LogLevel.debug;
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

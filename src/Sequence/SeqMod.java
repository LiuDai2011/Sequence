package Sequence;

import Sequence.content.SqContent;
import Sequence.core.SqBundle;
import Sequence.core.SqLog;
import Sequence.world.meta.IO;
import Sequence.world.util.Graph;
import Sequence.world.util.Pair;
import arc.Core;
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

        class L extends Pair<Float, Integer> implements IO {
            public L() {
                super(0f, 0);
            }
            public L(float k, int v) {
                super(k, v);
            }
            public L(float k) {
                super(k, 0);
            }

            @Override
            public String toString() {
                return "L{" + getKey() + (getValue() == 0 ? "" : ", " + getValue()) + "}";
            }
        }
        L l1 = new L(0), l2 = new L(1), l3 = new L(2), l4 = new L(3), l5 = new L(4), l6 = new L(5), l7 = new L(6);
        SqLog.info("a");
        Graph<L> a = new Graph<>(L.class);
        a.addEdge(l1, l2, false);
        a.addEdge(l2, l3, false);
        a.addEdge(l3, l4, false);
        a.addEdge(l1, l4, false);
        SqLog.info(a);
        SqLog.info("b");
        Graph<L> b = new Graph<>(L.class);
        b.addEdge(l5, l1, false);
        b.addEdge(l5, l2, false);
        b.addEdge(l5, l3, false);
        b.addEdge(l5, l4, false);
        SqLog.info(b);
        SqLog.info("a+b");
        SqLog.info(a.copy().merge(b));
        Core.app.exit();

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

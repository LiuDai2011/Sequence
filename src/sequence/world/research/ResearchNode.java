package sequence.world.research;

import arc.Core;
import arc.scene.style.Drawable;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.gen.Tex;

import static sequence.world.research.ResearchTree.all;

public class ResearchNode {
    //    public @Nullable ObjectFloatMap<Item> researchCostMultipliers;
    public final Seq<ResearchNode> children = new Seq<>();
    public int depth;
    public @Nullable Drawable icon;
    public @Nullable String name;
    //    public boolean requiresUnlock = false;
    public @Nullable ResearchNode parent;

    public ResearchNode(@Nullable ResearchNode parent) {
        if (parent != null) {
            parent.children.add(this);
        }

        this.parent = parent;
        this.depth = parent == null ? 0 : parent.depth + 1;

        all.add(this);
    }

    public Drawable icon() {
        return icon == null ? Tex.clear : icon;
    }

    public String localizedName() {
        return Core.bundle.get("research-tree." + name, name);
    }
}

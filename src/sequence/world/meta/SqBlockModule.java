package sequence.world.meta;

import arc.util.Nullable;
import mindustry.gen.Building;
import mindustry.world.modules.BlockModule;

public abstract class SqBlockModule extends BlockModule {
    public final @Nullable Building build;

    public SqBlockModule(@Nullable Building build) {
        this.build = build;
    }

    public abstract void update();
}

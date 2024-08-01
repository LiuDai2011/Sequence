package Sequence.core;

import arc.util.Nullable;
import mindustry.world.meta.StatValue;

public interface SeqElem {
    default int order() {
        return -1;
    }

    @Nullable
    StatValue statValue();
}

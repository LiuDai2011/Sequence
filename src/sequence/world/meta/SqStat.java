package sequence.world.meta;

import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

public class SqStat {
    public static final StatCat sequence = new StatCat("sequence");

    public static final Stat sequenceOrder = new Stat("sequenceOrder", sequence),

    sequenceEffect = new Stat("sequenceEffect", sequence),

    informationAnnihilationFactor = new Stat("informationAnnihilationFactor");
}

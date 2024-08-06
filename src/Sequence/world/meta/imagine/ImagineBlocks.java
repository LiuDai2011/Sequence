package Sequence.world.meta.imagine;

import Sequence.graphic.SqColor;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.gen.Building;
import mindustry.ui.Bar;
import mindustry.world.Block;
import Sequence.world.meta.imagine.ImagineEnergyModule.IEMc;

public class ImagineBlocks {
    public static void stats(Block block) {
//        block.stats.add();
    }

    public static void bars(Block block) {
        block.addBar("imagine-energy-amount", entity -> new Bar(
                () -> "amount",
                () -> getColor(getIEM(entity)),
                () -> getIEM(entity).record.amount / getIEM(entity).capacity
        ));
        block.addBar("imagine-energy-activity", entity -> new Bar(
                () -> "activity",
                () -> getColor(getIEM(entity)),
                () -> getIEM(entity).record.activity / 1e8f
        ));
        block.addBar("imagine-energy-instability", entity -> new Bar(
                () -> "instability",
                () -> getColor(getIEM(entity)),
                () -> getIEM(entity).record.instability / 1e6f
        ));
    }

    private static Color getColor(ImagineEnergyModule iem) {
        Color color = SqColor.imagineEnergy.cpy();
        return iem.record.active ?
                color.lerp(Color.white, Mathf.sin(Time.time / 15f) * 0.3f) :
                color.lerp(Color.black, 0.6f);
    }

    private static ImagineEnergyModule getIEM(Building building) {
        return ((IEMc) building).IEM();
    }
}

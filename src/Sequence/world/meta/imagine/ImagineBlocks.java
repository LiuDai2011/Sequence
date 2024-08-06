package Sequence.world.meta.imagine;

import Sequence.graphic.SqColor;
import arc.func.Floatp;
import arc.func.Func;
import arc.func.Prov;
import mindustry.gen.Building;
import mindustry.ui.Bar;
import mindustry.world.Block;
import Sequence.world.meta.imagine.ImagineEnergyModule.IEMc;

public class ImagineBlocks {
    public static void stats(Block block) {
//        block.stats.add();
    }

    public static void bars(Block block) {
//        block.addBar("imagine-energy-amount", entity -> new Bar(
//                "amount",
//                SqColor.imagineEnergy,
//                () -> getIEM(entity).record.amount / getIEM(entity).capacity
//        ));
        block.addBar("imagine-energy-amount", imagineBar(
                e -> "amount",
                e -> getIEM(e).record.amount / getIEM(e).capacity
        ));
    }

    private static Func<Building, Bar> imagineBar(Func<Building, CharSequence> name, Func<Building, Float> fraction) {
        return entity -> new Bar(
                () -> name.get(entity),
                () -> SqColor.imagineEnergy,
                () -> fraction.get(entity)
        );
    }

    private static ImagineEnergyModule getIEM(Building building) {
        return ((IEMc) building).IEM();
    }
}

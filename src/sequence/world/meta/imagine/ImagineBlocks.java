package sequence.world.meta.imagine;

import sequence.graphic.SqColor;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.util.Time;
import mindustry.world.Block;

public class ImagineBlocks {
    public static void stats(Block block) {
//        block.stats.add();
    }

    public static void bars(Block block) {
        if (block instanceof BlockIEc iec && iec.hasImagineEnergy()) {
//            block.addBar("imagine-energy-amount", entity -> new Bar(
//                    () -> "amount",
//                    () -> getColor(getIEM(entity)),
//                    () -> getIEM(entity).amount() / getIEM(entity).capacity()
//            ));
//            block.addBar("imagine-energy-activity", entity -> new Bar(
//                    () -> "activity",
//                    () -> getColor(getIEM(entity)),
//                    () -> getIEM(entity).activity() / 1e6f
//            ));
//            block.addBar("imagine-energy-instability", entity -> new Bar(
//                    () -> "instability",
//                    () -> getColor(getIEM(entity)),
//                    () -> getIEM(entity).instability() / 1e8f
//            ));
        }
    }

    private static Color getColor(ImagineEnergyModule iem) {
        Color color = SqColor.INSTANCE.getImagineEnergy().cpy();
        return iem.isActive() ?
                color.lerp(Color.white, Mathf.sin(Time.time / 15f) * 0.3f) :
                color.lerp(Color.black, 0.6f);
    }

//    private static ImagineEnergyModule getIEM(Building building) {
//        return ((BuildingIEc) building).getIEM();
//    }
}
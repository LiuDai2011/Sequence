package Sequence.content;

import Sequence.core.SeqElem;
import Sequence.world.meta.SqStat;
import arc.graphics.Color;
import mindustry.type.Item;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValue;

public class SqItems {
    public static SqItem berylliumalAlloy,

    crystallizedBeryllium,

    encapsulatedImagineEnergy,

    grainBoundaryAlloy,

    pureSilicon,

    phaseCore,

    standardAlloyPlate,

    vectorizedChip;

    public static void load() {
        berylliumalAlloy = new SqItem("berylliumal-alloy") {{
            color = Color.valueOf("989aa4");
            cost = 0.95f;
            healthScaling = 1.3f;
        }};
        crystallizedBeryllium = new SqItem("crystallized-beryllium") {{
            color = Color.valueOf("d1eeee");
            healthScaling = 0.5f;
            cost = 0.65f;
        }};
        encapsulatedImagineEnergy = new EncapsulatedImagineEnergyItem("encapsulated-imagine-energy") {{
            color = Color.valueOf("bf92f9");
            radioactivity = 15;
            charge = 7;
            cost = 4.5f;
            healthScaling = 0.01f;
            explosiveness = 2;
        }};
        grainBoundaryAlloy = new SqItem("grain-boundary-alloy") {{
            color = Color.valueOf("aaaaaa");
            radioactivity = 0.3f;
            charge = 0.5f;
            cost = 1.8f;
        }};
        pureSilicon = new SqItem("pure-silicon") {{
            color = Color.valueOf("53565c");
            cost = 0.9f;
        }};
        phaseCore = new SqItem("phase-core") {{
            color = Color.valueOf("ffd59e");
            radioactivity = 2.5f;
            cost = 5;
            healthScaling = 0.1f;
        }};
        standardAlloyPlate = new SqItem("standard-alloy-plate") {{
            color = Color.valueOf("cbd97f");
            cost = 1.45f;
            healthScaling = 1.3f;
        }};
        vectorizedChip = new SqItem("vectorized-chip") {{
            color = Color.valueOf("4a4b53");
            cost = 1.2f;
            radioactivity = 0.85f;
            explosiveness = 0.05f;
        }};
    }

    public static class SqItem extends Item implements SeqElem {
        public int ord = -1;

        public SqItem(String name) {
            super(name);
        }

        @Override
        public int order() {
            return ord;
        }

        @Override
        public StatValue statValue() {
            return null;
        }
    }

    public static class EncapsulatedImagineEnergyItem extends SqItem {
        public EncapsulatedImagineEnergyItem(String name) {
            super(name);
        }

        @Override
        public void setStats() {
            super.setStats();
            stats.remove(Stat.explosiveness);
            stats.remove(Stat.radioactivity);
            stats.remove(Stat.charge);
            stats.remove(Stat.flammability);
            stats.addPercent(SqStat.informationAnnihilationFactor, 0.3f);
        }
    }
}

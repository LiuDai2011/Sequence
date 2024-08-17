package Sequence.world.meta.imagine;

import arc.Events;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.gen.Building;

public abstract class ImagineEnergyGraph {
    public static final Seq<ImagineEnergyGraph> all = new Seq<>();

    static {
        Events.run(EventType.Trigger.update, () -> {
            if (Vars.state.is(GameState.State.paused)) return;
            for (ImagineEnergyGraph graph : all) {
                graph.update();
            }
        });
    }

    public ImagineEnergyGraph() {
        all.add(this);
    }

    public abstract ImagineEnergyModule getModule(Building build);

    public abstract void update();
}

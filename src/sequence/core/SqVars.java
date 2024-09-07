package sequence.core;

import sequence.world.entities.SpreadPointBulletType;
import arc.Events;
import arc.files.Fi;
import arc.func.Cons;
import arc.math.Interp;
import arc.struct.IntMap;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.mod.ClassMap;
import org.jetbrains.annotations.NotNull;

public class SqVars {
    private static final int MIN_PRIORITY = Integer.MAX_VALUE;

    public static final Interp pow7 = new Interp.Pow(7);
    public static final Object[] emptyObjArr = {};
    public static final float revTimerUnit = 20;
    public static Fi unzipDirectory;
    public static final IntMap<Seq<Processor>> processors = new IntMap<>();

    static {
        unzipDirectory = Vars.dataDirectory.child("unzip/");
        Events.run(EventType.Trigger.update, () -> allProcessors(Processor::run));
    }

    public static void addProcessor(Runnable runnable, int priority) {
        Processor processor = new Processor(runnable, priority);
        processors.get(priority, Seq::new).add(processor);
    }

    public static void addProcessor(Runnable runnable) {
        addProcessor(runnable, MIN_PRIORITY);
    }

    public static void allProcessors(Cons<Processor> action) {
        for (IntMap.Entry<Seq<Processor>> entry : processors) {
            for (Processor p : entry.value) {
                action.get(p);
            }
        }
    }

    public static final class Processor implements Comparable<Processor> {
        public Runnable runnable;
        public int priority = 0;

        public Processor(Runnable runnable) {
            this.runnable = runnable;
        }

        public Processor(Runnable runnable, int priority) {
            this.runnable = runnable;
            this.priority = priority;
        }

        public void run() {
            runnable.run();
        }

        @Override
        public int compareTo(@NotNull Processor o) {
            return Integer.compare(priority, o.priority);
        }
    }
}

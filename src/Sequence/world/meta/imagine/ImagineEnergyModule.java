package Sequence.world.meta.imagine;

import Sequence.world.meta.SqBlockModule;
import arc.math.Mathf;
import arc.util.io.Reads;
import arc.util.io.Writes;

public class ImagineEnergyModule extends SqBlockModule {
    public ImagineEnergyRecord record;
    public float capacity = 0f;
    public final ImagineEnergyGraph graph = new ImagineEnergyGraph();

    public ImagineEnergyModule() {
        record = new ImagineEnergyRecord();
    }

    @Override
    public void write(Writes write) {
        record.write(write);
    }

    @Override
    public void read(Reads read) {
        record.read(read);
    }

    @Override
    public void update() {
        record.update();
    }

    public void add(float add) {
        record.add(add);
        record.activity += record.multi();
        checkCapacity();
    }

    public boolean getFromGraph(float amount) {
        return true;
    }

    public float boost() {
        return record.boost();
    }

    private void checkCapacity() {
        record.amount = Math.min(record.amount, capacity);
    }

    public interface IEMc {
        ImagineEnergyModule IEM();
    }

    public static class ImagineEnergyRecord {
        public static final double ln2 = 0.69314718056;
        public static final float activeMultiBase = 1.943f, boostMax = 400, log2a6_633e112 = 374.785608153f;

        public float amount;
        public float activity = 1;
        public float instability = 0f;
        public boolean active;

        public ImagineEnergyRecord() {
            this(0, 0, false);
        }

        public ImagineEnergyRecord(float amount, float activity, boolean active) {
            this.active = active;
            this.activity = activity;
            this.amount = amount;
        }

        public void add(float add) {
            amount += add;
        }

        public boolean zero() {
            return Mathf.zero(amount);
        }

        public void write(Writes write) {
            write.f(amount);
        }

        public void read(Reads read) {
            amount = read.f();
        }

        public void active(boolean active) {
            this.active = active;
        }

        public void active() {
            active(true);
        }

        public double multi() {
            return active ? Mathf.pow(
                    Math.min(activeMultiBase + amount / 18563f, activeMultiBase * 100) + 1,
                    Math.min(amount / 12000f + 1, 49.3f)
            ) : 1;
        }

        public double energy() {
            return multi() * amount;
        }

        public float boost() {
            return boostMax / log2a6_633e112 * (float) (Math.log(multi()) / ln2 / 100) + 1;
        }

        public void update() {
            if (active) {
                activity += energy();
                instability += activity;
            }
        }

        public static ImagineEnergyRecord empty() {
            return new ImagineEnergyRecord();
        }
    }
}

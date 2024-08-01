package Sequence.world.meta;

import arc.math.Mathf;
import arc.util.io.Reads;
import arc.util.io.Writes;

public class ImagineEnergyModule extends SqBlockModule {
    public ImagineEnergyRecord record;

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

    public interface IEMc {
        ImagineEnergyModule IEM();
    }

    public static class ImagineEnergyRecord {
        public static final double ln2 = 0.69314718056;
        public static final float activeMultiBase = 1.943f, boostMax = 400, log2a6_633e112 = 374.785608153f;

        public float amount;
        public float activity;
        public boolean active;

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
            if (active) activity += amount * multi();
        }
    }
}

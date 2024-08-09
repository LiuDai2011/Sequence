package Sequence.world.meta.imagine;

import arc.math.Mathf;

public class ImagineEnergyRecord {
    public final float amount, activity, instability;
    public final boolean active;

    public ImagineEnergyRecord(float amount, float activity, float instability, boolean active) {
        this.amount = amount;
        this.activity = activity;
        this.instability = instability;
        this.active = active;
    }

    public boolean zero() {
        return Mathf.zero(amount);
    }

    public static ImagineEnergyRecord empty() {
        return new ImagineEnergyRecord(0, 0, 0, false);
    }
}

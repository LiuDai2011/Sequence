package Sequence.world.meta.imagine;

import Sequence.world.meta.SqBlockModule;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;

public class ImagineEnergyModule extends SqBlockModule {
    protected float capacity;
    protected float amount, activity, instability;
    protected boolean active;

    public ImagineEnergyModule(@Nullable Building build) {
        super(build);
    }

    @Override
    public void update() {
        if (active) {
            activity += amount / 6e5f * Time.delta;
            instability += activity * Time.delta / 60f;
        } else {
            instability -= instability * 0.000012f * Time.delta;
            activity -= activity * 0.0000075f * Time.delta;
        }
    }

    @Override
    public void read(Reads read) {
        capacity = read.f();
        amount = read.f();
        activity = read.f();
        instability = read.f();
        active = read.bool();
    }

    @Override
    public void write(Writes write) {
        write.f(capacity);
        write.f(amount);
        write.f(activity);
        write.f(instability);
        write.bool(active);
    }

    public void active(boolean act) {
        active = act;
    }

    public void active() {
        active = true;
    }

    public boolean isActive() {
        return active;
    }

    public float amount() {
        return amount;
    }

    public void amount(float amount) {
        this.amount = amount;
    }

    public float activity() {
        return activity;
    }

    public void activity(float activity) {
        this.activity = activity;
    }

    public float instability() {
        return instability;
    }

    public void instability(float instability) {
        this.instability = instability;
    }

    public float capacity() {
        return capacity;
    }

    public void capacity(float capacity) {
        this.capacity = capacity;
    }

    public void add(float amount) {
        this.amount += amount;
        checkCapacity();
    }

    public void add(float amount, float activity) {
        this.activity = this.activity / 2f + activity / 2f;
        add(amount);
    }

    public void add(float amount, float activity, float instability) {
        this.instability += instability;
        add(amount, activity);
    }

    protected void checkCapacity() {
        amount = Math.min(amount, capacity);
    }
}

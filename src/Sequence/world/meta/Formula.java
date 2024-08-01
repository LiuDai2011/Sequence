package Sequence.world.meta;

import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;

public class Formula {
    public final ItemStack[] inputItem;
    public final LiquidStack[] inputLiquid;
    public final float inputPower;
    public final ItemStack[] outputItem;
    public final LiquidStack[] outputLiquid;
    public final float outputPower;
    public final float time;

    public Formula(ItemStack[] inputItem, LiquidStack[] inputLiquid, float inputPower, ItemStack[] outputItem, LiquidStack[] outputLiquid, float outputPower, float time) {
        this.inputItem = inputItem;
        this.inputLiquid = inputLiquid;
        this.inputPower = inputPower;
        this.outputItem = outputItem;
        this.outputLiquid = outputLiquid;
        this.outputPower = outputPower;
        this.time = time;
    }
}

package Sequence.world.meta;

import Sequence.world.meta.imagine.ImagineEnergyRecord;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;

public class Formula {
    public final ItemStack[] inputItem;
    public final LiquidStack[] inputLiquid;
    public final float inputPower;
    public final ImagineEnergyRecord inputImagine;
    public final ItemStack[] outputItem;
    public final LiquidStack[] outputLiquid;
    public final float outputPower;
    public final ImagineEnergyRecord outputImagine;
    public final float time;
    public final boolean liquidSecond;

    public Formula(ItemStack[] inputItem, LiquidStack[] inputLiquid, float inputPower, ImagineEnergyRecord inputImagine, ItemStack[] outputItem, LiquidStack[] outputLiquid, float outputPower, ImagineEnergyRecord outputImagine, float time, boolean liquidSecond) {
        this.inputItem = inputItem;
        this.inputLiquid = inputLiquid;
        this.inputPower = inputPower;
        this.inputImagine = inputImagine;
        this.outputItem = outputItem;
        this.outputLiquid = outputLiquid;
        this.outputPower = outputPower;
        this.outputImagine = outputImagine;
        this.time = time;
        this.liquidSecond = liquidSecond;
    }

    public Formula(ItemStack[] inputItem, LiquidStack[] inputLiquid, float inputPower, ItemStack[] outputItem, LiquidStack[] outputLiquid, float outputPower, float time, boolean liquidSecond) {
        this(inputItem, inputLiquid, inputPower, ImagineEnergyRecord.empty(), outputItem, outputLiquid, outputPower, ImagineEnergyRecord.empty(), time, liquidSecond);
    }

    public Formula(ItemStack[] inputItem, LiquidStack[] inputLiquid, float inputPower, ItemStack[] outputItem, LiquidStack[] outputLiquid, float outputPower, float time) {
        this(inputItem, inputLiquid, inputPower, outputItem, outputLiquid, outputPower, time, false);
    }
}

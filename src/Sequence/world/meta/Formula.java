package Sequence.world.meta;

import Sequence.world.meta.imagine.ImagineEnergyModule;
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
    public final ImagineEnergyModule.ImagineEnergyRecord inputImagineEnergy;
    public final ImagineEnergyModule.ImagineEnergyRecord outputImagineEnergy;

    public Formula(ItemStack[] inputItem, LiquidStack[] inputLiquid, float inputPower, ItemStack[] outputItem, LiquidStack[] outputLiquid, float outputPower, float time, ImagineEnergyModule.ImagineEnergyRecord inputImagineEnergy, ImagineEnergyModule.ImagineEnergyRecord outputImagineEnergy) {
        this.inputItem = inputItem;
        this.inputLiquid = inputLiquid;
        this.inputPower = inputPower;
        this.outputItem = outputItem;
        this.outputLiquid = outputLiquid;
        this.outputPower = outputPower;
        this.time = time;
        this.inputImagineEnergy = inputImagineEnergy;
        this.outputImagineEnergy = outputImagineEnergy;
    }

    public Formula(ItemStack[] inputItem, LiquidStack[] inputLiquid, float inputPower, ItemStack[] outputItem, LiquidStack[] outputLiquid, float outputPower, float time) {
        this(inputItem, inputLiquid, inputPower, outputItem, outputLiquid, outputPower, time, ImagineEnergyModule.ImagineEnergyRecord.empty(), ImagineEnergyModule.ImagineEnergyRecord.empty());
    }
}

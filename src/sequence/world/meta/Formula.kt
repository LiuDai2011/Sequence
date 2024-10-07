package sequence.world.meta

import mindustry.type.ItemStack
import mindustry.type.LiquidStack
import sequence.world.meta.imagine.ImagineEnergyRecord

class Formula(
    val inputItem: Array<ItemStack>,
    val inputLiquid: Array<LiquidStack>,
    val inputPower: Float,
    val inputImagine: ImagineEnergyRecord,
    val outputItem: Array<ItemStack>,
    val outputLiquid: Array<LiquidStack>,
    val outputPower: Float,
    val outputImagine: ImagineEnergyRecord,
    val time: Float,
    val liquidSecond: Boolean
) {
    @JvmOverloads
    constructor(
        inputItem: Array<ItemStack>,
        inputLiquid: Array<LiquidStack>,
        inputPower: Float,
        outputItem: Array<ItemStack>,
        outputLiquid: Array<LiquidStack>,
        outputPower: Float,
        time: Float,
        liquidSecond: Boolean = false
    ) : this(
        inputItem,
        inputLiquid,
        inputPower,
        ImagineEnergyRecord.empty,
        outputItem,
        outputLiquid,
        outputPower,
        ImagineEnergyRecord.empty,
        time,
        liquidSecond
    )
}

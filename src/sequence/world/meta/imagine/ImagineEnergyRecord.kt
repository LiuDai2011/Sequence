package sequence.world.meta.imagine

import arc.math.Mathf

data class ImagineEnergyRecord(var amount: Float) {
    fun zero() = Mathf.zero(amount)

    companion object {
        val empty = ImagineEnergyRecord(0f)
    }
}

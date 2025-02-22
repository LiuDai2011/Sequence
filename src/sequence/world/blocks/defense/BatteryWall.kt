package sequence.world.blocks.defense

import arc.func.Prov
import arc.math.Mathf
import arc.struct.Seq
import mindustry.gen.Building
import mindustry.world.meta.BlockStatus
import mindustry.world.meta.Env

class BatteryWall(name: String) : SqWall(name) {
    init {
        outputsPower = true
        consumesPower = true
        canOverdrive = false
        envEnabled = envEnabled or Env.space
        buildType = Prov { BatteryWallBuild() }
    }

    override fun order() = 157

    override fun statValue() = null

    inner class BatteryWallBuild : WallBuild() {
        override fun warmup(): Float {
            return power.status
        }

        override fun overwrote(previous: Seq<Building>) {
            for (other in previous) {
                if (other.power != null && other.block.consPower != null && other.block.consPower.buffered) {
                    val amount = other.block.consPower.capacity * other.power.status
                    power.status = Mathf.clamp(power.status + amount / consPower.capacity)
                }
            }
        }

        override fun status(): BlockStatus {
            if (Mathf.equal(power.status, 0f, 0.001f)) return BlockStatus.noInput
            return if (Mathf.equal(power.status, 1f, 0.001f)) BlockStatus.active else BlockStatus.noOutput
        }
    }
}
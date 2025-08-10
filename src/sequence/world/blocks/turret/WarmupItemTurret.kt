package sequence.world.blocks.turret

import arc.func.Prov
import arc.util.Time
import mindustry.entities.bullet.BulletType
import mindustry.world.meta.Stat
import mindustry.world.meta.StatUnit
import sequence.core.SqStatValues
import sequence.util.replace
import sequence.world.entities.WarmupBulletType
import sequence.world.meta.SqStat

class WarmupItemTurret(name: String) : SqItemTurret(name) {
    var shootNeededWarmup = 20f

    init {
        reload = 0f
        buildType = Prov { MultiBlockTurretBuild() }
    }

    override fun setStats() {
        super.setStats()
        stats.remove(Stat.ammo)
        stats.add(SqStat.ammoOverride, SqStatValues.ammo(ammoTypes))
        stats.replace(
            Stat.reload,
            60f / (minWarmup * shootNeededWarmup) * shoot.shots,
            StatUnit.perSecond
        )
    }

    inner class MultiBlockTurretBuild : ItemTurretBuild() {
        var accumulatedWarmup = 0f
        var accumulatedWarmupTimes = 0f

        override fun updateTile() {
            super.updateTile()
            accumulatedWarmup += shootWarmup * Time.delta
            accumulatedWarmup = accumulatedWarmup.coerceAtLeast(0f)
        }

        override fun updateShooting() {
            val type = peekAmmo() ?: return
            val warmupType = (type as? WarmupBulletType)?.warmupBulletType
            warmupType?.also {
                while (accumulatedWarmup >= shootNeededWarmup) {
                    shoot(it)
                    accumulatedWarmup -= shootNeededWarmup
                    accumulatedWarmupTimes++
                }
            }
            if (!charging() && accumulatedWarmupTimes >= minWarmup) {
                shoot(type)
                accumulatedWarmupTimes -= minWarmup
            }
        }

        override fun useAmmo(): BulletType? {
            if (ammo.isEmpty) return (peekAmmo() as? WarmupBulletType)?.warmupBulletType
            return super.useAmmo()
        }

//        override fun write(write: Writes?) {
//            super.write(write)
//            write!!.f(accumulatedWarmup)
//        }
//
//        override fun read(read: Reads?) {
//            super.read(read)
//            accumulatedWarmup = read!!.f()
//        }
    }
}
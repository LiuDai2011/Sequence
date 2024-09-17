package sequence.core

import mindustry.mod.ClassMap
import sequence.content.SqLiquids
import sequence.world.blocks.defense.BatteryWall
import sequence.world.blocks.defense.SqShieldWall
import sequence.world.blocks.defense.UnionWall
import sequence.world.entities.SpreadPointBulletType

object ClassPutter {
    operator fun invoke() {
        ClassMap.classes.apply {
            put("SpreadPointBulletType", SpreadPointBulletType::class.java)
            put("BatteryWall", BatteryWall::class.java)
            put("SqShieldWall", SqShieldWall::class.java)
            put("UnionWall", UnionWall::class.java)
            put("VectorizedFluid", SqLiquids.VectorizedFluid::class.java)
        }
    }
}
package sequence.content

import arc.graphics.Color
import arc.scene.ui.layout.Table
import arc.util.Strings
import arc.util.Time
import mindustry.content.Fx
import mindustry.entities.abilities.*
import mindustry.gen.MechUnit
import mindustry.gen.Sounds
import mindustry.graphics.Pal
import mindustry.type.UnitType
import mindustry.type.Weapon
import mindustry.ui.Bar
import sequence.core.SeqElem
import sequence.util.EntityRegister
import sequence.util.IgnoredLocalName
import sequence.util.build
import sequence.util.register
import sequence.world.entities.abilities.TriArcShieldAbility
import sequence.world.entities.units.generator.Gen1Entity
import kotlin.math.floor

object SqUnitTypes {
    lateinit var gen1: SqUnitType

    fun load() {
        object : SqUnitType("test-register"), IgnoredLocalName {
            init {
                abilities.add(TriArcShieldAbility())
                hitSize = 4f * 8f
                speed = 15f
                weapons.add(Weapon("oh-no0").register {
                    top = false
                    x = -2.5f
                    y = 2.5f
                    reload = 30f
                    ejectEffect = Fx.none
                    recoil = 2f
                    shootSound = Sounds.missile
                    velocityRnd = 0.5f
                    inaccuracy = 15f
                    alternate = true

                    bullet = SqBulletTypes.havocPC
                })
                weapons.add(Weapon("oh-no1").register {
                    top = false
                    x = 2.5f
                    y = 2.5f
                    reload = 55f
                    ejectEffect = Fx.none
                    recoil = 2f
                    shootSound = Sounds.missile
                    velocityRnd = 0.5f
                    inaccuracy = 15f

                    bullet = SqBulletTypes.executor
                })
                weapons.add(Weapon("oh-no1").register {
                    top = false
                    x = 0f
                    y = -2.5f
                    reload = 75f
                    ejectEffect = Fx.none
                    recoil = 2f
                    shootSound = Sounds.missile
                    velocityRnd = 0.5f
                    inaccuracy = 15f

                    bullet = SqBulletTypes.finality
                })
            }
        }.build { TestEntity() }
        gen1 = SqUnitType("gen1").register {
            flying = true
            health = 5000000f
            armor = 350f
            hitSize = 12f * 8f
            speed = 0.5f
            abilities.add(RegenAbility().register {
                percentAmount = 0.5f / 60f
                amount = 800f / 60f
            })
            abilities.add(EnergyFieldAbility(250f / 60f, 1.5f * 60f, 180f))
            abilities.add(ForceFieldAbility(140f, 400f / 60f, 10000000f, 0.7f * 60f))
            abilities.add(RepairFieldAbility(450f / 60f, 1.5f * 60f, 185f))
            abilities.add(ShieldRegenFieldAbility(500f, 750000f, 2f * 60f, 145f))
            abilities.add(StatusFieldAbility(SqStatusEffects.repair, 1f * 60f, 3.5f * 60f, 155f))
        }.build { Gen1Entity() }
    }

    class TestEntity : MechUnit() {
        var timer0 = 0f
        var times = 0
        var accDmg = 0f

        val timePerTimes = 12f

        override fun classId() = EntityRegister.getID(this::class.java)

        override fun update() {
            super.update()
            timer0 += Time.delta
            times += 5 * floor(timer0 / timePerTimes).toInt()
            timer0 %= timePerTimes
//            Damage.damage(team, x, y, 130f, accDmg)
        }

        override fun rawDamage(amount: Float) {
            if (times > 0) {
                times--
                accDmg += amount
                maxHealth = accDmg.coerceAtLeast(200f)
                health += amount
                health = health.coerceAtMost(maxHealth)
                return
            }
            super.rawDamage(amount)
        }

        override fun display(table: Table?) {
            super.display(table)
            table?.table { bt ->
                bt.defaults().growX().height(20f).pad(4f)
                bt.row()
                bt.add("TestEntity").row()
                bt.add(Bar(
                    { "Timer: ${Strings.fixed(timer0, 2)}" },
                    { Pal.accent },
                    { timer0 / timePerTimes }
                )).growX().row()
                bt.add(Bar(
                    { "Times: $times" },
                    { Pal.heal },
                    { times.toFloat() / 80 }
                ).blink(Color.white)).growX().row()
                bt.add(Bar(
                    { "AccDmg: ${Strings.fixed(accDmg, 2)}" },
                    { Pal.lightOrange },
                    { accDmg / 100f }
                ).blink(Color.white)).growX().row()
            }?.growX()
        }
    }

    class SqUnitType(name: String) : UnitType(name), SeqElem {
        var ord = -1
        override val order: Int
            get() = ord
    }
}

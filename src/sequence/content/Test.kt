package sequence.content

object Test {
    fun load() {
//        val classes = arrayOf(
//            Class.forName("mindustry.world.blocks.Attributes"),
//            Class.forName("mindustry.world.blocks.Autotiler"),
//            Class.forName("mindustry.world.blocks.ConstructBlock"),
//            Class.forName("mindustry.world.blocks.ControlBlock"),
//            Class.forName("mindustry.world.blocks.ExplosionShield"),
//            Class.forName("mindustry.world.blocks.ItemSelection"),
//            Class.forName("mindustry.world.blocks.LaunchAnimator"),
//            Class.forName("mindustry.world.blocks.RotBlock"),
//            Class.forName("mindustry.world.blocks.UnitTetherBlock"),
//            Class.forName("mindustry.world.blocks.campaign.Accelerator"),
//            Class.forName("mindustry.world.blocks.campaign.LandingPad"),
//            Class.forName("mindustry.world.blocks.campaign.LaunchPad"),
//            Class.forName("mindustry.world.blocks.defense.AutoDoor"),
//            Class.forName("mindustry.world.blocks.defense.BaseShield"),
//            Class.forName("mindustry.world.blocks.defense.BuildTurret"),
//            Class.forName("mindustry.world.blocks.defense.DirectionalForceProjector"),
//            Class.forName("mindustry.world.blocks.defense.Door"),
//            Class.forName("mindustry.world.blocks.defense.ForceProjector"),
//            Class.forName("mindustry.world.blocks.defense.MendProjector"),
//            Class.forName("mindustry.world.blocks.defense.OverdriveProjector"),
//            Class.forName("mindustry.world.blocks.defense.Radar"),
//            Class.forName("mindustry.world.blocks.defense.RegenProjector"),
//            Class.forName("mindustry.world.blocks.defense.ShieldWall"),
//            Class.forName("mindustry.world.blocks.defense.ShockMine"),
//            Class.forName("mindustry.world.blocks.defense.ShockwaveTower"),
//            Class.forName("mindustry.world.blocks.defense.Thruster"),
//            Class.forName("mindustry.world.blocks.defense.Wall"),
//            Class.forName("mindustry.world.blocks.defense.turrets.BaseTurret"),
//            Class.forName("mindustry.world.blocks.defense.turrets.ContinuousLiquidTurret"),
//            Class.forName("mindustry.world.blocks.defense.turrets.ContinuousTurret"),
//            Class.forName("mindustry.world.blocks.defense.turrets.ItemTurret"),
//            Class.forName("mindustry.world.blocks.defense.turrets.LaserTurret"),
//            Class.forName("mindustry.world.blocks.defense.turrets.LiquidTurret"),
//            Class.forName("mindustry.world.blocks.defense.turrets.PayloadAmmoTurret"),
//            Class.forName("mindustry.world.blocks.defense.turrets.PointDefenseTurret"),
//            Class.forName("mindustry.world.blocks.defense.turrets.PowerTurret"),
//            Class.forName("mindustry.world.blocks.defense.turrets.ReloadTurret"),
//            Class.forName("mindustry.world.blocks.defense.turrets.TractorBeamTurret"),
//            Class.forName("mindustry.world.blocks.defense.turrets.Turret"),
//            Class.forName("mindustry.world.blocks.distribution.ArmoredConveyor"),
//            Class.forName("mindustry.world.blocks.distribution.BufferedItemBridge"),
//            Class.forName("mindustry.world.blocks.distribution.ChainedBuilding"),
//            Class.forName("mindustry.world.blocks.distribution.Conveyor"),
//            Class.forName("mindustry.world.blocks.distribution.DirectionalUnloader"),
//            Class.forName("mindustry.world.blocks.distribution.DirectionBridge"),
//            Class.forName("mindustry.world.blocks.distribution.DirectionLiquidBridge"),
//            Class.forName("mindustry.world.blocks.distribution.Duct"),
//            Class.forName("mindustry.world.blocks.distribution.DuctBridge"),
//            Class.forName("mindustry.world.blocks.distribution.DuctJunction"),
//            Class.forName("mindustry.world.blocks.distribution.DuctRouter"),
//            Class.forName("mindustry.world.blocks.distribution.ItemBridge"),
//            Class.forName("mindustry.world.blocks.distribution.Junction"),
//            Class.forName("mindustry.world.blocks.distribution.MassDriver"),
//            Class.forName("mindustry.world.blocks.distribution.OverflowDuct"),
//            Class.forName("mindustry.world.blocks.distribution.OverflowGate"),
//            Class.forName("mindustry.world.blocks.distribution.Router"),
//            Class.forName("mindustry.world.blocks.distribution.Sorter"),
//            Class.forName("mindustry.world.blocks.distribution.StackConveyor"),
//            Class.forName("mindustry.world.blocks.distribution.StackRouter"),
//            Class.forName("mindustry.world.blocks.environment.AirBlock"),
//            Class.forName("mindustry.world.blocks.environment.Cliff"),
//            Class.forName("mindustry.world.blocks.environment.EmptyFloor"),
//            Class.forName("mindustry.world.blocks.environment.Floor"),
//            Class.forName("mindustry.world.blocks.environment.OreBlock"),
//            Class.forName("mindustry.world.blocks.environment.OverlayFloor"),
//            Class.forName("mindustry.world.blocks.environment.Prop"),
//            Class.forName("mindustry.world.blocks.environment.RemoveOre"),
//            Class.forName("mindustry.world.blocks.environment.RemoveWall"),
//            Class.forName("mindustry.world.blocks.environment.SeaBush"),
//            Class.forName("mindustry.world.blocks.environment.Seaweed"),
//            Class.forName("mindustry.world.blocks.environment.ShallowLiquid"),
//            Class.forName("mindustry.world.blocks.environment.SpawnBlock"),
//            Class.forName("mindustry.world.blocks.environment.StaticTree"),
//            Class.forName("mindustry.world.blocks.environment.StaticWall"),
//            Class.forName("mindustry.world.blocks.environment.SteamVent"),
//            Class.forName("mindustry.world.blocks.environment.TallBlock"),
//            Class.forName("mindustry.world.blocks.environment.TreeBlock"),
//            Class.forName("mindustry.world.blocks.environment.WobbleProp"),
//            Class.forName("mindustry.world.blocks.heat.HeatBlock"),
//            Class.forName("mindustry.world.blocks.heat.HeatConductor"),
//            Class.forName("mindustry.world.blocks.heat.HeatConsumer"),
//            Class.forName("mindustry.world.blocks.heat.HeatProducer"),
//            Class.forName("mindustry.world.blocks.legacy.LegacyBlock"),
//            Class.forName("mindustry.world.blocks.legacy.LegacyCommandCenter"),
//            Class.forName("mindustry.world.blocks.legacy.LegacyMechPad"),
//            Class.forName("mindustry.world.blocks.legacy.LegacyUnitFactory"),
//            Class.forName("mindustry.world.blocks.liquid.ArmoredConduit"),
//            Class.forName("mindustry.world.blocks.liquid.Conduit"),
//            Class.forName("mindustry.world.blocks.liquid.LiquidBlock"),
//            Class.forName("mindustry.world.blocks.liquid.LiquidBridge"),
//            Class.forName("mindustry.world.blocks.liquid.LiquidJunction"),
//            Class.forName("mindustry.world.blocks.liquid.LiquidRouter"),
//            Class.forName("mindustry.world.blocks.logic.CanvasBlock"),
//            Class.forName("mindustry.world.blocks.logic.LogicBlock"),
//            Class.forName("mindustry.world.blocks.logic.LogicDisplay"),
//            Class.forName("mindustry.world.blocks.logic.MemoryBlock"),
//            Class.forName("mindustry.world.blocks.logic.MessageBlock"),
//            Class.forName("mindustry.world.blocks.logic.SwitchBlock"),
//            Class.forName("mindustry.world.blocks.logic.TileableLogicDisplay"),
//            Class.forName("mindustry.world.blocks.payloads.BlockProducer"),
//            Class.forName("mindustry.world.blocks.payloads.BuildPayload"),
//            Class.forName("mindustry.world.blocks.payloads.Constructor"),
//            Class.forName("mindustry.world.blocks.payloads.Payload"),
//            Class.forName("mindustry.world.blocks.payloads.PayloadBlock"),
//            Class.forName("mindustry.world.blocks.payloads.PayloadConveyor"),
//            Class.forName("mindustry.world.blocks.payloads.PayloadDeconstructor"),
//            Class.forName("mindustry.world.blocks.payloads.PayloadLoader"),
//            Class.forName("mindustry.world.blocks.payloads.PayloadMassDriver"),
//            Class.forName("mindustry.world.blocks.payloads.PayloadRouter"),
//            Class.forName("mindustry.world.blocks.payloads.PayloadSource"),
//            Class.forName("mindustry.world.blocks.payloads.PayloadUnloader"),
//            Class.forName("mindustry.world.blocks.payloads.PayloadVoid"),
//            Class.forName("mindustry.world.blocks.payloads.UnitPayload"),
//            Class.forName("mindustry.world.blocks.power.Battery"),
//            Class.forName("mindustry.world.blocks.power.BeamNode"),
//            Class.forName("mindustry.world.blocks.power.ConsumeGenerator"),
//            Class.forName("mindustry.world.blocks.power.HeaterGenerator"),
//            Class.forName("mindustry.world.blocks.power.ImpactReactor"),
//            Class.forName("mindustry.world.blocks.power.LightBlock"),
//            Class.forName("mindustry.world.blocks.power.LongPowerNode"),
//            Class.forName("mindustry.world.blocks.power.NuclearReactor"),
//            Class.forName("mindustry.world.blocks.power.PowerBlock"),
//            Class.forName("mindustry.world.blocks.power.PowerDiode"),
//            Class.forName("mindustry.world.blocks.power.PowerDistributor"),
//            Class.forName("mindustry.world.blocks.power.PowerGenerator"),
//            Class.forName("mindustry.world.blocks.power.PowerGraph"),
//            Class.forName("mindustry.world.blocks.power.PowerNode"),
//            Class.forName("mindustry.world.blocks.power.SolarGenerator"),
//            Class.forName("mindustry.world.blocks.power.ThermalGenerator"),
//            Class.forName("mindustry.world.blocks.power.VariableReactor"),
//            Class.forName("mindustry.world.blocks.production.AttributeCrafter"),
//            Class.forName("mindustry.world.blocks.production.BeamDrill"),
//            Class.forName("mindustry.world.blocks.production.BurstDrill"),
//            Class.forName("mindustry.world.blocks.production.Drill"),
//            Class.forName("mindustry.world.blocks.production.Fracker"),
//            Class.forName("mindustry.world.blocks.production.GenericCrafter"),
//            Class.forName("mindustry.world.blocks.production.HeatCrafter"),
//            Class.forName("mindustry.world.blocks.production.Incinerator"),
//            Class.forName("mindustry.world.blocks.production.ItemIncinerator"),
//            Class.forName("mindustry.world.blocks.production.Pump"),
//            Class.forName("mindustry.world.blocks.production.Separator"),
//            Class.forName("mindustry.world.blocks.production.SingleBlockProducer"),
//            Class.forName("mindustry.world.blocks.production.SolidPump"),
//            Class.forName("mindustry.world.blocks.production.WallCrafter"),
//            Class.forName("mindustry.world.blocks.sandbox.ItemSource"),
//            Class.forName("mindustry.world.blocks.sandbox.ItemVoid"),
//            Class.forName("mindustry.world.blocks.sandbox.LiquidSource"),
//            Class.forName("mindustry.world.blocks.sandbox.LiquidVoid"),
//            Class.forName("mindustry.world.blocks.sandbox.PowerSource"),
//            Class.forName("mindustry.world.blocks.sandbox.PowerVoid"),
//            Class.forName("mindustry.world.blocks.storage.CoreBlock"),
//            Class.forName("mindustry.world.blocks.storage.StorageBlock"),
//            Class.forName("mindustry.world.blocks.storage.Unloader"),
//            Class.forName("mindustry.world.blocks.units.DroneCenter"),
//            Class.forName("mindustry.world.blocks.units.Reconstructor"),
//            Class.forName("mindustry.world.blocks.units.RepairTower"),
//            Class.forName("mindustry.world.blocks.units.RepairTurret"),
//            Class.forName("mindustry.world.blocks.units.UnitAssembler"),
//            Class.forName("mindustry.world.blocks.units.UnitAssemblerModule"),
//            Class.forName("mindustry.world.blocks.units.UnitBlock"),
//            Class.forName("mindustry.world.blocks.units.UnitCargoLoader"),
//            Class.forName("mindustry.world.blocks.units.UnitCargoUnloadPoint"),
//            Class.forName("mindustry.world.blocks.units.UnitFactory")
//        )
//        classes.sortBy { it.name }
//        classes.forEach {
//            SqLog.debug("")
//            SqLog.debug("")
//            SqLog.debug("${it.simpleName}(${it.name})")
//            val modifiers = it.modifiers
//            SqLog.debug("${
//                if (Modifier.isPublic(modifiers)) "public"
//                else if (Modifier.isProtected(modifiers)) "protect"
//                else "private"
//            } ${
//                if (it.isInterface) "interface"
//                else if (it.isEnum) "enum"
//                else "class"
//            }")
//            if (it.interfaces.isNotEmpty()) {
//                SqLog.debug("implements ${it.interfaces.joinToString { c -> c.name } } }}")
//            }
//            if (it.superclass != null)
//                SqLog.debug("extends ${it.superclass.name}")
//            if (!it.isInterface) {
//                SqLog.debug("constructors")
//                val declaredConstructors = it.declaredConstructors
//                for (constructor in declaredConstructors) {
//                    val modifier = constructor.modifiers
//                    SqLog.debug("${
//                        if (Modifier.isPublic(modifier)) "public"
//                        else if (Modifier.isProtected(modifier)) "protect"
//                        else "private"
//                    } ")
//                }
//            }
//            SqLog.debug("")
//            SqLog.debug("")
//        }
//        Core.app.post {
//            Core.app.exit()
//        }
    }
}
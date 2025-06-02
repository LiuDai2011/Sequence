package sequence.world.draw

import arc.Core
import arc.graphics.Blending
import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.TextureRegion
import mindustry.entities.part.DrawPart
import mindustry.gen.Building
import mindustry.graphics.Drawf
import mindustry.graphics.Layer
import mindustry.world.Block
import mindustry.world.blocks.defense.turrets.Turret
import mindustry.world.blocks.defense.turrets.Turret.TurretBuild
import mindustry.world.draw.DrawTurret

open class DrawTurretGlow @JvmOverloads constructor(
    basePrefix: String = "",
    var color: Color = Color.clear,
    var opacity: Float = 1.0f
) : DrawTurret(basePrefix) {
    var glowLayer = shadowLayer + 0.1f
    lateinit var glow: TextureRegion

    override fun load(block: Block?) {
        super.load(block)
        if (block == null) return
        glow = Core.atlas.find(block.name + "-base-glow")

        if (!glow.found()) glow = Core.atlas.find(basePrefix + "block-glow-" + block.size)
        if (!glow.found() && block.minfo.mod != null) glow =
            Core.atlas.find(block.minfo.mod.name + "-" + basePrefix + "block-glow-" + block.size)
    }

    override fun draw(build: Building?) {
        if (build == null) return

        val turret = build.block as Turret
        val tb = build as TurretBuild

        Draw.z(Layer.turret)

        Draw.rect(base, build.x, build.y)
        Draw.color()

        Draw.z(shadowLayer)

        Drawf.shadow(
            preview,
            build.x + tb.recoilOffset.x - turret.elevation,
            build.y + tb.recoilOffset.y - turret.elevation,
            tb.drawrot()
        )

        Draw.z(glowLayer)

        Drawf.light(tb.x, tb.y, glow, color, opacity)

        Draw.blend(Blending.additive)
        Draw.color(color)
        Draw.alpha(opacity)
        Draw.rect(glow, tb.x, tb.y)
        Draw.reset()
        Draw.blend()

        Draw.z(turretLayer)

        drawTurret(turret, tb)
        drawHeat(turret, tb)

        if (parts.size > 0) {
            if (outline.found()) {
                Draw.z(turretLayer - 0.01f)
                Draw.rect(outline, build.x + tb.recoilOffset.x, build.y + tb.recoilOffset.y, tb.drawrot())
                Draw.z(turretLayer)
            }
            val progress = tb.progress()

            val params = DrawPart.params.set(
                build.warmup(),
                1f - progress,
                1f - progress,
                tb.heat,
                tb.curRecoil,
                tb.charge,
                tb.x + tb.recoilOffset.x,
                tb.y + tb.recoilOffset.y,
                tb.rotation
            )
            for (part in parts) {
                params.setRecoil(if (part.recoilIndex >= 0 && tb.curRecoils != null) tb.curRecoils[part.recoilIndex] else tb.curRecoil)
                part.draw(params)
            }
        }
    }
}
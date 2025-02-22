package sequence.graphic

import arc.graphics.Color
import mindustry.Vars.tilesize
import mindustry.gen.Building
import mindustry.graphics.Drawf

fun dashRectBuild(color: Color, build: Building) =
    Drawf.dashRect(
        color,
        build.x - build.block.size * tilesize / 2,
        build.y - build.block.size * tilesize / 2,
        build.block.size * tilesize.toFloat(), build.block.size * tilesize.toFloat()
    )
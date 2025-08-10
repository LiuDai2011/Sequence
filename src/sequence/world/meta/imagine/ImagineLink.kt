package sequence.world.meta.imagine

import mindustry.gen.Building
import sequence.world.blocks.imagine.ImagineCenter

interface ImagineLink {
    fun toBuild() =
        /* UNSAFE */
        this as Building

    var center: ImagineCenter.ImagineCenterBuild?
}

fun Building.toImagineLink() =
    /* UNSAFE */
    this as ImagineLink

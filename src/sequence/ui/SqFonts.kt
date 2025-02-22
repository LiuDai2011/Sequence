package sequence.ui

import arc.files.Fi
import arc.freetype.FreeTypeFontGenerator
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontData
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontParameter
import arc.func.Cons
import arc.graphics.Texture
import arc.graphics.g2d.Font
import arc.graphics.g2d.TextureRegion
import arc.scene.ui.layout.Scl
import arc.struct.Seq
import arc.util.Eachable
import arc.util.Reflect
import sequence.content.SqIcon
import sequence.core.Unzipper.find
import sequence.core.Unzipper.findAll
import sequence.core.Unzipper.unzip
import sequence.core.Unzipper.version

object SqFonts {
    lateinit var seqIcon: Font
    lateinit var seq: Font
    lateinit var jetbrainsMono: FontFamily
    private val cons = Cons { param: FreeTypeFontParameter ->
        param.size = Scl.scl(param.size.toFloat()).toInt()
        param.magFilter = Texture.TextureFilter.linear
        param.minFilter = Texture.TextureFilter.linear
    }

    fun loadFamily(name: String, ftfp: (Fi) -> FreeTypeFontParameter): FontFamily {
        val all = findAll { it.name().startsWith("$name-") }
        check(all != null) { "Cannot find the family ${name}." }
        var res = FontFamily()
        for (font in all) {
            val file = unzip(font, version[font.name()])
            res.fonts.add(
                loadFont(file, ftfp(file)) info StandardInformation(
                    file.name(),
                    file.name().lowercase().contains("bold"),
                    file.name().lowercase().contains("italic")
                )
            )
        }
        return res
    }

    fun loadFonts() {
        val iconFile = unzip(
            find("seqicon.ttf"),
            version["seqicon.ttf"]
        )
        seq = loadFont(iconFile, object : FreeTypeFontParameter() {
            init {
                size = 48
                incremental = false
                characters = "\u0000" + SqIcon.all
            }
        })
        seqIcon = loadFont(iconFile, object : FreeTypeFontParameter() {
            init {
                size = 30
                incremental = true
                characters = "\u0000" + SqIcon.all
            }
        })
        jetbrainsMono = loadFamily("JetBrainsMono") { FreeTypeFontParameter() }
    }

    fun loadFont(file: Fi?, parameter: FreeTypeFontParameter): Font {
        cons[parameter]
        val generator = FreeTypeFontGenerator(file)
        val data = FreeTypeFontData()
        generator.generateData(parameter, data)
        val regions = Reflect.get<Seq<TextureRegion>>(FreeTypeFontData::class.java, data, "regions")
        val font = Font(data, regions, false)
        font.setOwnsTexture(parameter.packer == null)
        return font
    }

    abstract class FontInformation {
        abstract val name: String
        abstract val bold: Boolean
        abstract val italic: Boolean
    }

    class StandardInformation(override val name: String, override val bold: Boolean, override val italic: Boolean) :
        FontInformation()

    data class FontEntry(
        val font: Font,
        val info: FontInformation = StandardInformation("Unnamed", bold = false, italic = false)
    ) {
        constructor(font: Font, name: String, bold: Boolean, italic: Boolean) :
                this(font, StandardInformation(name, bold, italic))
    }

    infix fun Font.info(info: FontInformation) = FontEntry(this, info)
    class FontFamily(
        val fonts: Seq<FontEntry> = Seq()
    ) : Iterable<FontEntry> by fonts, Eachable<FontEntry> by fonts
}

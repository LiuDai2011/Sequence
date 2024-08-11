package Sequence.ui;

import Sequence.content.SqIcon;
import Sequence.core.Unzipper;
import arc.files.Fi;
import arc.freetype.FreeTypeFontGenerator;
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontData;
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Reflect;

public class SqFonts {
    public static Font seqIcon;

    public static void loadFonts() {
        String name = "seqicon";
        Fi fontFi = Unzipper.unzip(Unzipper.find(name + ".ttf"), Unzipper.version.get(name + ".ttf"));
        SqFonts.seqIcon = loadFont(fontFi, new FreeTypeFontParameter() {{
            size = 42;
            incremental = false;
            characters = "\0" + SqIcon.all;
        }});
    }

    public static Font loadFont(Fi file, FreeTypeFontParameter parameter) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
        FreeTypeFontData data = new FreeTypeFontData();
        generator.generateData(parameter, data);

        Seq<TextureRegion> regions = Reflect.get(FreeTypeFontData.class, data, "regions");
        Font font = new Font(data, regions, false);
        font.setOwnsTexture(parameter.packer == null);
        return font;
    }
}

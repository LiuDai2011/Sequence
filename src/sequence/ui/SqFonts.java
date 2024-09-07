package sequence.ui;

import sequence.content.SqIcon;
import sequence.core.Unzipper;
import arc.files.Fi;
import arc.freetype.FreeTypeFontGenerator;
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontData;
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import arc.func.Cons;
import arc.graphics.Texture;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import arc.util.Reflect;

public class SqFonts {
    public static Font seqIcon, seq;
    protected static Cons<FreeTypeFontParameter> cons = param -> {
        param.size = (int) (Scl.scl(param.size));
        param.magFilter = Texture.TextureFilter.linear;
        param.minFilter = Texture.TextureFilter.linear;
//        param.packer = UI.packer;
    };

    public static void loadFonts() {
        Fi iconFile = Unzipper.unzip(
                Unzipper.find("seqicon.ttf"),
                Unzipper.version.get("seqicon.ttf"));
        SqFonts.seq = loadFont(iconFile,
                new FreeTypeFontParameter() {{
                    size = 48;
                    incremental = false;
                    characters = "\0" + SqIcon.all;
                }});
        SqFonts.seqIcon = loadFont(iconFile,
                new FreeTypeFontParameter() {{
                    size = 30;
                    incremental = true;
                    characters = "\0" + SqIcon.all;
                }});
    }

    public static Font loadFont(Fi file, FreeTypeFontParameter parameter) {
        cons.get(parameter);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);
        FreeTypeFontData data = new FreeTypeFontData();
        generator.generateData(parameter, data);

        Seq<TextureRegion> regions = Reflect.get(FreeTypeFontData.class, data, "regions");
        Font font = new Font(data, regions, false);
        font.setOwnsTexture(parameter.packer == null);
        return font;
    }
}

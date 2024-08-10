package Sequence.ui;

import Sequence.content.SqIcon;
import Sequence.core.Unzipper;
import arc.Core;
import arc.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import arc.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import arc.graphics.g2d.Font;

public class SqFonts {
    public static Font seqIcon;

    public static void loadFonts() {
        FreeTypeFontParameter fontParameters = new FreeTypeFontParameter() {{
            size = 48;
            incremental = false;
            characters = "\0" + SqIcon.all;
        }};
        String path = Unzipper.unzip(Unzipper.find("seqicon.ttf"), Unzipper.version.get("seqicon.ttf")).absolutePath();
        FreeTypeFontLoaderParameter parameter = new FreeTypeFontLoaderParameter(path, fontParameters);
        Core.assets.load("seq-icon", Font.class, parameter)
                .loaded = f -> SqFonts.seqIcon = f;
    }
}

package Sequence.content;

import Sequence.core.SqLog;
import Sequence.ui.SqFonts;
import arc.Core;
import arc.graphics.g2d.TextureAtlas;
import arc.scene.style.TextureRegionDrawable;
import mindustry.gen.Icon;
import mindustry.ui.Fonts;

public class SqIcon {
    public static TextureRegionDrawable imagineEnergy, mainMenu;
    public static final String all = "\ue800";

    static {
        imagineEnergy = new TextureRegionDrawable(Core.atlas.find("seq-imagine-energy"));
        Icon.icons.put("imagineEnergy", imagineEnergy);
        mainMenu = Fonts.getGlyph(SqFonts.seqIcon, '\ue800');
        Icon.icons.put("seqMainMenu", mainMenu);
        for (int i = 0; i < 80000; i++) {
            if (SqFonts.seqIcon.getData().getGlyph((char) i) != null) {
                SqLog.info(((char) i) + " " + i);
            }
        }
    }
}

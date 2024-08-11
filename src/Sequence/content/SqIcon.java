package Sequence.content;

import Sequence.ui.SqFonts;
import arc.scene.style.TextureRegionDrawable;
import mindustry.gen.Icon;
import mindustry.ui.Fonts;

public class SqIcon {
    public static final String all = "\ue800\ue801";
    public static TextureRegionDrawable imagineEnergy, mainMenu;

    static {
        mainMenu = Fonts.getGlyph(SqFonts.seqIcon, '\ue800');
        Icon.icons.put("seqMainMenu", mainMenu);
        imagineEnergy = Fonts.getGlyph(SqFonts.seqIcon, '\ue801');
        Icon.icons.put("imagineEnergy", imagineEnergy);
    }
}

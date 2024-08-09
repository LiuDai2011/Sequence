package Sequence.content;

import Sequence.core.SqLog;
import arc.Core;
import arc.scene.style.TextureRegionDrawable;
import mindustry.gen.Icon;

public class SqIcon {
    public static TextureRegionDrawable imagineEnergy, mainMenu;

    static {
        imagineEnergy = new TextureRegionDrawable(Core.atlas.find("seq-imagine-energy"));
        Icon.icons.put("imagineEnergy", imagineEnergy);
        mainMenu = new TextureRegionDrawable(Core.atlas.find("seq-main-menu-icon"));
        Icon.icons.put("seqMainMenu", mainMenu);
    }
}

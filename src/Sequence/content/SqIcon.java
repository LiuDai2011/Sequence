package Sequence.content;

import arc.Core;
import arc.scene.style.TextureRegionDrawable;
import mindustry.gen.Icon;

public class SqIcon {
    public static TextureRegionDrawable imagineEnergy;

    static {
        imagineEnergy = new TextureRegionDrawable(Core.atlas.find("seq-imagine-energy"));
        Icon.icons.put("imagineEnergy", imagineEnergy);
    }
}

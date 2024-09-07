package sequence.ui.research;

import arc.scene.ui.Image;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

public class SqResearchDialog extends BaseDialog {
    public ResearchCanvas canvas;

    public SqResearchDialog() {
        super("");

        margin(0);
        clear();

        stack(
                new Image(Styles.black5),
                canvas = new ResearchCanvas()
        ).grow().pad(0f).margin(0f);

        addCloseButton();
    }
}

package Sequence.content;

import Sequence.ui.SqFonts;
import Sequence.ui.SqUI;

public class SqContent {
    public static void load() {
        SqFonts.loadFonts();

        SqItems.load();
        SqLiquids.load();
        SqBlocks.load();

        SqOverride.setup();
        SqUI.load();
    }
}

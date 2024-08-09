package Sequence.content;

import Sequence.ui.SqUI;

public class SqContent {
    public static void load() {
        SqItems.load();
        SqLiquids.load();
        SqBlocks.load();

        SqOverride.setup();
        SqUI.load();
    }
}

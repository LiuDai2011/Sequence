package Sequence.content;

import Sequence.SqLog;

public class SqContent {
    public static void load() {
        SqItems.load();
        SqLiquids.load();
        SqOverride.setup();
    }
}

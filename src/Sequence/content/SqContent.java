package Sequence.content;

public class SqContent {
    public static void load() {
        SqItems.load();
        SqLiquids.load();
        SqOverride.setup();
        SqBlocks.load();
    }
}

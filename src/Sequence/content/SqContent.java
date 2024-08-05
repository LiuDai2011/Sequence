package Sequence.content;

public class SqContent {
    public static void load() {
        SqItems.load();
        SqLiquids.load();
        SqBlocks.load();

        SqOverride.setup();
    }
}

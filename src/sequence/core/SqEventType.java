package sequence.core;

import mindustry.ctype.UnlockableContent;

public class SqEventType {
    public static class ContentStatInitEvent {
        public final UnlockableContent content;

        public ContentStatInitEvent(UnlockableContent content) {
            this.content = content;
        }
    }
}

package Sequence.content;

import Sequence.*;
import arc.Events;
import mindustry.game.EventType;

import static mindustry.Vars.ui;

public class SqContent {
    public static void load() {
        SqItems.load();
        Events.on(EventType.ClientLoadEvent.class, ignore -> {
            ui.content = new SqContentInfoDialog();

            Events.on(SqEventType.ContentStatInitEvent.class, e -> {
                if (e.content instanceof SeqElem elem) {
                    e.content.stats.useCategories = true;
                    e.content.stats.add(SqStat.sequenceOrder, elem.order() == -1 ? SqBundle.modCat("seq-null") : String.valueOf(elem.order()));
                }
            });
        });
    }
}

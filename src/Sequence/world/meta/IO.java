package Sequence.world.meta;

import arc.util.io.Reads;
import arc.util.io.Writes;

public interface IO {
    void read(Reads read);
    void write(Writes write);
}

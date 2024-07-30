package Sequence;

public interface SeqElem {
    default int order() {
        return -1;
    }
}

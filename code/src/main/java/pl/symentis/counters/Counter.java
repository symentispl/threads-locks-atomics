package pl.symentis.counters;

public class Counter {
    private int counter;

    void inc() {
        counter++;
    }

    int value() {
        return counter;
    }
}

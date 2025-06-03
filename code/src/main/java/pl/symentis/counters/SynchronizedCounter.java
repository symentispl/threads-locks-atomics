package pl.symentis.counters;

public class SynchronizedCounter {
    private int counter;

    synchronized void inc() {
        counter++;
    }

    synchronized int value() {
        return counter;
    }
}

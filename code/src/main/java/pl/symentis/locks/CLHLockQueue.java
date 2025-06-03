package pl.symentis.locks;

import java.util.concurrent.atomic.AtomicReference;

public class CLHLockQueue {
    private final AtomicReference<QNode> tail = new AtomicReference<>(new QNode());
    private final ThreadLocal<QNode> currentThreadNode = ThreadLocal.withInitial(QNode::new);
    private final ThreadLocal<QNode> currentThreadPred = new ThreadLocal<>();

    public void lock() {
        QNode qnode = currentThreadNode.get();
        qnode.locked = true;
        QNode pred = tail.getAndSet(qnode);
        currentThreadPred.set(pred);
        while (pred.locked) {
            Thread.yield();
        }
    }

    public void unlock() {
        QNode qnode = currentThreadNode.get();
        qnode.locked = false;
        currentThreadNode.set(currentThreadPred.get());
    }

    private static class QNode {
        volatile boolean locked = false;
    }
}

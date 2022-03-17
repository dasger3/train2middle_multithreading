package task4;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Pool that block when it has not any items or it full
 */
public class BlockingObjectPool {
    private final BlockingQueue pool;
    private final ReentrantLock lock = new ReentrantLock();
    private int createdObjects = 0;
    private final int size;
    /**
     * Creates filled pool of passed size * * @param size of pool
     */
    public BlockingObjectPool(int size) {
        pool = new ArrayBlockingQueue<>(size, true);
        this.size = size;
        lock.lock();
        createPool();
    }

    /**
     * Gets object from pool or blocks if pool is empty * * @return object from pool
     */
    public Object get() throws InterruptedException {
        if (!lock.isLocked()) {
            if (lock.tryLock()) {
                try {
                    ++createdObjects;
                    return new Object();
                } finally {
                    if (createdObjects < size) lock.unlock();
                }
            }
        }
        return pool.take();
    }

    /**
     * Puts object to pool or blocks if pool is full * * @param object to be taken back to pool
     */
    public void take(Object object) {
        pool.add(object);
    }

    public void createPool() {
        if (lock.isLocked()) {
            for (int i = 0; i < size; ++i) {
                pool.add(new Object());
                createdObjects++;
            }
        }
    }
}
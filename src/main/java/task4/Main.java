package task4;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static BlockingObjectPool blockingObjectPool;

    public static void main(String[] args) {

        blockingObjectPool = new BlockingObjectPool(2);

        ExecutorService exec = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 30; i++) {
            exec.execute(() -> {
                try {
                    test();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        exec.shutdown();
    }

    public static void test () throws InterruptedException {
        Object tmp = blockingObjectPool.get();
        // some actions with tmp
        blockingObjectPool.take(tmp);
    }
}

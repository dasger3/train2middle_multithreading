package task1;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainSynchronizedMap {

    public static void main(String[] args) {

        Map<Integer, Integer> m = new HashMap<>();
        Map<Integer, Integer> map = Collections.synchronizedMap(m);
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                map.put(i, i);
            }
        });
        Thread thread2 = new Thread(() -> {
            int sum = 0;
            synchronized (map) {
                for (Map.Entry<Integer, Integer> e : map.entrySet()) {
                    sum += e.getValue();
                    System.out.println(sum);
                }
            }
        });
        thread1.start();
        try {
            Thread.sleep(100);
        } catch (Exception ignored) {}
        thread2.start();
    }
}

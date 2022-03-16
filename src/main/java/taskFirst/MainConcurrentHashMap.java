package taskFirst;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MainConcurrentHashMap {
    public static void main(String[] args) {
        Map<Integer, Integer> map = new ConcurrentHashMap<>();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                map.put(i, i);
            }
        });
        Thread thread2 = new Thread(() -> {
            int sum = 0;
            Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();
            while (iterator.hasNext() && thread1.isAlive()) {
                sum += iterator.next().getValue();
                System.out.println(sum);
            }
        });
        thread1.start();
        thread2.start();
    }
}


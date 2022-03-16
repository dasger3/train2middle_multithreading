package taskFirst;

import java.util.HashMap;
import java.util.Map;

public class MainHashMap {
    public static void main(String[] args) {

        Map<Integer, Integer> map = new HashMap<>();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                map.put(i, i);
            }
        });
        Thread thread2 = new Thread(() -> {
            int sum = 0;
            for (Map.Entry<Integer, Integer> e : map.entrySet()) {
                sum += e.getValue();
                System.out.println(sum);
            }
        });
        thread1.start();
        /*try {
            Thread.sleep(10);
        } catch (Exception ignored) {}*/
        thread2.start();

    }
}


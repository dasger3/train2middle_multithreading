package taskFirst;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    private static final int N = 10000;
    static int[] tmp;
    static int sumResult;
    static Map<Integer, Integer> map;

    private static class FirstThread extends Thread {

        public void run() {
            for (int i = 0; i < N; i++) {
                map.put(i, tmp[i]);
            }
        }

    }

    private static class SecondThread extends Thread {

        public void run() {
            Set<Integer> s = map.keySet();
            for (Integer t : s) {
                sumResult += map.get(t);
            }
        }
    }

    public void start() throws InterruptedException {
        tmp = new int[N];
        for (int i = 0; i < N; i++) {
            tmp[i] = (int) (Math.random() * 100);
        }
        int sumRight = Arrays.stream(tmp).sum();

        map = new ConcurrentHashMap<>();

        Thread thread1 = new FirstThread();
        Thread thread2 = new SecondThread();

        thread1.start();

        thread2.start();

        Thread.sleep(1000);
        System.out.println(sumResult + "   " + sumRight);
    }

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        main.start();
    }
}


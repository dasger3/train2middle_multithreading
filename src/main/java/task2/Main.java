package task2;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        List<Integer> elements = new LinkedList<>();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; /*i < 100000*/true; i++) {
                synchronized (elements) {
                    elements.add(random.nextInt(1000));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            synchronized (elements) {
                int sum = 0;
                for (Integer element : elements) {
                    sum += element;
                    System.out.println("Sum: " + sum);
                }
            }
        });
        Thread thread3 = new Thread(() -> {
            synchronized (elements) {
                int sum = 0;
                for (Integer element : elements) {
                    sum += element * element;
                    System.out.println("Square root of sum of squares of all numbers: " + Math.sqrt(sum));
                }
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
        Thread.sleep(5000);
        System.out.println(elements);
    }
}

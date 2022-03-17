package task3;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ProducerConsumer {
    private static final int BUFFER_MAX_SIZE = 42;
    private List<String> buffer = new LinkedList<>();

    synchronized void produce() throws InterruptedException {
        while (buffer.size() == BUFFER_MAX_SIZE) {
            wait();
        }
        buffer.add(generateMessage());
        notify();
    }

    synchronized void consume() throws InterruptedException {
        while (buffer.size() == 0) {
            wait();
        }
        String result = buffer.remove(0);
        System.out.println("Message: " + result);
        notify();
    }

    private String generateMessage() {
        Random random = new Random();

        return random.ints(97, 123)
                .limit(10)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

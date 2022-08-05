package person.companion.queue;

import org.junit.Test;

/**
 * 功能描述：
 *
 * author: companion
 * Write by: 2021/7/15 13:22
 */
public class Main {
    @Test
    public void test() {
        QueueArray<Integer> queue = new QueueArray<>();
        for (int i = 0; i < 10; i++) {
            queue.enQueue(i);
            if (i > 6) {
                queue.deQueue();
            }

            System.out.println(queue);
        }
    }
}

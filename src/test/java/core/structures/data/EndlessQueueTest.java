package core.structures.data;

import core.structures.data.exceptions.EndlessQueueException;
import core.structures.data.impl.EndlessQueue;
import core.structures.data.support.EndlessQueueResolver;
import core.support.ExtTestWatcher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({EndlessQueueResolver.class, ExtTestWatcher.class})
public class EndlessQueueTest {

    @Test
    @DisplayName("Новые элементы должны добавляться в конец очереди")
    public void queuingTest(EndlessQueue<Integer> queue) {
        for (int i = 1; i < 10; i++) {
            queue.push(i);
        }

        for (int i = 1; i < 10; i++) {
            assertEquals(
                    i,
                    queue.poll(),
                    "element in queue"
            );
        }
    }

    @Test
    @DisplayName("Null не должен добавляться в очередь")
    public void nullElementTest(EndlessQueue<String> queue) {
        queue.push(null);

        assertEquals(
                0,
                queue.size(),
                "size"
        );
    }

    @Test
    @DisplayName("Получение элемента из пустой очереди")
    public void readingFromAnEmpty(EndlessQueue<Object> queue) {
        var exception = assertThrows(
                EndlessQueueException.class,
                queue::poll,
                "exception"
        );

        assertEquals(
                "Нет элементов в очереди",
                exception.getMessage(),
                "error message"
        );
    }

    @Test
    @DisplayName("Размер очереди не может быть больше максимального размера очереди")
    public void maxSizeTest(EndlessQueue<Object> queue) {
        var expectedQueueSize = queue.maxSize();

        for (int i = 0; i < expectedQueueSize + 1; i++) {
            queue.push(new Object());
        }

        assertEquals(
                expectedQueueSize,
                queue.size(),
                "size"
        );
    }
}

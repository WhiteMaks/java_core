package core.structures.data.impl;

import core.structures.data.Queue;
import core.structures.data.exceptions.EndlessQueueException;

import java.util.ArrayDeque;

public class EndlessQueue<T> implements Queue<T> {
    private final java.util.Queue<T> queue;

    private final int maxSize;

    public EndlessQueue(int maxSize) {
        this.maxSize = maxSize;

        queue = new ArrayDeque<>();
    }

    @Override
    public void push(T element) {
        if (element == null) {
            return;
        }

        queue.add(element);

        trimQueue();
    }

    @Override
    public T poll() {
        if (size() == 0) {
            throw new EndlessQueueException("Нет элементов в очереди");
        }

        return queue.poll();
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public int maxSize() {
        return maxSize;
    }
}

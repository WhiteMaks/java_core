package core.structures.data;

public interface Queue<T> {

    void push(T element);

    T poll();

    int size();

    default int maxSize() {
        return 0;
    }

    default void trimQueue() {
        while (size() > maxSize()) {
            poll();
        }
    }
}

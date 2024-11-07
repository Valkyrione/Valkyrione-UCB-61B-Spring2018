package synthesizer;

public interface BoundedQueue<T> extends Iterable<T> {
    int capacity();
    int fillCount(); // Returns number of current items
    void enqueue(T x); // Adds item to the end
    T dequeue(); // Deletes and returns item from the front
    T peek(); // Returns item from the front
    default boolean isEmpty() {
        int itemCount = fillCount();
        return itemCount == 0;
    }
    default boolean isFull() {
        int capacity = capacity();
        int itemCount = fillCount();
        return capacity == itemCount;
    }
}

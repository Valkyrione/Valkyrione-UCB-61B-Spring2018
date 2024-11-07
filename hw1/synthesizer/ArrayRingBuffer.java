
package synthesizer;

import java.util.Iterator;

// Make sure to make this class and all of its methods public
// Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    @SuppressWarnings("unchecked")
    public ArrayRingBuffer(int capacity) {
        //  Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
        rb = (T[]) new Object[capacity];
        this.first = 0;
        this.last = 0;
        this.fillCount = 0;
        this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        //  Enqueue the item. Don't forget to increase fillCount and update last.
        if (isFull()) {
            throw new RuntimeException("Ring buffer overflow");
        }
        rb[last] = x;
        last++;
        if (last == capacity) {
            last = 0;
        }
        fillCount++;
    }

    /**
     * Dequeue the oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    @Override
    public T dequeue() {
        //  Dequeue the first item. Don't forget to decrease fillCount and update
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer underflow");
        }
        T removedItem = rb[first];
        rb[first] = null;
        first++;
        if (first == capacity) {
            first = 0;
        }
        fillCount--;
        return removedItem;
    }

    /**
     * Return oldest item, but don't remove it.
     */
    @Override
    public T peek() {
        //  Return the first item. None of your instance variables should change.
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer empty");
        }
        return rb[first];
    }

    //  When you get to part 5, implement the needed code to support iteration.
    public Iterator<T> iterator() {
        return new ArrayRingBufferIterator();
    }

    private class ArrayRingBufferIterator implements Iterator<T> {
        private int wizPos;
        private int numItemsRecovered;
        ArrayRingBufferIterator() {
            wizPos = first;
            numItemsRecovered = 0;
        }

        @Override
        public T next() {
            T returnItem = rb[wizPos];
            wizPos++;
            if (wizPos == capacity) {
                wizPos = 0;
            }
            numItemsRecovered++;
            return returnItem;
        }

        @Override
        public boolean hasNext() {
            return numItemsRecovered < fillCount;
        }
    }
}

public class LinkedListDeque<T> {
    private static class LLDNode<T> {
        private T item;
        private LLDNode<T> last;
        private LLDNode<T> next;

        private LLDNode(LLDNode<T> last, T item, LLDNode<T> next) {
            this.last = last;
            this.item = item;
            this.next = next;
        }
    }

    private LLDNode<T> sentinel;
    private int size;

    public LinkedListDeque() {
        // constructs empty LLD
        sentinel = new LLDNode<>(null, null, null);
        size = 0;
    }

    private void linkTwoNodes(LLDNode<T> front, LLDNode<T> back) {
        front.next = back;
        back.last = front;
    }

    public void addFirst(T item) {
        // constant time, no looping or recursion
        LLDNode<T> newFirst = new LLDNode<>(null, item, null);
        if (size == 0) {
            linkTwoNodes(sentinel, newFirst);
            linkTwoNodes(newFirst, sentinel);
        } else {
            LLDNode<T> oldFirst = sentinel.next;
            linkTwoNodes(sentinel, newFirst);
            linkTwoNodes(newFirst, oldFirst);
        }
        size++;
    }

    public void addLast(T item) {
        // constant time, no looping or recursion
        LLDNode<T> newLast = new LLDNode<>(null, item, null);
        if (size == 0) {
            linkTwoNodes(newLast, sentinel);
            linkTwoNodes(sentinel, newLast);
        } else {
            LLDNode<T> oldLast = sentinel.last;
            linkTwoNodes(newLast, sentinel);
            linkTwoNodes(oldLast, newLast);
        }
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        // constant time
        return size;
    }

    public void printDeque() {
        if (size == 0) {
            System.out.println("Error: empty deque.");
        }
        LLDNode<T> pointer = sentinel.next;
        String result = String.valueOf(pointer.item);
        pointer = pointer.next;
        while (pointer != sentinel) {
            result = result + " " + pointer.item;
            pointer = pointer.next;
        }
        System.out.println(result);
    }

    public T removeFirst() {
        // constant time, no looping or recursion
        // Don't keep reference of removed items to save memory
        if (size == 0) {
            return null;
        }
        T itemOfFirst = sentinel.next.item;
        if (size == 1) {
            sentinel.next = null;
            sentinel.last = null;
        } else if (size > 1) {
            linkTwoNodes(sentinel, sentinel.next.next);
        }
        size--;
        return itemOfFirst;
    }

    public T removeLast() {
        // constant time, no looping or recursion
        // Don't keep reference of removed items to save memory
        if (size == 0) {
            return null;
        }
        T itemOfLast = sentinel.last.item;
        if (size == 1) {
            sentinel.next = null;
            sentinel.last = null;
        } else if (size > 1) {
            linkTwoNodes(sentinel.last.last, sentinel);
        }
        size--;
        return itemOfLast;
    }

    public T get(int index) {
        // use iteration instead of recursion
        if (index > size - 1) {
            return null;
        }
        LLDNode<T> pointer = sentinel.next;
        for (int i = 0; i < index; i++) {
            pointer = pointer.next;
        }
        return pointer.item;
    }

    public T getRecursive(int index) {
        // same as get() but recursive
        return getRecursiveHelper(sentinel.next, index);
    }

    private T getRecursiveHelper(LLDNode<T> current, int positionLeft) {
        if (positionLeft == 0) {
            return current.item;
        } else {
            return getRecursiveHelper(current.next, positionLeft - 1);
        }
    }
}

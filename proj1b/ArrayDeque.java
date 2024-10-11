public class ArrayDeque<T> implements Deque<T> {
    private T[] arr;
    private static final int INITIALSIZE = 8;
    private static final int UPSIZEFACTOR = 2;
    private static final double DOWNSIZEFACTOR = 0.25;
    private int arraySize;
    private int dequeSize;
    private int frontPointer;
    private int endPointer;

    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        arr = (T[]) new Object[INITIALSIZE];
        arraySize = INITIALSIZE;
        dequeSize = 0;
        frontPointer = -1;
        endPointer = -1;
    }

    @SuppressWarnings("unchecked")
    private void upSize() {
        int newSize = arraySize * UPSIZEFACTOR;
        T[] newArr = (T[]) new Object[newSize];
        int frontPointerNewArray = -1;
        int endPointerNewArray = -1;
        if (frontPointer <= endPointer && dequeSize != 0) {
            System.arraycopy(arr, frontPointer, newArr, 0, dequeSize);
            endPointerNewArray = dequeSize - 1;
            frontPointerNewArray = 0;
        } else if (frontPointer > endPointer && dequeSize != 0) {
            System.arraycopy(arr, frontPointer, newArr, 0, arraySize - frontPointer);
            endPointerNewArray = arraySize - frontPointer - 1;
            frontPointerNewArray = 0;
            System.arraycopy(arr, 0, newArr, endPointerNewArray + 1, endPointer + 1);
            endPointerNewArray += endPointer + 1;
        }

        arr = newArr;
        arraySize = newSize;
        frontPointer = frontPointerNewArray;
        endPointer = endPointerNewArray;
    }

    @SuppressWarnings("unchecked")
    private void downSize() {
        int newSize = (int) Math.ceil(arraySize * DOWNSIZEFACTOR);
        T[] newArr = (T[]) new Object[newSize];
        int frontPointerNewArray = -1;
        int endPointerNewArray = -1;
        if (frontPointer <= endPointer && dequeSize != 0) {
            System.arraycopy(arr, frontPointer, newArr, 0, dequeSize);
            endPointerNewArray = dequeSize - 1;
            frontPointerNewArray = 0;
        } else if (frontPointer > endPointer && dequeSize != 0) {
            System.arraycopy(arr, frontPointer, newArr, 0, arraySize - frontPointer);
            endPointerNewArray = arraySize - frontPointer - 1;
            frontPointerNewArray = 0;
            System.arraycopy(arr, 0, newArr, endPointerNewArray + 1, endPointer + 1);
            endPointerNewArray += endPointer + 1;
        }
        arr = newArr;
        arraySize = newSize;
        frontPointer = frontPointerNewArray;
        endPointer = endPointerNewArray;
    }

    public void addFirst(T item) {
        if (dequeSize == arraySize) {
            upSize();
        }

        if (dequeSize == 0) {
            arr[0] = item;
            frontPointer = 0;
            endPointer = 0;
        } else {
            frontPointer -= 1;
            if (frontPointer == -1) {
                frontPointer = arraySize - 1;
            }
            arr[frontPointer] = item;
        }
        dequeSize++;
    }

    public void addLast(T item) {
        if (dequeSize == arraySize) {
            upSize();
        }

        if (dequeSize == 0) {
            arr[0] = item;
            frontPointer = 0;
            endPointer = 0;
        } else {
            endPointer += 1;
            if (endPointer == arraySize) {
                endPointer = 0;
            }
            arr[endPointer] = item;
        }
        dequeSize++;
    }

    public boolean isEmpty() {
        return dequeSize == 0;
    }

    public int size() {
        return dequeSize;
    }

    public void printDeque() {
        if (dequeSize == 0) {
            System.out.println("Error: empty deque.");
        }
        String result = String.valueOf(arr[frontPointer]);
        int pointer = frontPointer;
        while (pointer != endPointer) {
            pointer += 1;
            if (pointer == arraySize) {
                pointer = 0;
            }
            result = result + " " + arr[pointer];
        }
        System.out.println(result);
    }

    public T removeFirst() {
        if (dequeSize == 0) {
            return null;
        }
        T item = arr[frontPointer];
        frontPointer += 1;
        if (frontPointer == arraySize) {
            frontPointer = 0;
        }
        dequeSize -= 1;

        if (dequeSize == 0) {
            frontPointer = -1;
            endPointer = -1;
        }

        int potentialNewSize = (int) Math.ceil(arraySize * DOWNSIZEFACTOR);
        if (dequeSize < potentialNewSize) {
            downSize();
        }
        return item;
    }

    public T removeLast() {
        if (dequeSize == 0) {
            return null;
        }
        T item = arr[endPointer];
        endPointer -= 1;
        if (endPointer == -1) {
            endPointer = arraySize - 1;
        }
        dequeSize -= 1;

        if (dequeSize == 0) {
            frontPointer = -1;
            endPointer = -1;
        }

        int potentialDownSize = (int) Math.ceil(arraySize * DOWNSIZEFACTOR);
        if (dequeSize <= potentialDownSize) {
            downSize();
        }
        return item;
    }

    public T get(int index) {
        int arrayIndex = frontPointer + index;
        if (arrayIndex >= arraySize) {
            arrayIndex -= arraySize;
        }
        return arr[arrayIndex];
    }
}

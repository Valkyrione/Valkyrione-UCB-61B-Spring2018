public class Sort {
    /** Sorts strings destructively. */
    public static void sort(String[] x) {
        sort(x, 0);
    }

    //** Sorts strings destructively starting from item start. */
    private static void sort(String[] x, int start) {
        if (start == x.length) {
            return;
        }
        int smallestIndex = findSmallest(x, start);
        swap(x, start, smallestIndex);
        sort(x, start + 1);
    }

    /** Returns the smallest string in x. */
    public static int findSmallest(String[] x, int start) {
        int smallestIndex = start;
        for (int i = start + 1; i < x.length; i++) {
            int compareResult = x[i].compareTo(x[smallestIndex]);
            if (compareResult < 0) {
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }

    /** Swap two elements in the list. */
    public static void swap(String[] x, int a, int b) {
        String temp = x[a];
        x[a] = x[b];
        x[b] = temp;
    }
}
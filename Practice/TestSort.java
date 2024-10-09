/** For textbook ch 3.1 */
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the Sort class. */
public class TestSort {
    @Test
    public void testFindSmallest() {
        String[] input = {"i", "have", "an", "egg"};
        int expected = 2;

        int actual = Sort.findSmallest(input, 0);
        assertEquals(expected, actual);

        String[] input2 = {"there", "are", "many", "pigs"};
        int expected2 = 2;

        int actual2 = Sort.findSmallest(input2, 2);
        assertEquals(expected2, actual2);
    }

    @Test
    public void testSwap() {
        String[] input = {"i", "have", "an", "egg"};
        int a = 0;
        int b = 2;
        String[] expected = {"an", "have", "i", "egg"};

        Sort.swap(input, a, b);
        assertArrayEquals(expected, input);
    }
}

class Sort {
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
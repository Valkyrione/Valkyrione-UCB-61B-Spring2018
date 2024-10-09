import org.junit.Test;
import static org.junit.Assert.*;

public class TestArrays {
    @Test
    public void testInsert() {
        // insert to middle
        int[] array1 = {1, 2, 3};
        array1 = Arrays.insert(array1, 4, 2);
        int[] expected1 = {1, 2, 4, 3};
        assertArrayEquals(array1, expected1);

        // insert to front
        array1 = Arrays.insert(array1, 5, 0);
        int[] expected2 = {5, 1, 2, 4, 3};
        assertArrayEquals(array1, expected2);

        // insert to position too large
        array1 = Arrays.insert(array1, 6, 100);
        int[] expected3 = {5, 1, 2, 4, 3, 6};
        assertArrayEquals(array1, expected3);

        // insert to empty array
        int[] array2 = new int[0];
        array2 = Arrays.insert(array2, 999, 10);
        int[] expected4 = {999};
        assertArrayEquals(array2, expected4);
    }

    @Test
    public void testReverse() {
        // even number of items
        int[] array1 = {1, 2, 3, 4};
        Arrays.reverse(array1);
        int[] expected1 = {4, 3, 2, 1};
        assertArrayEquals(array1, expected1);

        // odd number of items
        int[] array2 = {1, 2, 3, 4, 5};
        Arrays.reverse(array2);
        int[] expected2 = {5, 4, 3, 2, 1};
        assertArrayEquals(array2, expected2);

        // empty array
        int[] array3 = new int[0];
        Arrays.reverse(array3);
        int[] expected3 = new int[0];
        assertArrayEquals(array3, expected3);
    }

    @Test
    public void testReplicate() {
        // normal
        int[] array1 = {3, 2, 1};
        array1 = Arrays.replicate(array1);
        int[] expected1 = {3, 3, 3, 2, 2, 1};
        assertArrayEquals(array1, expected1);

        // only 1s
        int[] array2 = {1, 1, 1};
        array2 = Arrays.replicate(array2);
        int[] expected2 = {1, 1, 1};
        assertArrayEquals(array2, expected2);

        // with 0
        int[] array3 = {3, 0, 1};
        array3 = Arrays.replicate(array3);
        int[] expected3 = {3, 3, 3, 1};
        assertArrayEquals(array3, expected3);

        // only 0s
        int[] array4 = {0, 0, 0};
        array4 = Arrays.replicate(array4);
        int[] expected4 = new int[0];
        assertArrayEquals(array4, expected4);

        // empty array
        int[] array5 = new int[0];
        array5 = Arrays.replicate(array5);
        int[] expected5 = new int[0];
        assertArrayEquals(array5, expected5);
    }
}

class Arrays {
    public static int[] insert(int[] arr, int item, int position) {
        int[] result = new int[arr.length + 1];
        if (position > result.length) {
            position = result.length - 1;
        }
        System.arraycopy(arr, 0, result, 0, position);
        result[position] = item;
        System.arraycopy(arr, position, result, position + 1, arr.length - position);
        return result;
    }

    public static void reverse(int[] arr) {
        int temp;
        for (int i = 0; i < arr.length / 2; i ++) {
            temp = arr[i];
            arr[i] = arr[arr.length - 1 - i];
            arr[arr.length - 1 - i] = temp;
        }
    }

    public static int[] replicate(int[] arr) {
        int newLength = 0;
        for (int i = 0; i < arr.length; i ++) {
            newLength += arr[i];
        }

        int[] result = new int[newLength];
        if (newLength == 0) {
            return result;
        }

        int current = 0;
        for (int i = 0; i < arr.length; i ++) {
            for (int j = 0; j < arr[i]; j ++) {
                result[current] = arr[i];
                current ++;
            }
        }
        return result;
    }
}
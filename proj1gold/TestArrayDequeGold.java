import static org.junit.Assert.*;
import org.junit.Test;
public class TestArrayDequeGold {
    @Test
    public void task1() {
        StudentArrayDeque<Integer> test = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solution = new ArrayDequeSolution<>();
        String message = "";
        message += "isEmpty()";
        assertTrue(message, test.isEmpty() && solution.isEmpty());
        for (int i = 0; i < 100; i++) {
            message += "\nsize()";
            int testSize = test.size();
            int solutionSize = solution.size();
            assertEquals(message, testSize, solutionSize);

            int methodSelector = 0;
            if (testSize == 0 || solutionSize == 0) {
                methodSelector = StdRandom.uniform(2);
            } else {
                methodSelector = StdRandom.uniform(4);
            }

            if (methodSelector == 0) {
                message += "\naddFirst(" + i + ")" + "\nget(0)";
                test.addFirst(i);
                solution.addFirst(i);
                assertEquals(message, test.get(0), solution.get(0));
            } else if (methodSelector == 1) {
                message += "\naddLast(" + i + ")" + "\nget(" + testSize + ")";
                test.addLast(i);
                solution.addLast(i);
                assertEquals(message, test.get(testSize), solution.get(testSize));
            } else if (methodSelector == 2) {
                message += "\nremoveFirst()";
                assertEquals(message, test.removeFirst(), solution.removeFirst());
            } else if (methodSelector == 3) {
                message += "\nremoveLast()";
                assertEquals(message, test.removeLast(), solution.removeLast());
            }


        }
    }
}

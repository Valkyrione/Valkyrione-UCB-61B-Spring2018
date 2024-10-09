import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the Sort class. */
public class TestSLList {
    @Test
    public void testOutput() {
        int[] input = {1, 2, 3};
        SLList list1 = new SLList(input);
        int[] output1 = list1.output();
        int[] expected1 = {1, 2, 3};
        assertArrayEquals(output1, expected1);
    }

    @Test
    public void testAddFirst() {
        int[] input1 = {1, 2, 3};
        SLList list1 = new SLList(input1);
        list1.addFirst(0);
        int[] output1 = list1.output();
        int[] expected1 = {0, 1, 2, 3};
        assertArrayEquals(output1, expected1);

        SLList list2 = new SLList();
        list2.addFirst(5);
        int[] output2 = list2.output();
        int[] expected2 = {5};
        assertArrayEquals(output2, expected2);
    }

    @Test
    public void testInsert() {
        // insert into the middle
        int[] input1 = {1, 2, 3};
        SLList list1 = new SLList(input1);
        list1.insert(5, 2);
        int[] output1 = list1.output();
        int[] expected1 = {1, 2, 5, 3};
        assertArrayEquals(output1, expected1);

        // insert at the beginning
        SLList list2 = new SLList(input1);
        list2.insert(5, 0);
        int[] output2 = list2.output();
        int[] expected2 = {5, 1, 2, 3};
        assertArrayEquals(output2, expected2);

        // insert to an empty list
        SLList list3 = new SLList();
        list3.insert(5, 3);
        int[] output3 = list3.output();
        int[] expected3 = {5};
        assertArrayEquals(output3, expected3);

        // insert position behind the end of list
        SLList list4 = new SLList(input1);
        list4.insert(5, 10);
        int[] output4 = list4.output();
        int[] expected4 = {1, 2, 3, 5};
        assertArrayEquals(output4, expected4);
    }

    @Test
    public void testReverse() {
        int[] input = {1, 2, 3, 4, 5};
        SLList list = new SLList(input);
        list.reverseIter();
        int[] outputIter = list.output();
        int[] expectedIter = {5, 4, 3, 2, 1};
        assertArrayEquals(outputIter, expectedIter);
        list.reverseRec();
        int[] outputRec = list.output();
        int[] expectedRec = {1, 2, 3, 4, 5};
        assertArrayEquals(outputRec, expectedRec);
    }
}

class SLList {
    private class IntNode {
        public int item;
        public IntNode next;
        public IntNode(int item, IntNode next) {
            this.item = item;
            this.next = next;
        }
    }

    private IntNode first;
    public int length = 0;

    public SLList(int[] array) {
        IntNode current = null;
        for (int i = array.length; i > 0; i --) {
            current = new IntNode(array[i-1], current);
            first = current;
            length ++;
        }
    }

    public SLList() {
        first = null;
    }

    public int[] output() {
        int[] result = new int[length];
        IntNode current = first;
        for (int i = 0; i < length; i ++) {
            result[i] = current.item;
            current = current.next;
        }
        return result;
    }

    public void addFirst(int x) {
        first = new IntNode(x, first);
        length ++;
    }

    public void insert(int x, int position) {
        if (first == null || position == 0) {
            addFirst(x);
            return;
        }
        IntNode nodeBeforeInsertion = first;
        while (position > 1) { // find the node before the insertion position
            if (nodeBeforeInsertion.next == null) {
                break;
            }
            nodeBeforeInsertion = nodeBeforeInsertion.next;
            position -= 1;
        }
        IntNode inserted = new IntNode(x, nodeBeforeInsertion.next);
        nodeBeforeInsertion.next = inserted;
        length ++;
    }

    public void reverseIter() {
        IntNode current = first;
        IntNode beforeCurrent = null;
        while (current.next != null) {
            IntNode nextFromCurrent = current.next;
            current.next = beforeCurrent;
            beforeCurrent = current;
            current = nextFromCurrent;
        }
        current.next = beforeCurrent;
        first = current;
    }

    public void reverseRec() {
        IntNode lastToBe = first;
        reverseNodesRec(first, first.next);
        lastToBe.next = null;
    }

    /** Reverse the order of all elements recursively */
    private void reverseNodesRec(IntNode firstOfTwo, IntNode secondOfTwo) {
        if (secondOfTwo.next != null) {
            reverseNodesRec(secondOfTwo, secondOfTwo.next);
        } else {
            this.first = secondOfTwo;
        }
        secondOfTwo.next = firstOfTwo;
    }
}
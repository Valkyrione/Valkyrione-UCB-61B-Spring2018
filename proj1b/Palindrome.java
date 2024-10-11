public class Palindrome {
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> result = new ArrayDeque<>();
        if (word == null) {
            return null;
        }
        for (int i = 0; i < word.length(); i++) {
            result.addLast(word.charAt(i));
        }
        return result;
    }

    public boolean isPalindrome(String word) {
        if (word == null) {
            return false;
        }
        Deque<Character> deq = wordToDeque(word);
        return isPalindromeHelper(deq);
    }

    private boolean isPalindromeHelper(Deque<Character> deq) {
        int size = deq.size();
        if (size == 0 || size == 1) {
            return true;
        }

        Character first = deq.removeFirst();
        Character last = deq.removeLast();
        if (first == last) {
            return isPalindromeHelper(deq);
        } else {
            return false;
        }
    }

    public boolean isPalindrome(String word, CharacterComparator cc) {
        if (word == null || cc == null) {
            return false;
        }
        Deque<Character> deq = wordToDeque(word);
        return isPalindromeHelper(deq, cc);
    }

    private boolean isPalindromeHelper(Deque<Character> deq, CharacterComparator cc) {
        int size = deq.size();
        if (size == 0 || size == 1) {
            return true;
        }

        Character first = deq.removeFirst();
        Character last = deq.removeLast();
        if (cc.equalChars(first, last)) {
            return isPalindromeHelper(deq, cc);
        } else {
            return false;
        }
    }
}

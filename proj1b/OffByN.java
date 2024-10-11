public class OffByN implements CharacterComparator {
    private int differenceInChar;

    public OffByN(int N) {
        differenceInChar = N;
    }

    @Override
    public boolean equalChars(char x, char y) {
        return x - y == differenceInChar || y - x == differenceInChar;
    }
}

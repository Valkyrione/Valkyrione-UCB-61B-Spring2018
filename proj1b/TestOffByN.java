import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    static CharacterComparator offBy3 = new OffByN(3);

    @Test
    public void testOffBy3() {
        assertTrue(offBy3.equalChars('a', 'd'));
    }
}

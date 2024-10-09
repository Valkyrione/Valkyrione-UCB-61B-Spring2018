import org.junit.Test;
import static org.junit.Assert.*;

public class FlikTest {
    @Test
    public void testIsSameNumber() {
        assertTrue(Flik.isSameNumber(2, 2));
        assertFalse(Flik.isSameNumber(2, 3));
        assertFalse(Flik.isSameNumber(0, 2));
        assertTrue(Flik.isSameNumber(0, 0));

        assertTrue(Flik.isSameNumber(127, 127));
        Integer a = 127;
        Integer b = 127;
        assertTrue(a == b);
        assertTrue(a.equals(b));
        Integer c = 128;
        Integer d = 128;
        assertTrue(c == d);
        assertTrue(c.equals(d));
    }
}

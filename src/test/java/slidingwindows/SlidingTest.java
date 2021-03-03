package slidingwindows;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SlidingTest
{

    @Test
    public void test() {
        SlidingWindowCounterB counter = new SlidingWindowCounterB(10);

        assertTrue(counter.visit(1, "A").isEmpty());
        assertTrue(counter.visit(2, "B").isEmpty());
        assertTrue(counter.aggregate(3) == 2);
        assertTrue(counter.visit(3, "B").isEmpty());
        assertTrue(counter.aggregate(4) == 2);
        assertTrue(counter.visit(10, "B").isEmpty());
        assertTrue(counter.aggregate(15) == 1);
    }
}

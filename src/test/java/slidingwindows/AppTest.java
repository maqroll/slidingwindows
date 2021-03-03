package slidingwindows;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppTest
{

    @Test
    public void test() {
        SlidingWindowCounterA counter = new SlidingWindowCounterA(10 /* ten seconds */, l -> l/1000 /* per second */);

        long initialBucketId = counter.currentBucketId();
        assertTrue(counter.visit(initialBucketId*1000 +1, "A").isEmpty());
        List<ClosedPeriod> periods = counter.visit(initialBucketId * 1000 + 1000, "A"); // next
        assertTrue(periods.size() == 1);
        assertTrue(periods.get(0).value() == 1);

        periods = counter.visit(initialBucketId * 1000 + 2000, "B");
        assertTrue(periods.size() == 1);
        assertTrue(periods.get(0).value() == 1);

        periods = counter.visit(initialBucketId * 1000 + 3000);
        assertTrue(periods.size() == 1);
        assertTrue(periods.get(0).value() == 2);

        periods = counter.visit(initialBucketId * 1000 + 6000);
        assertTrue(periods.size() == 3);
        for(ClosedPeriod p : periods) {
            assertTrue(p.value() == 2);
        }
    }

    @Test
    public void grow() {
        int depth = 10;
        int[] c = new int[depth];
        int pos = depth-1;

        for (int i=0; i<20; i++) {
            System.out.println(++pos%depth);
        }
    }

    @Test
    public void distance() {
        int depth = 10;
        int[] c = new int[depth];
        int pos = 8;

        for (int i=0; i<depth; i++) {
            System.out.println((depth+pos-i)%depth);
        }
    }

    @Test
    public void mapOrderedAccess() {
        LinkedHashMap<String, Long> orderedMap = new LinkedHashMap<>(16, 0.80f, true);

        long idx = 1;

        orderedMap.put("A", idx);
        orderedMap.put("B", idx++);
        orderedMap.put("C", idx++);

        Iterator<Map.Entry<String, Long>> iterator = orderedMap.entrySet().iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        orderedMap.put("A", idx++);
        iterator = orderedMap.entrySet().iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    @Test
    public void mapOrderedAccessKeySet() {
        LinkedHashMap<String, Long> orderedMap = new LinkedHashMap<>(16, 0.80f, true);

        long idx = 1;

        orderedMap.put("A", idx);
        orderedMap.put("B", idx++);
        orderedMap.put("C", idx++);

        Iterator<String> iterator = orderedMap.keySet().iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }

        orderedMap.put("A", idx++);
        iterator = orderedMap.keySet().iterator();
        while(iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}

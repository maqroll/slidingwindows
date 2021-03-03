package slidingwindows;

import java.util.List;

/**
 * A and B implementations differ in semantic!!
 * This interface is rather cranky.
 */
public interface SlidingWindowCounter {
  List<ClosedPeriod> visit(List<String> ids);
  List<ClosedPeriod> visit();
  List<ClosedPeriod> visit(String id);
}

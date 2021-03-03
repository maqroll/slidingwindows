package slidingwindows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This implementation uses an ORDERED LinkedHashMap to clean old entries WITHOUT running through all entries.
 * PassiveExpiringMap (commons.collection) iterates over ALL entries on cleaning.
 * TODO: experiments on throughput.
 *
 * This implementation produce aggregation on calling aggregate method (SlidingWindowCounterA does while detecting closed
 * periods on visiting).
 */
public class SlidingWindowCounterB implements SlidingWindowCounter {
  private final long depthMs;
  private final Map<String, Long> mostRecentSessionTime; //ORDERED!!!!

  // Expiring slot requires to purge mostRecentSessionTime;
  public SlidingWindowCounterB(long depthMs) {
    this.depthMs = depthMs;

    // IMPORTANT: ordered
    mostRecentSessionTime = new LinkedHashMap<>(100, 0.80f, true);
  }

  private void purgeExpiredEntries(long ts) {
    long cut = ts - depthMs;

    Iterator<Map.Entry<String, Long>> it = mostRecentSessionTime.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, Long> entry = it.next();

      if (entry.getValue() < cut) {
        it.remove();
      } else {
        break;
      }
    }
  }

  public int aggregate(long ts) {
    purgeExpiredEntries(ts);

    return mostRecentSessionTime.size();
  }

  public List<ClosedPeriod> visit(long ts, List<String> ids) {
    for(String id : ids) {
      mostRecentSessionTime.put(id, ts);
    }

    return Collections.emptyList();
  }
  List<ClosedPeriod> visit(long ts) {
    return visit(ts, Collections.emptyList());
  }

  List<ClosedPeriod>  visit(long ts, String id) {
    return visit(ts, new ArrayList<String>(){{add(id);}});
  }

  @Override
  public List<ClosedPeriod> visit(List<String> ids) {
    return visit(System.currentTimeMillis(), ids);
  }

  @Override
  public List<ClosedPeriod> visit() {
    return visit(System.currentTimeMillis());
  }

  @Override
  public List<ClosedPeriod> visit(String id) {
    return visit(System.currentTimeMillis(), id);
  }
}

package slidingwindows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SlidingWindowCounterA implements SlidingWindowCounter {
  // milisecond to bucket id
  // for 1second resolution use /1000
  private final Function<Long, Long> resolutionMapper;
  private final int depth;
  private final Map<String, Long> mostRecentSlotForSession;
  //private final Deque<Set<String>> sessionsForSlot;
  private final long[] counters; // descending with overflow (initially currentBucketId -> counters[depth-1])
  private int currentBucketPos;

  // most recent bucket goes from [current, current + 1)
  private long currentBucketId;

  private long total;

  // Expiring slot requires to purge mostRecentSlotForSession;
  public SlidingWindowCounterA(int depth /* how many buckets to keep */, Function<Long,Long> resolutionMapper) {
    this.resolutionMapper = resolutionMapper;
    this.depth = depth;

    mostRecentSlotForSession = new LRUHashMap<>(1000); // empty
    //sessionsForSlot = new LinkedList<Set<String>>();
    counters = new long[depth];

    total = 0;
    currentBucketId = resolutionMapper.apply(System.currentTimeMillis());
    currentBucketPos = depth - 1;
  }

  List<ClosedPeriod>  visit(long ts, List<String> ids) {
    List<ClosedPeriod> res = new ArrayList<>();

    Long bucketId = resolutionMapper.apply(ts);

    if (bucketId != currentBucketId) {
      // just send expired bucket
      res.add(new ClosedPeriod(currentBucketId, total));

      while(currentBucketId < bucketId) {
        currentBucketId++;
        currentBucketPos = (currentBucketPos+1)%depth;
        total -= counters[currentBucketPos];
        counters[currentBucketPos] = 0L;
        if (bucketId != currentBucketId) {
          res.add(new ClosedPeriod(currentBucketId, total));
        }
      }
    }

    for(String id : ids) {
      Long previousBucket = mostRecentSlotForSession.put(id, currentBucketId);

      if (previousBucket != null) {
        final long distance = bucketId - previousBucket;
        if (distance < depth) {
          counters[(depth+currentBucketPos-(int)distance)%depth]--;
          total--; // optimize??
        }
      }

      counters[currentBucketPos]++;
      total++; // optimize??
    }

    return res;
  }

  long currentBucketId() {
    return currentBucketId;
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

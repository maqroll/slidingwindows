package slidingwindows;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUHashMap<K, V> extends LinkedHashMap<K, V> {

  private int maxSize;

  /**
   * Create a new hash map with a bounded size and with least recently used entries removed.
   *
   * @param maxSize the maximum size (in number of entries) to which the map can grow before the
   *     least recently used entries start being removed.<br>
   *     Setting maxSize to a very large value, like {@link Integer#MAX_VALUE} is allowed, but is
   *     less efficient than using {@link java.util.HashMap} because our class needs to keep track
   *     of the use order (via an additional doubly-linked list) which is not used when the map's
   *     size is always below the maximum size.
   */
  public LRUHashMap(int maxSize) {
    super(16, 0.75f, true);
    this.maxSize = maxSize;
  }

  /** Return the max size */
  public int getMaxSize() {
    return maxSize;
  }

  /**
   * setMaxSize() allows changing the map's maximal number of elements which was defined at
   * construction time.
   *
   * <p>Note that if the map is already larger than maxSize, the current implementation does not
   * shrink it (by removing the oldest elements); Rather, the map remains in its current size as new
   * elements are added, and will only start shrinking (until settling again on the give maxSize) if
   * existing elements are explicitly deleted.
   */
  public void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }

  // We override LinkedHashMap's removeEldestEntry() method. This method
  // is called every time a new entry is added, and if we return true
  // here, the eldest element will be deleted automatically. In our case,
  // we return true if the size of the map grew beyond our limit - ignoring
  // what is that eldest element that we'll be deleting.
  @Override
  protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
    return size() > maxSize;
  }

  @SuppressWarnings("unchecked")
  @Override
  public LRUHashMap<K, V> clone() {
    return (LRUHashMap<K, V>) super.clone();
  }
}

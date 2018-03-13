package cz4013.shared.container;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A fixed-size, key-value based least-recently used cached.
 * <p>
 * When the cache is full, the last key to be accessed (read/write) will be removed.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class LruCache<K, V> {
  private Map<K, V> map;

  /**
   * Constructs a new cache with the given capacity.
   *
   * @param capacity the capacity.
   */
  public LruCache(int capacity) {
    map = Collections.synchronizedMap(new LinkedHashMap<K, V>(capacity, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
      }
    });
  }

  /**
   * Promotes the given key and returns the associated value (if any).
   *
   * @param key the key
   * @return the value
   */
  public Optional<V> get(K key) {
    return Optional.ofNullable(map.get(key));
  }

  /**
   * Adds or replaces a {@code (key, value)} pair and promotes the given key.
   *
   * @param key   the key
   * @param value the value
   */
  public void put(K key, V value) {
    map.put(key, value);
  }
}

package cz4013.shared.container;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class LruCache<K, V> {
  private Map<K, V> map;

  public LruCache(int capacity) {
    map = Collections.synchronizedMap(new LinkedHashMap<K, V>(capacity, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
      }
    });
  }

  public Optional<V> get(K key) {
    return Optional.ofNullable(map.get(key));
  }

  public void put(K key, V value) {
    map.put(key, value);
  }
}

package caching.internal.eviction;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUEvictionPolicy<K> implements EvictionPolicy<K> {

    private final float loadFactor = 0.75f; // map to resize itself once 75% full
    private final boolean accessOrder = true; // reorder map when access operation occurs - head the eldest, tail the youngest

    // value is boolean for placeholder sake. no specific meaning
    // for more info - https://docs.oracle.com/javase/8/docs/api/java/util/LinkedHashMap.html#LinkedHashMap-int-float-boolean-
    private final Map<K, Boolean> tracker;

    public LRUEvictionPolicy(int initialCapacity) {
        this.tracker = new LinkedHashMap<>(initialCapacity, loadFactor, accessOrder); 
    }

    @Override
    public void onInsert(K key) {
        tracker.put(key, true);
    }

    @Override
    public void onAccess(K key) {
        tracker.get(key);
    }

    @Override
    public void onRemove(K key) {
        tracker.remove(key);
    }

    @Override
    public K evict() {
        if(tracker.isEmpty()) return null;

        K eldest =  tracker.keySet().iterator().next();
        tracker.remove(eldest);
        return eldest;
    }

    @Override
    public void clear() {
        tracker.clear();
    }

    @Override
    public int size() {
        return tracker.size();
    }
}

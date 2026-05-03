package eviction_policy;

import java.util.LinkedHashMap;
import java.util.Map;

public class FIFOEvictionPolicy<K>implements EvictionPolicy<K> {

    private final float loadFactor = 0.75f; // map to resize itself once 75% full
    private final boolean accessOrder = false; // insertion order retains - head the eldest, tail the youngest
    private final Map<K,Boolean> queue;

    public FIFOEvictionPolicy(int initialCapacity) {
        this.queue = new LinkedHashMap<>(initialCapacity, loadFactor, accessOrder);
    }

    @Override
    public void onInsert(K key) {
        queue.put(key, true);
    }

    @Override
    public void onAccess(K key) {}

    @Override
    public void onRemove(K key) {
        queue.remove(key);
    }

    @Override
    public K evict() {
        if(queue.isEmpty()) return null;

        K eldest = queue.keySet().iterator().next();
        queue.remove(eldest);
        return eldest;
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public int size() {
        return queue.size();
    }
}

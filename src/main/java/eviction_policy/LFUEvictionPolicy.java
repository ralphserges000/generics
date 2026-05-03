package eviction_policy;

import java.util.LinkedHashMap;
import java.util.Map;

public class LFUEvictionPolicy<K> implements EvictionPolicy<K> {

    private final Map<K, Integer> tracker;

    private final float loadFactor = 0.75f; 
    private final boolean accessOrder = false;

    public LFUEvictionPolicy(int maxCapacity) {
        if(maxCapacity < 1) throw new IllegalArgumentException("maxCapacity does not accept value lesser than 1");
        this.tracker = new LinkedHashMap<>(maxCapacity, loadFactor, accessOrder);
    }

    @Override
    public void onInsert(K key) {
        Integer updatedCount = tracker.getOrDefault(key, 0) + 1;
        tracker.put(key, updatedCount);
    }

    @Override
    public void onAccess(K key) {
        Integer updatedCount = tracker.getOrDefault(key, 0) + 1;
        tracker.put(key, updatedCount);
    }

    @Override
    public void onRemove(K key) {
        tracker.remove(key);
    }

    @Override
    public K evict() {
        if(tracker.isEmpty()) return null;

        K leastFrequentKey = null;
        Integer lowestFrequecy = Integer.MAX_VALUE;

        for(Map.Entry<K,Integer> entry : tracker.entrySet()) {
            K key = entry.getKey();
            Integer freq = entry.getValue();
            
            if(freq < lowestFrequecy) {
                lowestFrequecy = freq;
                leastFrequentKey = key;
            }
        }

        tracker.remove(leastFrequentKey);
        return leastFrequentKey;
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

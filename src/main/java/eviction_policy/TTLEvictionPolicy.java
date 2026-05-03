package eviction_policy;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;


public class TTLEvictionPolicy<K> implements EvictionPolicy<K> {

    private final long ttlMillis;
    private final Map<K, Long> tracker;

    private final float loadFactor = 0.75f; // map to resize itself once 75% full
    private final boolean accessOrder = false;

    public TTLEvictionPolicy(int maxCapacity, long ttlMillis) {
        if(maxCapacity < 1) throw new IllegalArgumentException("maxCapacity does not accept value lesser than 1");
        if(ttlMillis < 1) throw new IllegalArgumentException("ttlMillis does not accept value lesser than 1");
        this.ttlMillis = ttlMillis;
        this.tracker = new LinkedHashMap<>(maxCapacity, loadFactor, accessOrder);
    }

    @Override
    public void onInsert(K key) {
        long insertedAtMillis = System.currentTimeMillis();
        tracker.put(key, ttlMillis + insertedAtMillis);
    }

    @Override
    public void onAccess(K key) {}

    @Override
    public void onRemove(K key) {
        tracker.remove(key);
    }

    @Override
    public K evict() {
        if(tracker.isEmpty()) return null;

        long nowInMillis = System.currentTimeMillis();

        Iterator<Map.Entry<K, Long>> iterator = tracker.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<K, Long> entry = iterator.next();
            if(nowInMillis >= entry.getValue()) {
                K toBeEvicted = entry.getKey();
                iterator.remove();
                return toBeEvicted;
            }
        }

        K eldest = this.tracker.keySet().iterator().next();
        this.tracker.remove(eldest);
        return eldest;
    }

    @Override
    public void clear() {
        this.tracker.clear();
    }

    @Override
    public int size() {
        return this.tracker.size();
    }

    @Override
    public boolean isExpired(K key) {
        Long expiresAtMillis = this.tracker.get(key);
        if(expiresAtMillis == null) return false; 

        long nowInMillis = System.currentTimeMillis();
        return nowInMillis >= expiresAtMillis;
    }
}

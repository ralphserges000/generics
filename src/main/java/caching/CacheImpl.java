package caching;

import java.util.HashMap;
import java.util.Map;

import caching.internal.eviction.EvictionPolicy;

class CacheImpl<K,V> implements Cache<K,V> {

    private final int maxCapacity;
    private final EvictionPolicy<K> evictionPolicy;
    private final Map<K,V> cache = new HashMap<>();
    private int cacheHitCount = 0;
    private int cacheMissCount = 0;

    public CacheImpl(int maxCapacity, EvictionPolicy<K> evictionPolicy) {
        if(maxCapacity < 1) throw new IllegalArgumentException("Invalid maxCapacity argument. value should be >= 1");
        if(evictionPolicy == null) throw new IllegalArgumentException("Eviction policy cannot be null");
        this.maxCapacity = maxCapacity;
        this.evictionPolicy = evictionPolicy;
    }

    @Override
    public synchronized V get(K key) {
        if(!cache.containsKey(key)) {
            cacheMissCount ++;
            return null;
        }

        if(this.evictionPolicy.isExpired(key)) {
            cache.remove(key);
            this.evictionPolicy.onRemove(key);
            cacheMissCount ++;
            return null;
        }

        cacheHitCount ++;
        evictionPolicy.onAccess(key);
        return cache.get(key);
    }

    @Override
    public synchronized void put(K key, V value) {

        if(cache.containsKey(key)) {
            cache.put(key, value);
            evictionPolicy.onInsert(key);
            return; 
        }

        if(cache.size() >= maxCapacity) {
            K toBeEvicted = evictionPolicy.evict();
            if(toBeEvicted != null) {
                cache.remove(toBeEvicted);
            }
        }

        cache.put(key, value);
        evictionPolicy.onInsert(key);
    }

    @Override
    public synchronized V remove(K key) {
        if (cache.containsKey(key)) {
            evictionPolicy.onRemove(key);
        }
        return cache.remove(key);
    }

    @Override
    public synchronized void clear() {
        evictionPolicy.clear();
        cache.clear();
    }

    @Override
    public synchronized int size() {
        return cache.size();
    }

    // the higher the ratio, the more effective our cache is since it is able to provide contain frequently accessed data
    @Override
    public synchronized double getCacheHitRatio() {
        int totalRequest = cacheHitCount + cacheMissCount;
        if(totalRequest == 0) return 0.0;
        return (double) cacheHitCount / totalRequest;
    }
}

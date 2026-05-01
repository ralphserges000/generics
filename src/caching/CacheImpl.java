package caching;

import java.util.HashMap;
import java.util.Map;

import eviction_policy.EvictionPolicy;

public class CacheImpl<K,V> implements Cache<K,V> {

    private final int maxCapacity;
    private final EvictionPolicy<K> evictionPolicy;
    private final Map<K,V> cache = new HashMap<>();
    private int cacheHitCount = 0;
    private int cacheMissCount = 0;

    public CacheImpl(int maxCapacity, EvictionPolicy<K> evictionPolicy) {
        this.maxCapacity = maxCapacity;
        this.evictionPolicy = evictionPolicy;
    }

    @Override
    public V get(K key) {
        V value = cache.get(key); 

        if(value == null) {
            cacheMissCount ++;
            return null;
        }
    
        cacheHitCount ++;
        evictionPolicy.onAccess(key);
        return value;
    }

    @Override
    public void put(K key, V value) {

        if(cache.containsKey(key)) { 
            evictionPolicy.onAccess(key); 
        }
        else {
            if(cache.size() >= maxCapacity) {
                K toBeEvicted = evictionPolicy.evict();
                if(toBeEvicted != null) {
                    cache.remove(toBeEvicted);
                    evictionPolicy.onRemove(toBeEvicted);
                }
            }
            evictionPolicy.onInsert(key);
        }
        cache.put(key, value);
    }

    @Override
    public V remove(K key) {
        evictionPolicy.onRemove(key);
        return cache.remove(key);
    }

    @Override
    public void clear() {
        evictionPolicy.clear();
        cache.clear();
    }

    @Override
    public int size() {
        return cache.size();
    }

    // the higher the ratio, the more effective our cache is since it is able to provide contain frequently accessed data
    public double getCacheHitRatio() {
        int totalRequest = cacheHitCount + cacheMissCount;
        if(totalRequest == 0) return 0.0;
        return (double) cacheHitCount / totalRequest;
    }
}

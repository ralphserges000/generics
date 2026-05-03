package caching;

import caching.internal.eviction.FIFOEvictionPolicy;
import caching.internal.eviction.LFUEvictionPolicy;
import caching.internal.eviction.LRUEvictionPolicy;
import caching.internal.eviction.TTLEvictionPolicy;

public final class CacheFactory {

    private CacheFactory() {
    }

    public static <K, V> Cache<K, V> lru(int maxCapacity) {
        return new CacheImpl<>(maxCapacity, new LRUEvictionPolicy<>(maxCapacity));
    }

    public static <K, V> Cache<K, V> fifo(int maxCapacity) {
        return new CacheImpl<>(maxCapacity, new FIFOEvictionPolicy<>(maxCapacity));
    }

    public static <K, V> Cache<K, V> ttl(int maxCapacity, long ttlMillis) {
        return new CacheImpl<>(maxCapacity, new TTLEvictionPolicy<>(maxCapacity, ttlMillis));
    }

    public static <K, V> Cache<K, V> lfu(int maxCapacity) {
        return new CacheImpl<>(maxCapacity, new LFUEvictionPolicy<>(maxCapacity));
    }

}

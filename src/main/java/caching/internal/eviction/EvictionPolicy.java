package caching.internal.eviction;

public interface EvictionPolicy<K> {
    void onInsert(K key);
    void onAccess(K key);
    void onRemove(K key);
    K evict();
    void clear();
    int size();

    default boolean isExpired(K key) { return false; }
}

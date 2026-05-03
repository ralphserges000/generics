# Cache and Eviction Policies Implementation

## Background
This is an exercise on implementing my own cache with various eviction policies.ß
 * LRU (Least Recent Used)
 * FIFO (First In, First Out) 
 * TTL (Time To Live) 
 * LFU (Least Frequently Used)


## Features
* User can select from a list of eviction policy that best suit his use case.
* User can define the maximum size of his cache.
* User can evaluate the effectiveness of the cache by hit ratio.
* All available public APIs are thread-safe.

## Quick Start

```java
import caching.Cache;
import caching.CacheFactory;

Cache<String, Integer> lruCache = CacheFactory.lru(100);
Cache<String, Integer> fifoCache = CacheFactory.fifo(100);
Cache<String, Integer> ttlCache = CacheFactory.ttl(100, 5_000);
Cache<String, Integer> lfuCache = CacheFactory.lfu(100);

lruCache.put("a", 1);
Integer value = lruCache.get("a");
```

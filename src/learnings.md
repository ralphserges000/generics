**Learning Points**

1. **Hide implementation behind a public interface**

A package-private class like `CacheImpl` cannot be named or instantiated directly outside its package, but users can still use its behavior through a public interface like `Cache`.

```java
Cache<String, Integer> cache = CacheFactory.lru(100);
cache.put("a", 1);
cache.get("a");
```

This works because `CacheFactory` creates `CacheImpl` internally and returns it as `Cache`.

Key idea:

> Users do not need access to the implementation class. They only need access to a public contract that the implementation fulfills.

This is **programming to an interface**, with **implementation hiding**, often exposed through a **factory pattern**.

2. **`LinkedHashMap` can implement LRU ordering**

`LinkedHashMap` has a constructor that supports access-order:

```java
new LinkedHashMap<K, V>(initialCapacity, loadFactor, true)
```

The third argument, `accessOrder = true`, means entries are reordered when they are accessed.

This makes it useful for LRU caches because the map maintains entries from least-recently accessed to most-recently accessed.

Typical shape:

```java
Map<K, V> cache = new LinkedHashMap<K, V>(capacity, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
};
```

Key idea:

> With `accessOrder = true`, `LinkedHashMap` gives you the ordering behavior needed for LRU.

3. **`module-info.java` controls which packages are exposed**

In Java’s module system, `module-info.java` controls which packages are visible to other modules.

In this project:

```java
module learning.generics {
    exports caching;
}
```

This exposes public types in the `caching` package, like `Cache` and `CacheFactory`.

Packages not exported, such as:

```java
caching.internal.eviction
```

remain hidden from other modules, even if classes inside them are declared `public`.

Key idea:

> In the Java module system, `public` is not enough. The package must also be exported.

4. **Package-private classes stay hidden even in exported packages**

`CacheImpl` is in the exported `caching` package, but the class itself is package-private:

```java
class CacheImpl<K,V> implements Cache<K,V> {
    ...
}
```

So outside the `caching` package, users still cannot do this:

```java
new CacheImpl<>(...); // not allowed
```

They must use the public factory and interface:

```java
Cache<String, Integer> cache = CacheFactory.lru(100);
```

Key idea:

> Exporting a package does not make package-private classes public. Both the package and the type must be accessible.
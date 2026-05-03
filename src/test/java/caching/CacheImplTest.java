package caching;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CacheImplTest {

    @Test
    void test_cache_factory_validation() {
        Assertions.assertAll(
            "cache factory validation",
            () -> Assertions.assertThrows(IllegalArgumentException.class, () -> CacheFactory.lru(-3)),
            () -> Assertions.assertThrows(IllegalArgumentException.class, () -> CacheFactory.ttl(1, 0)),
            () -> Assertions.assertDoesNotThrow(() -> CacheFactory.lru(1)),
            () -> Assertions.assertDoesNotThrow(() -> CacheFactory.fifo(1)),
            () -> Assertions.assertDoesNotThrow(() -> CacheFactory.ttl(1, 1)),
            () -> Assertions.assertDoesNotThrow(() -> CacheFactory.lfu(1)));
    }
}

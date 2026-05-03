package caching;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import eviction_policy.LRUEvictionPolicy;

public class CacheImplTest {

    @Test
    void test_constructor_validation() {
        Assertions.assertAll(
            "constructor validation",
            () -> Assertions.assertThrows(IllegalArgumentException.class, () -> new CacheImpl<>(-3, null)),
            () -> Assertions.assertThrows(IllegalArgumentException.class, () -> new CacheImpl<>(1, null)),
            () -> Assertions.assertDoesNotThrow(() -> new CacheImpl<>(1, new LRUEvictionPolicy<>(1))));
    }
}

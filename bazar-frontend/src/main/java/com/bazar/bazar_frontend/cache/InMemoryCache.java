package com.bazar.bazar_frontend.cache;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

/**
 * Thread-safe LRU cache with TTL (default 5 mins)
 * Only book reads are cached and never writes
 */
@Component
public class InMemoryCache {

    private static final long DEFAULT_TTL_MS = 5 * 60 * 1000;     // 5 mins
    private static final int  MAX_ENTRIES    = 1_000;

    private static final class Entry {
        final Object value;
        final long   expiresAt;
        Entry(Object value, long ttlMs) {
            this.value     = value;
            this.expiresAt = Instant.now().toEpochMilli() + ttlMs;
        }
        boolean expired() { return Instant.now().toEpochMilli() > expiresAt; }
    }

    private final Map<String, Entry> lru =
    	    new LinkedHashMap<>(16, 0.75f, true) {
    	        @Override
    	        protected boolean removeEldestEntry(Map.Entry eldest) {
    	            return size() > MAX_ENTRIES;
    	        }
    	    };


    public synchronized <T> Optional<T> get(String key, Class<T> type) {
        Entry e = lru.get(key);
        if (e == null || e.expired()) {
            lru.remove(key);
            return Optional.empty();
        }
        return Optional.of(type.cast(e.value));
    }

    public synchronized void put(String key, Object value) {
        lru.put(key, new Entry(value, DEFAULT_TTL_MS));
    }

    /*
     * Invalidate a single key
     */
    public synchronized void invalidate(String key) { lru.remove(key); }

    /*
     * Invalidate all
     */
    public synchronized void clear() { lru.clear(); }
}

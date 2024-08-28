package com.dv.weatherapp.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ApiRateLimitService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    /**
     * Resolve the bucket for the given API key.
     *
     * @param apiKey the API key
     * @return the bucket for the given API key
     */
    public Bucket resolveBucket(String apiKey) {
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    private Bucket newBucket(String apiKey) {
        ApiRateLimit apiRateLimit = ApiRateLimit.resolveLimitFromApiKey(apiKey);
        return bucket(apiRateLimit.getLimit());
    }

    private Bucket bucket(Bandwidth limit) {
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}

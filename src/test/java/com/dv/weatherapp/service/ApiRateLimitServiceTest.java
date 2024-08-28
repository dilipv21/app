package com.dv.weatherapp.service;

import static org.junit.jupiter.api.Assertions.*;

import com.dv.weatherapp.service.ApiRateLimitService;
import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApiRateLimitServiceTest {

    private final ApiRateLimitService apiRateLimitService = new ApiRateLimitService();

    @Test
    void testResolveBucket() {
        Bucket bucket = apiRateLimitService.resolveBucket("XXXXXXXX");
        assertNotNull(bucket);
    }
}

package com.dv.weatherapp.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

import java.time.Duration;

public enum ApiRateLimit {
    USER_1(5),
    USER_2(5),
    USER_3(5),
    USER_4(5),
    USER_5(5);

    private final int bucketCapacity;

    ApiRateLimit(int bucketCapacity) {
        this.bucketCapacity = bucketCapacity;
    }

    Bandwidth getLimit() {
        return Bandwidth.classic(bucketCapacity, Refill.intervally(bucketCapacity, Duration.ofHours(1)));
    }

    public int bucketCapacity() {
        return bucketCapacity;
    }

    //TODO:: Need to update this method any additional user getting added will require code change here.
    static ApiRateLimit resolveLimitFromApiKey(String apiKey) {
        return switch (apiKey) {
            case "user1" -> USER_1;
            case "user2" -> USER_2;
            case "user3" -> USER_3;
            case "user4" -> USER_4;
            case "user5" -> USER_5;
            default -> null;
        };
    }

}

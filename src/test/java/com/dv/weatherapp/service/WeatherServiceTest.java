package com.dv.weatherapp.service;

import com.dv.weatherapp.config.CountryCodesLookUpConfig;
import com.dv.weatherapp.config.OpenWeatherMapProperties;

import io.github.bucket4j.Bucket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private CountryCodesLookUpConfig countryCodesLookUpConfig;

    @Mock
    private ApiRateLimitService apiRateLimitService;

    @Mock
    private OpenWeatherMapProperties openWeatherMapProperties;

    @Mock
    private Bucket bucket;

    @InjectMocks
    private WeatherService weatherService;


    @Test
    void testValidateInput_ValidInputs() {
        when(countryCodesLookUpConfig.getCountryCode("United States")).thenReturn("US");
        when(openWeatherMapProperties.getUserNameFromApiKey("validApiKey")).thenReturn("testUser");
        when(apiRateLimitService.resolveBucket("XXXXXXXX")).thenReturn(bucket);
        when(bucket.tryConsume(1)).thenReturn(true);

        ResponseEntity<Object> result = weatherService.getWeatherDescription("New York", "United States", "validApiKey");
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void testValidateInput_InvalidCountry() {
        when(countryCodesLookUpConfig.getCountryCode("InvalidCountry")).thenReturn(null);

        ResponseEntity<Object> result = weatherService.getWeatherDescription("New York", "InvalidCountry", "validApiKey");
        assertEquals(400, result.getStatusCodeValue());
        //assertEquals("Invalid Country Provided", result.getBody());
    }

    @Test
    void testValidateInput_InvalidApiKey() {
        when(countryCodesLookUpConfig.getCountryCode("United States")).thenReturn("US");
        when(openWeatherMapProperties.getUserNameFromApiKey("invalidApiKey")).thenReturn(null);

        ResponseEntity<Object> result = weatherService.getWeatherDescription("New York", "United States", "invalidApiKey");
        assertEquals(401, result.getStatusCodeValue());
        assertEquals("Invalid API Key Provided", result.getBody());
    }

    @Test
    void testValidateInput_RateLimitExceeded() {
        when(countryCodesLookUpConfig.getCountryCode("United States")).thenReturn("US");
        when(openWeatherMapProperties.getUserNameFromApiKey("validApiKey")).thenReturn("testUser");
        when(apiRateLimitService.resolveBucket("XXXXXXXX")).thenReturn(bucket);
        when(bucket.tryConsume(1)).thenReturn(false);

        ResponseEntity<Object> result = weatherService.getWeatherDescription("New York", "United States", "validApiKey");
        assertEquals(429, result.getStatusCodeValue());
        assertEquals("Too many requests", result.getBody());
    }
}

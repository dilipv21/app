package com.dv.weatherapp.config;

import static org.junit.jupiter.api.Assertions.*;

import com.dv.weatherapp.config.OpenWeatherMapProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)

class OpenWeatherMapPropertiesTest {

    @InjectMocks
    private OpenWeatherMapProperties openWeatherMapProperties ;

    @Test
    void testGetUserNameFromApiKey_ValidApiKey() {

        String userName = openWeatherMapProperties.getUserNameFromApiKey("validApiKey");
        assertEquals("testUser", userName);
    }

    @Test
    void testGetUserNameFromApiKey_InvalidApiKey() {
        String userName = openWeatherMapProperties.getUserNameFromApiKey("invalidApiKey");
        assertNull(userName);
    }
}

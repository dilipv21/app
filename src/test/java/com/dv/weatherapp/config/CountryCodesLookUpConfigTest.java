package com.dv.weatherapp.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class CountryCodesLookUpConfigTest {

    private final CountryCodesLookUpConfig countryCodesLookUpConfig = new CountryCodesLookUpConfig();

    @Test
    void testGetCountryCode_ValidCountry() {
        String countryCode = countryCodesLookUpConfig.getCountryCode("United States");
        assertEquals("US", countryCode);
    }

    @Test
    void testGetCountryCode_InvalidCountry() {
        String countryCode = countryCodesLookUpConfig.getCountryCode("InvalidCountry");
        assertNull(countryCode);
    }
}

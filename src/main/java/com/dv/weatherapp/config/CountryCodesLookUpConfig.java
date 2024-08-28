package com.dv.weatherapp.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class CountryCodesLookUpConfig {
    private final Map<String, String> countryMap;

    /**
     * Initialize the country code map
     * Key: Country name in lowercase
     * Value: Country code
     */
    public CountryCodesLookUpConfig() {
        String[] locales = Locale.getISOCountries();
        this.countryMap = new HashMap<>();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            countryMap.put(obj.getDisplayCountry().toLowerCase(), obj.getCountry());
        }
    }

    public String getCountryCode(String countryName) {
        return countryMap.get(countryName.toLowerCase());
    }


}

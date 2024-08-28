package com.dv.weatherapp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties("openweathermap")
public class OpenWeatherMapProperties {
    private String url;
    private Map<String, String> users;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getUsers() {
        return users;
    }

    public void setUsers(Map<String, String> users) {
        this.users = users;
    }

    // Optional convenience method for retrieving a specific user's API key
    public String getApiKey(String username) {
        return users.get(username);
    }

    public String getUserNameFromApiKey(String apiKey) {
        return users.entrySet().stream()
                .filter(entry -> entry.getValue().equals(apiKey))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

}
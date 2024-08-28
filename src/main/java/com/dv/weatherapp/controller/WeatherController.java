package com.dv.weatherapp.controller;

import com.dv.weatherapp.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public ResponseEntity<Object> getCurrentWeather(@RequestParam String city, @RequestParam String country, @RequestParam String apiKey) {
        log.info("Received request for city {} and country {}", city, country);
        return weatherService.getWeatherDescription(city, country, apiKey);
    }
}

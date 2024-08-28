package com.dv.weatherapp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//TODO: RestTemplate used, Fiegn Client is other option for http client
//@Component
@FeignClient(name = "weather-client", url = "${openweathermap.api.url}")
public interface WeatherClient {

    @GetMapping("/conditions")
    String getWeatherData(@RequestParam("city") String city, @RequestParam("appId") String apiKey);
}

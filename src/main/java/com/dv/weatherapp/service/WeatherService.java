package com.dv.weatherapp.service;

import com.dv.weatherapp.config.CountryCodesLookUpConfig;
import com.dv.weatherapp.config.OpenWeatherMapProperties;
import com.dv.weatherapp.entity.WeatherData;
import com.dv.weatherapp.model.AppException;
import com.dv.weatherapp.model.AppResponse;
import com.dv.weatherapp.model.WeatherApiResponse;
import com.dv.weatherapp.repository.WeatherRepository;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class WeatherService {

    private final CountryCodesLookUpConfig countryCodesLookUpConfig;
    private final ApiRateLimitService apiRateLimitService;
    private final OpenWeatherMapProperties openWeatherMapProperties;
    private final WeatherRepository weatherRepository;
    private final RestClient restClient;

    public WeatherService(CountryCodesLookUpConfig countryCodesLookUpConfig, ApiRateLimitService apiRateLimitService,
                          OpenWeatherMapProperties openWeatherMapProperties, WeatherRepository weatherRepository) {
        this.countryCodesLookUpConfig = countryCodesLookUpConfig;
        this.apiRateLimitService = apiRateLimitService;
        this.openWeatherMapProperties = openWeatherMapProperties;
        this.weatherRepository = weatherRepository;
        this.restClient = RestClient.create();
    }

    /**
     * Get weather description for a given city and country
     * <p>
     * First check if data exists in H2, if not call OpenWeatherMap and store the response in H2
     * <p>
     * Rate limiting is implemented using Bucket4j, hourly limit for each registered user
     * <p>
     * Error handling for various scenarios, such as invalid country name, unregistered user, API failure, etc.
     *
     */
    public ResponseEntity<Object> getWeatherDescription(final String city, final String country, final String apiKey) {

        log.info("Start of weather Service, validating request params");

        Optional<String> countryCode = Optional.ofNullable(countryCodesLookUpConfig.getCountryCode(country));
        if (countryCode.isEmpty()) {
            log.error("Invalid Country Name: {}", country);
            return ResponseEntity.badRequest().body(AppException.builder().message("Invalid Country Name. Expected https://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Officially_assigned_code_elements").build());
        }

        Optional<String> registeredUser = Optional.ofNullable(openWeatherMapProperties.getUserNameFromApiKey(apiKey));
        if (registeredUser.isEmpty()) {
            log.error("Unregistered User {}- Supplied API Key doesn't exist", apiKey);
            return ResponseEntity.status(401).body(AppException.builder().message("Unregistered User").build());
        }

        Bucket bucket = apiRateLimitService.resolveBucket(registeredUser.get());
        if (!bucket.tryConsume(1)) {
            log.error("Hourly limit reached for API Key: {}", apiKey);
            return ResponseEntity.status(429).body(AppException.builder().message("Hourly limit reached for API Key").build());
        }

        //TODO:: Add condition Check if data is 3 hours older?
        // Check if data for this location exists in H2
        Optional<WeatherData> existingData = weatherRepository.findByCityAndCountry(city, country);
        if (existingData.isPresent()) {
            log.info("Data found in H2 for {}-{}", city, country);
            return ResponseEntity.ok(ResponseEntity.ok(AppResponse.builder().city(city).country(country).weather(existingData.get().getDescription()).build()));
        }

        //Call OpenWeatherMap only if data doesn't exist in H2
        String url = openWeatherMapProperties.getUrl();
        log.info("Calling OpenWeatherMap");
        WeatherApiResponse response;
        try {
            response = restClient.get()
                    .uri(url, city, countryCode.get(), apiKey).retrieve().body(WeatherApiResponse.class);
        } catch (HttpStatusCodeException ex) {
            ResponseEntity<String> httpException = new ResponseEntity<String>(ex.getResponseBodyAsString(), ex.getResponseHeaders(), ex.getStatusCode());
            log.info("Failed {} {}", httpException.getStatusCode(), httpException.getBody());
            return ResponseEntity.status(httpException.getStatusCode()).body(AppException.builder().message("OpenWeather API failure, Please check the input provided in the request\n" + httpException.getBody()).build());
        } catch (Exception e) {
            log.error("Error fetching weather data: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(AppException.builder().message("Error fetching weather data").build());
        }

        log.info("Received response from OpenWeatherMap: {}", response);

        try {
            assert response != null;
            String description = response.getWeather().get(0).getDescription();
            WeatherData weatherData = WeatherData.builder().date(new Date())
                    .description(description)
                    .city(city)
                    .country(country)
                    .build();
            log.info("Adding data to DB for next retrieval");
            weatherRepository.save(weatherData);
            return ResponseEntity.ok(AppResponse.builder().city(city).country(country).weather(weatherData.getDescription()).build());
        } catch (Exception e) {
            log.error("Error parsing weather data: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(AppException.builder().message("Error parsing weather data").build());
        }
    }
}

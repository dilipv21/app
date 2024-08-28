package com.dv.weatherapp.client;

import com.dv.weatherapp.config.OpenWeatherMapProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.Map;

//@Configuration
public class RestClientConfig {

    private final OpenWeatherMapProperties openWeatherMapProperties;
    public RestClientConfig(OpenWeatherMapProperties openWeatherMapProperties) {
        this.openWeatherMapProperties = openWeatherMapProperties;
    }

    //TODO: Not used.
    @Bean(name = "customRestClient")
    RestClient restClient() {
        RestClient customClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                //.messageConverters(converters -> converters.add(new MyCustomMessageConverter()))
                .baseUrl(openWeatherMapProperties.getUrl())
                //.defaultUriVariables(Map.of("variable", "foo"))
                //.defaultHeader("My-Header", "Foo")
                //.requestInterceptor(myCustomInterceptor)
                //.requestInitializer(myCustomInitializer)
                .build();
        return customClient;
    }
}

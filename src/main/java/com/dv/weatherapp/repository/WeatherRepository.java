package com.dv.weatherapp.repository;

import com.dv.weatherapp.entity.WeatherData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeatherRepository extends CrudRepository<WeatherData, Long>{

    Optional<WeatherData> findByCityAndCountry(String city, String country);
}
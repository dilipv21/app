package com.dv.weatherapp.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class WeatherApiResponse {
    private String name;
    private ArrayList<Weather> weather;
}

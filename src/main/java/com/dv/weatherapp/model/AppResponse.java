package com.dv.weatherapp.model;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppResponse {
    private String city;
    private String country;
    private String weather;

    public String toString() {
        return ModelHelper.toJson(this);
    }
}

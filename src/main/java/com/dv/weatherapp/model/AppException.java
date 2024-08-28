package com.dv.weatherapp.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppException {
    private String message;

    public String toString() {
        return ModelHelper.toJson(this);
    }
}

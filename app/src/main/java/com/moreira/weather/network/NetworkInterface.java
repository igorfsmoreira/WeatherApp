package com.moreira.weather.network;

import com.moreira.weather.model.geocodingapi.GeocodingResponse;
import com.moreira.weather.model.onecallapi.OneCallResponse;
import com.moreira.weather.model.weatherapi.WeatherResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkInterface {
    @GET("data/2.5/onecall?exclude=minutely,alerts&units=metric")
    Call<OneCallResponse> getOneCallData(@Query("lat") double lat, @Query("lon") double lon);

    @GET("geo/1.0/reverse?limit=1")
    Call<List<GeocodingResponse>> getLocationFrom(@Query("lat") double lat, @Query("lon") double lon);

    @GET("data/2.5/weather?units=metric")
    Call<WeatherResponse> getWeatherFromName(@Query("q") String location);

    @GET("data/2.5/weather?units=metric")
    Call<WeatherResponse> getWeatherFromCoord(@Query("lat") double lat, @Query("lon") double lon);
}

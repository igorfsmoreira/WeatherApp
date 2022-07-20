package com.moreira.weather.model.onecallapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Daily {
    @SerializedName("dt")
    @Expose
    private int dt;
    @SerializedName("sunrise")
    @Expose
    private int sunrise;
    @SerializedName("sunset")
    @Expose
    private int sunset;
    @SerializedName("moonrise")
    @Expose
    private int moonrise;
    @SerializedName("moonset")
    @Expose
    private int moonset;
    @SerializedName("moon_phase")
    @Expose
    private double moonPhase;
    @SerializedName("temp")
    @Expose
    private Temp temp;
    @SerializedName("feels_like")
    @Expose
    private FeelsLike feelsLike;
    @SerializedName("pressure")
    @Expose
    private int pressure;
    @SerializedName("humidity")
    @Expose
    private int humidity;
    @SerializedName("dew_point")
    @Expose
    private double dewPoint;
    @SerializedName("wind_speed")
    @Expose
    private double windSpeed;
    @SerializedName("wind_deg")
    @Expose
    private int windDeg;
    @SerializedName("wind_gust")
    @Expose
    private double windGust;
    @SerializedName("weather")
    @Expose
    private List<Weather> weather = null;
    @SerializedName("clouds")
    @Expose
    private int clouds;
    @SerializedName("pop")
    @Expose
    private double pop;
    @SerializedName("uvi")
    @Expose
    private double uvi;

    public int getDt() {
        return dt;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public int getMoonrise() {
        return moonrise;
    }

    public int getMoonset() {
        return moonset;
    }

    public double getMoonPhase() {
        return moonPhase;
    }

    public Temp getTemp() {
        return temp;
    }

    public FeelsLike getFeelsLike() {
        return feelsLike;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getDewPoint() {
        return dewPoint;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getWindDeg() {
        return windDeg;
    }

    public double getWindGust() {
        return windGust;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public int getClouds() {
        return clouds;
    }

    public double getPop() {
        return pop;
    }

    public double getUvi() {
        return uvi;
    }
}

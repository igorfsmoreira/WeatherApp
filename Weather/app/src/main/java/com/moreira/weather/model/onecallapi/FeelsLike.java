package com.moreira.weather.model.onecallapi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FeelsLike {
    @SerializedName("day")
    @Expose
    private double day;
    @SerializedName("night")
    @Expose
    private double night;
    @SerializedName("eve")
    @Expose
    private double eve;
    @SerializedName("morn")
    @Expose
    private double morn;

    public double getDay() {
        return day;
    }

    public double getNight() {
        return night;
    }

    public double getEve() {
        return eve;
    }

    public double getMorn() {
        return morn;
    }
}

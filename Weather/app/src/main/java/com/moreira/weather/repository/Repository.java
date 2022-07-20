package com.moreira.weather.repository;

import android.location.Location;
import android.util.Log;

import com.moreira.weather.model.geocodingapi.GeocodingResponse;
import com.moreira.weather.model.onecallapi.OneCallResponse;
import com.moreira.weather.model.weatherapi.WeatherResponse;
import com.moreira.weather.network.NetworkClient;

public class Repository implements NetworkClient.OnDataReceivedListener {

    private static Repository mInstance;
    private OnDataUpdateListener mListener;
    private NetworkClient mClient;

    private Repository() {
        mClient = NetworkClient.getInstance();
        mClient.setListener(this);
    }

    public static Repository getInstance() {
        if (mInstance == null) {
            mInstance = new Repository();
        }

        return mInstance;
    }

    public interface OnDataUpdateListener {
        void onOneCallResponse(OneCallResponse data);

        void onGeocodingResponse(GeocodingResponse data);

        void onWeatherResponse(WeatherResponse data);

        void onCurrentLocationUpdate(double latitude, double longitude);
    }

    public void setListener(OnDataUpdateListener listener) {
        mListener = listener;
    }

    public void updateCurrentLocation(Location location) {
        if (location != null) {
            mListener.onCurrentLocationUpdate(location.getLatitude(), location.getLongitude());
        }
    }

    public void getOneCallData(double lat, double lon) {
        mClient.getOneCallData(lat, lon);
    }

    public void getGeoData(double lat, double lon) {
        mClient.getGeoData(lat, lon);
    }

    public void getWeatherFromCoord(double lat, double lon) {
        mClient.getWeatherFromCoord(lat, lon);
    }

    public void getDefaultLocationsData(String[][] locations) {
        for (String[] location : locations) {
            mClient.getWeatherFromName(location[0], location[1]);
        }
    }

    @Override
    public void onOneCallDataReceived(OneCallResponse data) {
        mListener.onOneCallResponse(data);
    }

    @Override
    public void onGeocodingDataReceived(GeocodingResponse data) {
        mListener.onGeocodingResponse(data);
    }

    @Override
    public void onWeatherResponse(WeatherResponse data) {
        mListener.onWeatherResponse(data);
    }
}

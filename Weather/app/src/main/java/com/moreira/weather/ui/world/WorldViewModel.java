package com.moreira.weather.ui.world;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moreira.weather.model.geocodingapi.GeocodingResponse;
import com.moreira.weather.model.onecallapi.OneCallResponse;
import com.moreira.weather.model.weatherapi.WeatherResponse;
import com.moreira.weather.repository.Repository;

import java.util.ArrayList;

public class WorldViewModel extends ViewModel implements Repository.OnDataUpdateListener {

    private final String[][] DEFAULT_LOCATIONS = {
            {"Lisbon", "PT"},
            {"Madrid", "ES"},
            {"Paris", "FR"},
            {"Berlin", "DE"},
            {"Copenhagen", "DK"},
            {"Rome", "IT"},
            {"London", "GB"},
            {"Dublin", "IE"},
            {"Prague", "CZ"},
            {"Vienna", "AT"},
    };
    private final Repository mRepository;
    private MutableLiveData<ArrayList<WeatherResponse>> mLocationsWeatherList;
    private MutableLiveData<Double[]> mCurrentLocation;
    private MutableLiveData<WeatherResponse> mCurrentLocationWeather;

    public WorldViewModel() {
        mRepository = Repository.getInstance();
        mLocationsWeatherList = new MutableLiveData<>(new ArrayList<>());
        mCurrentLocation = new MutableLiveData<>(new Double[]{1000.0, 1000.0});
        mCurrentLocationWeather = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<WeatherResponse>> getLocationsWeatherList() {
        return mLocationsWeatherList;
    }

    public MutableLiveData<Double[]> getCurrentLocation() {
        return mCurrentLocation;
    }

    public MutableLiveData<WeatherResponse> getCurrentLocationWeather() {
        return mCurrentLocationWeather;
    }

    public void setCurrentLocation(double latitude, double longitude) {
        // Round to 4 decimal places
        latitude = Math.round(latitude * 10000.0) / 10000.0;
        longitude = Math.round(longitude * 10000.0) / 10000.0;

        if (mCurrentLocation.getValue()[0] != latitude || mCurrentLocation.getValue()[1] != longitude) {
            mCurrentLocation.setValue(new Double[]{latitude, longitude});
        }
    }

    public void setRepositoryListener() {
        mRepository.setListener(this);
    }

    public void getWeatherFromCoord(double lat, double lon) {
        if (lat != 1000 && lon != 1000) {
            mRepository.getWeatherFromCoord(lat, lon);
        }
    }

    public void getDefaultLocationsData() {
        mRepository.getDefaultLocationsData(DEFAULT_LOCATIONS);
    }

    @Override
    public void onOneCallResponse(OneCallResponse data) {

    }

    @Override
    public void onGeocodingResponse(GeocodingResponse data) {

    }

    @Override
    public void onWeatherResponse(WeatherResponse data) {
        boolean isDefaultLocation = false;
        for (String[] location : DEFAULT_LOCATIONS) {
            if (data.getName().equals(location[0])) {
                isDefaultLocation = true;
                break;
            }
        }

        if (isDefaultLocation) {
            ArrayList<WeatherResponse> list = mLocationsWeatherList.getValue();

            boolean exist = false;
            for (int i = 0; i < list.size(); i++) {
                WeatherResponse element = list.get(i);
                if (element.getId() == data.getId()) {
                    list.set(i, data);
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                list.add(data);
            }

            mLocationsWeatherList.postValue(list);
        } else {
            mCurrentLocationWeather.postValue(data);
        }
    }

    @Override
    public void onCurrentLocationUpdate(double latitude, double longitude) {
        setCurrentLocation(latitude, longitude);
    }
}
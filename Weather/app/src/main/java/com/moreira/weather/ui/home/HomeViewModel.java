package com.moreira.weather.ui.home;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.moreira.weather.model.geocodingapi.GeocodingResponse;
import com.moreira.weather.model.onecallapi.OneCallResponse;
import com.moreira.weather.model.weatherapi.WeatherResponse;
import com.moreira.weather.repository.Repository;

public class HomeViewModel extends ViewModel implements Repository.OnDataUpdateListener {

    private final Repository mRepository;
    private MutableLiveData<OneCallResponse> mWeatherData;
    private MutableLiveData<GeocodingResponse> mGeoData;
    private MutableLiveData<Double[]> mLocation;
    private boolean mUseCurrentLocation = true;

    public HomeViewModel() {
        mRepository = Repository.getInstance();
        mWeatherData = new MutableLiveData<>();
        mGeoData = new MutableLiveData<>();
        mLocation = new MutableLiveData<>(new Double[]{0.0, 0.0});
    }

    public void setRepositoryListener() {
        mRepository.setListener(this);
    }

    public MutableLiveData<OneCallResponse> getWeatherData() {
        return mWeatherData;
    }

    public MutableLiveData<GeocodingResponse> getGeoData() {
        return mGeoData;
    }

    public MutableLiveData<Double[]> getLocation() {
        return mLocation;
    }

    public void setLocation(double[] selectedPlace) {
        mLocation.setValue(new Double[]{
                selectedPlace[0],
                selectedPlace[1],
        });
    }

    public void setUseCurrentLocation(boolean useCurrentLocation) {
        mUseCurrentLocation = useCurrentLocation;
    }

    public void getData(double lat, double lon) {
        mRepository.getOneCallData(lat, lon);
        mRepository.getGeoData(lat, lon);
    }

    @Override
    public void onOneCallResponse(OneCallResponse data) {
        mWeatherData.postValue(data);
    }

    @Override
    public void onGeocodingResponse(GeocodingResponse data) {
        Log.i("HomeViewModel", "onGeocodingResponse: " + data.getName());
        mGeoData.postValue(data);
    }

    @Override
    public void onWeatherResponse(WeatherResponse data) {

    }

    @Override
    public void onCurrentLocationUpdate(double latitude, double longitude) {
        if (!mUseCurrentLocation) {
            return;
        }

        // Round to 4 decimal places
        latitude = Math.round(latitude * 10000.0) / 10000.0;
        longitude = Math.round(longitude * 10000.0) / 10000.0;

        if (mLocation.getValue()[0] != latitude || mLocation.getValue()[1] != longitude) {
            mLocation.setValue(new Double[]{latitude, longitude});
        }
    }
}
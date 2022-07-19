package com.moreira.weather.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moreira.weather.adapter.RecyclerViewDailyAdapter;
import com.moreira.weather.adapter.RecyclerViewHourlyAdapter;
import com.moreira.weather.databinding.FragmentHomeBinding;
import com.moreira.weather.model.onecallapi.Current;
import com.moreira.weather.model.onecallapi.Daily;
import com.moreira.weather.model.onecallapi.Hourly;
import com.moreira.weather.model.onecallapi.OneCallResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding mBinding;
    private HomeViewModel mHomeViewModel;
    private TextView mTextLocal;
    private TextView mTextTemperature;
    private TextView mTextDescription;
    private ImageView mImageStatus;
    private TextView mTextSunrise;
    private TextView mTextSunset;
    private TextView mTextPressure;
    private TextView mTextHumidity;
    private TextView mTextUVI;
    private TextView mTextWindSpeed;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHomeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mHomeViewModel.setRepositoryListener();

        mBinding = FragmentHomeBinding.inflate(inflater, container, false);

        mTextLocal = mBinding.homeTvLocal;
        mTextTemperature = mBinding.homeTvTemperature;
        mTextDescription = mBinding.homeTvDescription;
        mImageStatus = mBinding.homeIvStatus;
        mTextSunrise = mBinding.homeTvSunrise;
        mTextSunset = mBinding.homeTvSunset;
        mTextPressure = mBinding.homeTvPressure;
        mTextHumidity = mBinding.homeTvHumidity;
        mTextUVI = mBinding.homeTvUvi;
        mTextWindSpeed = mBinding.homeTvWindSpeed;

        initRecyclerViewHourly();

        initRecyclerViewDaily();

        double[] selectedPlace = getSelectedLocationFromWorldFragment();
        if (selectedPlace != null && selectedPlace[0] != 1000 && selectedPlace[1] != 1000) {
            mHomeViewModel.setUseCurrentLocation(getUseCurrentLocationFromWorldFragment());
            mHomeViewModel.setLocation(selectedPlace);
        }

        initViewModelObservers();

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private double[] getSelectedLocationFromWorldFragment() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);

        if (sharedPreferences != null) {
            return new double[]{
                    Double.parseDouble(sharedPreferences.getString("latitude", "1000")),
                    Double.parseDouble(sharedPreferences.getString("longitude", "1000"))
            };
        }

        return null;
    }

    private boolean getUseCurrentLocationFromWorldFragment() {
        SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);

        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean("useCurrentLocation", false);
        }

        return false;
    }

    private void initViewModelObservers() {
        mHomeViewModel.getLocation().observe(getViewLifecycleOwner(), location -> {
            mHomeViewModel.getData(location[0], location[1]);
        });

        mHomeViewModel.getWeatherData().observe(getViewLifecycleOwner(), weatherData -> {
            Current current = weatherData.getCurrent();
            Calendar calendar = Calendar.getInstance();
            String temperature = Math.round(current.getTemp()) + "ºC";
            String description = current.getWeather().get(0).getMain();
            calendar.setTimeInMillis((long) current.getSunrise() * 1000);
            String sunrise = new SimpleDateFormat("HH:mm").format(calendar.getTime());
            calendar.setTimeInMillis((long) current.getSunset() * 1000);
            String sunset = new SimpleDateFormat("HH:mm").format(calendar.getTime());
            String pressure = (Math.round(current.getPressure() * 0.75006)) + " mmHg";
            String humidity = current.getHumidity() + "%";
            String uvi = String.valueOf(current.getUvi());
            String windSpeed = current.getWindSpeed() + " km/h";

            mTextTemperature.setText(temperature);
            mTextDescription.setText(description);
            String iconUrl = "https://openweathermap.org/img/wn/" + current.getWeather().get(0).getIcon() + "@4x.png";
            Glide.with(getContext()).load(iconUrl).into(mImageStatus);
            mTextSunrise.setText(sunrise);
            mTextSunset.setText(sunset);
            mTextPressure.setText(pressure);
            mTextHumidity.setText(humidity);
            mTextUVI.setText(uvi);
            mTextWindSpeed.setText(windSpeed);

            updateRecyclerViewHourly(weatherData);
            updateRecyclerViewDaily(weatherData);
        });

        mHomeViewModel.getGeoData().observe(getViewLifecycleOwner(), geoData -> {
            mTextLocal.setText(geoData.getName());
        });
    }

    private void initRecyclerViewHourly() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.homeRvHourly.setLayoutManager(layoutManager);
        RecyclerViewHourlyAdapter adapter = new RecyclerViewHourlyAdapter(getContext());
        mBinding.homeRvHourly.setAdapter(adapter);
    }

    private void initRecyclerViewDaily() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mBinding.homeRvDaily.setLayoutManager(layoutManager);
        RecyclerViewDailyAdapter adapter = new RecyclerViewDailyAdapter(getContext());
        mBinding.homeRvDaily.setAdapter(adapter);
    }

    private void updateRecyclerViewHourly(OneCallResponse oneCallResponse) {
        ArrayList<RecyclerViewHourlyAdapter.RecyclerViewItem> items = new ArrayList<>();

        for (Hourly element : oneCallResponse.getHourly()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((long) element.getDt() * 1000);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");

            RecyclerViewHourlyAdapter.RecyclerViewItem item = new RecyclerViewHourlyAdapter.RecyclerViewItem(
                    format.format(calendar.getTime()),
                    Math.round(element.getTemp()) + "ºC",
                    element.getWeather().get(0).getIcon()
            );

            items.add(item);
        }

        ((RecyclerViewHourlyAdapter) mBinding.homeRvHourly.getAdapter()).updateItems(items);
    }

    private void updateRecyclerViewDaily(OneCallResponse oneCallResponse) {
        ArrayList<RecyclerViewDailyAdapter.RecyclerViewItem> items = new ArrayList<>();

        for (Daily element : oneCallResponse.getDaily()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((long) element.getDt() * 1000);
            SimpleDateFormat format = new SimpleDateFormat("EEE d");

            RecyclerViewDailyAdapter.RecyclerViewItem item = new RecyclerViewDailyAdapter.RecyclerViewItem(
                    format.format(calendar.getTime()),
                    Math.round(element.getTemp().getMax()) + "ºC",
                    Math.round(element.getTemp().getMin()) + "ºC",
                    element.getWeather().get(0).getIcon()
            );

            items.add(item);
        }

        ((RecyclerViewDailyAdapter) mBinding.homeRvDaily.getAdapter()).updateItems(items);
    }
}
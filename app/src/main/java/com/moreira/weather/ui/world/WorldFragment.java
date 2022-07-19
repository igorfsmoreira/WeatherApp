package com.moreira.weather.ui.world;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moreira.weather.R;
import com.moreira.weather.adapter.RecyclerViewWorldAdapter;
import com.moreira.weather.databinding.FragmentWorldBinding;
import com.moreira.weather.model.weatherapi.WeatherResponse;

import java.util.ArrayList;

public class WorldFragment extends Fragment implements RecyclerViewWorldAdapter.OnItemInteractionListener {

    private FragmentWorldBinding mBinding;
    private WorldViewModel mWorldViewModel;
    private TextView mTextLocal;
    private TextView mTextTemperature;
    private TextView mTextDescription;
    private ImageView mImageStatus;
    private CardView mViewCurrentLocation;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mWorldViewModel = new ViewModelProvider(this).get(WorldViewModel.class);
        mWorldViewModel.setRepositoryListener();

        mBinding = FragmentWorldBinding.inflate(inflater, container, false);

        mTextLocal = mBinding.worldTvLocal;
        mTextTemperature = mBinding.worldTvTemperature;
        mTextDescription = mBinding.worldTvDescription;
        mImageStatus = mBinding.worldIvStatus;
        mViewCurrentLocation = mBinding.worldCardViewCurrent;
        mViewCurrentLocation.setOnClickListener(view -> {
            Double[] value = mWorldViewModel.getCurrentLocation().getValue();
            if (value != null && value[0] != 1000 && value[1] != 1000) {
                saveOnSharedPrefsData(value[0], value[1], true);
                Navigation.findNavController(getView()).navigate(R.id.navigation_home);
            }
        });

        double[] lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation != null) {
            mWorldViewModel.setCurrentLocation(lastKnownLocation[0], lastKnownLocation[1]);
        }

        mWorldViewModel.getDefaultLocationsData();

        initRecyclerView();

        initViewModelObservers();

        return mBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private double[] getLastKnownLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null) {
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastKnownLocation != null) {
                    Log.i("WorldFragment", "lastKnownLocation: " + lastKnownLocation.getLatitude() + "  " + lastKnownLocation.getLongitude());
                    return new double[]{lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()};
                }
            }
        }

        return null;
    }

    private void initViewModelObservers() {
        mWorldViewModel.getWeather().observe(getViewLifecycleOwner(), weathers -> {
            updateRecyclerView(weathers);
        });

        mWorldViewModel.getCurrentLocation().observe(getViewLifecycleOwner(), currentLocation -> {
            mWorldViewModel.getWeatherFromCoord(currentLocation[0], currentLocation[1]);
        });

        mWorldViewModel.getCurrentLocationWeather().observe(getViewLifecycleOwner(), weather -> {
            String local = weather.getName();
            String temperature = Math.round(weather.getMain().getTemp()) + "ºC";
            String description = weather.getWeather().get(0).getMain();

            mTextLocal.setText(local);
            mTextTemperature.setText(temperature);
            mTextDescription.setText(description);
            String iconUrl = "https://openweathermap.org/img/wn/" + weather.getWeather().get(0).getIcon() + "@4x.png";
            Glide.with(getContext()).load(iconUrl).into(mImageStatus);
        });
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mBinding.recyclerView.setLayoutManager(layoutManager);
        RecyclerViewWorldAdapter adapter = new RecyclerViewWorldAdapter(getContext(), this);
        mBinding.recyclerView.setAdapter(adapter);
    }

    private void updateRecyclerView(ArrayList<WeatherResponse> weatherList) {
        ArrayList<RecyclerViewWorldAdapter.RecyclerViewItem> items = new ArrayList<>();

        for (WeatherResponse element : weatherList) {
            RecyclerViewWorldAdapter.RecyclerViewItem item = new RecyclerViewWorldAdapter.RecyclerViewItem(
                    element.getName() + ", " + element.getSys().getCountry(),
                    Math.round(element.getMain().getTemp()) + "ºC",
                    element.getWeather().get(0).getMain(),
                    element.getCoord().getLat(),
                    element.getCoord().getLon(),
                    element.getWeather().get(0).getIcon()
            );

            items.add(item);
        }

        ((RecyclerViewWorldAdapter) mBinding.recyclerView.getAdapter()).updateItems(items);
    }

    @Override
    public void onItemClick(RecyclerViewWorldAdapter.RecyclerViewItem item) {
        saveOnSharedPrefsData(item.getLatitude(), item.getLongitude(), false);
        Navigation.findNavController(getView()).navigate(R.id.navigation_home);
    }

    private void saveOnSharedPrefsData(double latitude, double longitude, boolean useCurrentLocation) {
        SharedPreferences sharedPreferences = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("latitude", String.valueOf(latitude));
        editor.putString("longitude", String.valueOf(longitude));
        editor.putBoolean("useCurrentLocation", useCurrentLocation);
        editor.apply();
    }
}
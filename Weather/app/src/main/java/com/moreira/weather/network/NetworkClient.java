package com.moreira.weather.network;

import android.util.Log;

import com.moreira.weather.model.geocodingapi.GeocodingResponse;
import com.moreira.weather.model.onecallapi.OneCallResponse;
import com.moreira.weather.model.weatherapi.WeatherResponse;

import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    public static final String API_KEY = "50438aa67649f28d667a617251d65598";
    public static final String BASE_URL = "https://api.openweathermap.org/";

    private static NetworkClient mInstance;
    private static NetworkInterface mNetworkInterface;
    private OnDataReceivedListener mListener;

    public interface OnDataReceivedListener {
        void onOneCallDataReceived(OneCallResponse data);

        void onGeocodingDataReceived(GeocodingResponse data);

        void onWeatherResponse(WeatherResponse data);
    }

    public static NetworkClient getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkClient();
        }

        return mInstance;
    }

    public NetworkInterface getNetworkInterface() {
        if (mNetworkInterface == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();

            client.addInterceptor(chain -> {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                HttpUrl url = originalHttpUrl.newBuilder().addQueryParameter("appid", API_KEY).build();
                Request.Builder requestBuilder = original.newBuilder().url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            });

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();

            mNetworkInterface = retrofit.create(NetworkInterface.class);
        }

        return mNetworkInterface;
    }

    public void setListener(OnDataReceivedListener listener) {
        mListener = listener;
    }

    public void getOneCallData(double lat, double lon) {
        Call<OneCallResponse> call = getNetworkInterface().getOneCallData(lat, lon);

        call.enqueue(new Callback<OneCallResponse>() {
            @Override
            public void onResponse(Call<OneCallResponse> call, Response<OneCallResponse> response) {
                OneCallResponse dataReceived = null;

                if (response.isSuccessful()) {
                    dataReceived = response.body();
                }

                if (dataReceived != null) {
                    mListener.onOneCallDataReceived(dataReceived);
                }
            }

            @Override
            public void onFailure(Call<OneCallResponse> call, Throwable t) {
                Log.i("APIClient", "getWeatherData" + t.getLocalizedMessage());
            }
        });
    }

    public void getGeoData(double lat, double lon) {
        Call<List<GeocodingResponse>> call = getNetworkInterface().getLocationFrom(lat, lon);

        call.enqueue(new Callback<List<GeocodingResponse>>() {
            @Override
            public void onResponse(Call<List<GeocodingResponse>> call, Response<List<GeocodingResponse>> response) {
                List<GeocodingResponse> dataReceived = null;

                if (response.isSuccessful()) {
                    dataReceived = response.body();
                }

                if (dataReceived != null && dataReceived.size() > 0) {
                    mListener.onGeocodingDataReceived(dataReceived.get(0));
                }
            }

            @Override
            public void onFailure(Call<List<GeocodingResponse>> call, Throwable t) {
                Log.i("APIClient", "getGeoData" + t.getLocalizedMessage());
            }
        });
    }

    public void getWeatherFromName(String name, String countryCode) {
        String location = name + "," + countryCode;
        Call<WeatherResponse> call = getNetworkInterface().getWeatherFromName(location);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse dataReceived = null;

                if (response.isSuccessful()) {
                    dataReceived = response.body();
                }

                if (dataReceived != null) {
                    mListener.onWeatherResponse(dataReceived);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.i("APIClient", "getWeatherFromName" + t.getLocalizedMessage());
            }
        });
    }

    public void getWeatherFromCoord(double lat, double lon) {
        Call<WeatherResponse> call = getNetworkInterface().getWeatherFromCoord(lat, lon);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                WeatherResponse dataReceived = null;

                if (response.isSuccessful()) {
                    dataReceived = response.body();
                }

                if (dataReceived != null) {
                    mListener.onWeatherResponse(dataReceived);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.i("APIClient", "getWeatherFromName" + t.getLocalizedMessage());
            }
        });
    }
}

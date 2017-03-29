package com.example.android.quakereport;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AVJEET on 18-02-2017.
 */

public class EarthquakeApiAdapter {
    private static final String BASE_URL = "https://earthquake.usgs.gov";
    private static Retrofit retrofit;


    public static Retrofit getAdapter() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder().setLenient().create();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();
        }
        return retrofit;
    }


}

//https://earthquake.usgs.gov/fdsnws/event/1/query.geojson?maxlatitude=36.457&minlatitude=4.916&maxlongitude=98.613
// &minlongitude=61.348&minmagnitude=0&orderby=time&limit=20
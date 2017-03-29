package com.example.android.quakereport;


import com.example.android.quakereport.Data.QuakeReport;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by AVJEET on 17-02-2017.
 */
public interface EarthquakeApi {

    @GET("/fdsnws/event/1/query")
    Call<QuakeReport> getQuakeReport(@Query("format") String format, @QueryMap Map<String,Double> coord,@Query("orderby") String orderby,@Query("limit") int limit);
}


//https://earthquake.usgs.gov/fdsnws/event/1/query.geojson?starttime=2017-01-22%2000:00:00&endtime=2017-02-21%2023:59:59
// &maxlatitude=36.457&minlatitude=4.916&maxlongitude=98.613&minlongitude=61.348&minmagnitude=0&orderby=time&limit=20
/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.android.quakereport.Data.Earthquake;
import com.example.android.quakereport.Data.Feature;
import com.example.android.quakereport.Data.Properties;
import com.example.android.quakereport.Data.QuakeReport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarthquakeActivity extends AppCompatActivity  {

    ArrayList<Earthquake> earthquakes = new ArrayList<>();

    Earthquake currentEarthquake = null;
    ProgressBar loading ;



    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nI= cm.getActiveNetworkInfo();
        boolean isConnected = nI != null &&
                nI.isConnectedOrConnecting();
        return isConnected;
    }
    public Map<String, Double> getCoord() {
        Map<String, Double> coord = new HashMap<>();
        coord.put("maxlatitude", 36.457);
        coord.put("minlatitude", 4.916);
        coord.put("maxlongitude", 98.613);
        coord.put("minlongitude", 61.348);
        return coord;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_settings){
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        if (isNetworkConnected()) {
            findViewById(R.id.noInternet).setVisibility(View.INVISIBLE);
            loading = (ProgressBar) findViewById(R.id.progressBar);
            loading.setVisibility(View.VISIBLE);
            extractEarthquake();

        } else {
            findViewById(R.id.noInternet).setVisibility(View.VISIBLE);
        }
    }



    public void extractEarthquake() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minmagnitude = sharedPrefs.
        EarthquakeApi earthquakeApi = EarthquakeApiAdapter.getAdapter().create(EarthquakeApi.class);
        Call<QuakeReport> call = earthquakeApi.getQuakeReport("geojson", getCoord(), "time", minmagnitude);

        call.enqueue(new Callback<QuakeReport>() {

            @Override
            public void onResponse(Call<QuakeReport> call, Response<QuakeReport> response) {

                Log.w(LOG_TAG, "Successful");
                fillResponse(response);
            }


            @Override
            public void onFailure(Call<QuakeReport> call, Throwable t) {
                Log.e(LOG_TAG, t.getLocalizedMessage(), t.getCause());
                t.printStackTrace();

            }

        });

    }

    public void fillResponse(Response<QuakeReport> response) {
        List<Feature> features = response.body().getFeatures();

        for (int i = 0; i < features.size(); i++) {
            Properties properties = features.get(i).getProperties();
            Formatter timeDate = new Formatter(properties.getTime());
            Formatter loc = new Formatter(properties.getPlace());
            Formatter mag = new Formatter((properties.getMag()));
            Earthquake e = new Earthquake(loc.getLocationOffset(), loc.getLocation(), mag.getMagnitude(), mag.magnitudeDouble, timeDate.getDate(), timeDate.getTime(), properties.getUrl());
            if(e.getLocation().contains(" India"))
            earthquakes.add(e);

        }
        updateUI();
    }


    public void updateUI() {
        if (earthquakes != null) {
            ListView earthquakeListView = (ListView) findViewById(R.id.list);
            loading.setVisibility(View.GONE);
            earthquakeListView.setVisibility(View.VISIBLE);
            // Create a new {@link ArrayAdapter} of earthquakes
            EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakes);

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            earthquakeListView.setAdapter(adapter);


            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    currentEarthquake = earthquakes.get(position);
                    String URL = currentEarthquake.getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(URL));
                    startActivity(intent);
                }
            });
        }
    }

}
class Formatter {
    private Date date;
    private String mLocationOffset;
    private String mLocation;
    private String mMagnitude;
    public Double magnitudeDouble;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy");

    private SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm aa");

    Formatter(long t) {
        date = new Date(t);
    }

    Formatter(String s) {
        if (s.contains("of")) {
            String[] loc = s.split(" of ");
            mLocationOffset = loc[0] + " of";
            mLocation = loc[1];
        } else {
            mLocationOffset = "Near the";
            mLocation = s;
        }
    }

    Formatter(Double d) {
        DecimalFormat magFormat = new DecimalFormat("0.0");
        mMagnitude = magFormat.format(d);
        magnitudeDouble = d;

    }

    public String getDate() {
        return dateFormat.format(date);
    }

    public String getTime() {
        return timeFormat.format(date);
    }

    public String getLocationOffset() {
        return mLocationOffset;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getMagnitude() {
        return mMagnitude;
    }


}




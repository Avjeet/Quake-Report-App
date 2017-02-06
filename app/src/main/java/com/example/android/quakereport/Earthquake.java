package com.example.android.quakereport;

/**
 * Created by AVJEET on 02-02-2017.
 */

public class Earthquake {
    private String mLocation;
    private String mMagnitude;
    private String mDate;
    private String mTime;
    private String mLocationOffset;
    private Double MagnitudeDouble;
    private String mUrl;

    Earthquake(String locOff,String loc, String mag, Double magD,String date,String time,String url){
        mLocationOffset=locOff;
        mLocation=loc;
        mMagnitude=mag;
        mDate=date;
        mTime=time;
        MagnitudeDouble=magD;
        mUrl=url;
    }

    public String getLocationOffset(){return mLocationOffset;}
    public String getLocation(){return mLocation;}
    public String getMagnitude(){return mMagnitude;}
    public String getDate(){return mDate;}
    public String getTime(){return mTime;}
    public Double getMagnitudeDouble(){return MagnitudeDouble;}
    public String getUrl(){return mUrl;}
}

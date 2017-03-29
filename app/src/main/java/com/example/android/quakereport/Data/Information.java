package com.example.android.quakereport.Data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AVJEET on 20-02-2017.
 */

public class Information {
    @SerializedName("type")
    public String info;
    public String getInfo(){return info;}
}

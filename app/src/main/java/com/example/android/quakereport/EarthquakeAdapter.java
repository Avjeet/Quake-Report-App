package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AVJEET on 02-02-2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    ViewHolder viewHolder=null;
    Context mContext;

    EarthquakeAdapter(Activity context,ArrayList<Earthquake> data){
        super(context,0,data);
        mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView=convertView;

        if(listItemView==null){
            LayoutInflater l=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItemView=l.inflate(R.layout.list_item,parent,false);
            viewHolder=new ViewHolder((TextView)listItemView.findViewById(R.id.magnitude),(TextView) listItemView.findViewById(R.id.location),(TextView) listItemView.findViewById(R.id.locationOffset),(TextView) listItemView.findViewById(R.id.date),(TextView) listItemView.findViewById(R.id.time));
            listItemView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)listItemView.getTag();
        }
        Earthquake currentEarthquake= getItem(position);
        viewHolder.magTextView.setText(currentEarthquake.getMagnitude());
        viewHolder.dateTextView.setText(currentEarthquake.getDate());
        viewHolder.locTextView.setText(currentEarthquake.getLocation());
        viewHolder.timeTextView.setText(currentEarthquake.getTime());
        viewHolder.locOffTextView.setText(currentEarthquake.getLocationOffset());
        int magnitudeColor=getMagnitudeColor(currentEarthquake.getMagnitudeDouble());
        viewHolder.magnitudeCircle.setColor(magnitudeColor);
    return listItemView;

    }
    private int getMagnitudeColor(Double d){
        int backColorID=0;
        int magnitudeFloor = (int) Math.floor(d);
        switch(magnitudeFloor){
            case 0:
            case 1:backColorID=R.color.magnitude1;
                break;
            case 2:backColorID= R.color.magnitude2;
                break;
            case 3:backColorID= R.color.magnitude3;
                break;
            case 4:backColorID= R.color.magnitude4;
                break;
            case 5:backColorID= R.color.magnitude5;
                break;
            case 6:backColorID= R.color.magnitude6;
                break;
            case 7:backColorID= R.color.magnitude7;
                break;
            case 8:backColorID= R.color.magnitude8;
                break;
            case 9:backColorID= R.color.magnitude9;
                break;
            case 10:backColorID= R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),backColorID);
    }

}

class ViewHolder {
    public TextView magTextView;
    public TextView dateTextView;
    public TextView locTextView;
    public TextView timeTextView;
    public TextView locOffTextView;
    public GradientDrawable magnitudeCircle;

    ViewHolder(TextView mag, TextView loc, TextView locOff, TextView date, TextView time) {
        magTextView = mag;
        dateTextView = date;
        locTextView = loc;
        locOffTextView = locOff;
        timeTextView = time;
        magnitudeCircle = (GradientDrawable) magTextView.getBackground();

    }
}
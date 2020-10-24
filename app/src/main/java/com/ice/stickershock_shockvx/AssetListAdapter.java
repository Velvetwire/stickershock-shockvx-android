package com.ice.stickershock_shockvx;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AssetListAdapter extends ArrayAdapter<Sticker> {
    private final Context context;
    private final List<Sticker> myList;
    TextView mAssetName;
    TextView timestampTv;
    TextView surftempTv;
    TextView airtempTv;
    TextView humidityTv;
    TextView pressureTv;
    TextView rssiTv;
    TextView batteryTv;
    TextView angleTv;
    TextView dropsTv;
    TextView bumpsTv;
    TextView addressTv;

    private final String DEGREES_C = "\u2103";
    private final String DEGREES_F = "\u2109";
    private final String DEGREES = "\u00B0";
    private final String MILLIBAR = "mB";
    private final String PERCENT = "%";
    private final String DECIBEL = " dB";

    public AssetListAdapter(Context context, List<Sticker> myList) {
        super(context, R.layout.list_rowasset ,myList);
        this.context = context;
        this.myList = myList;
    }



    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;

            View rowView  = inflater.inflate(R.layout.list_rowasset, parent, false);

            mAssetName  = (TextView) rowView.findViewById(R.id.assetname);
            surftempTv  = (TextView) rowView.findViewById(R.id.surftemp);
            airtempTv   = (TextView) rowView.findViewById(R.id.airtemp);
            humidityTv  = (TextView) rowView.findViewById(R.id.moisture);
            pressureTv  = (TextView) rowView.findViewById(R.id.pressure);
            rssiTv      = (TextView) rowView.findViewById(R.id.rssi);
            batteryTv   = (TextView) rowView.findViewById(R.id.batterylevel);
            addressTv   = (TextView) rowView.findViewById(R.id.stickerid);
     //       angleTv     = (TextView) rowView.findViewById(R.id.angle);
      //      dropsTv     = (TextView) rowView.findViewById(R.id.drops);
      //      bumpsTv     = (TextView) rowView.findViewById(R.id.bumps);
      //      distanceTv  = (TextView) rowView.findViewById(R.id.distance);

         Sticker device = myList.get(position);

         String assetname = device.name;
         String address   = device.address;
         String surftemp = ((float)device.surface / 100) + DEGREES_C;
         String airtemp = ((float)device.ambient / 100 ) + DEGREES_C;
         String humidity = ((float)device.humidity / 100) + PERCENT;
         String pressure = ((float)device.pressure ) + MILLIBAR;
         String rssi     = device.rssi +  DECIBEL;
         String battery  = ((float)device.batteryLevel) +  " %";

         //float degrees = (float) (((float)device.telemetry.angle / 1000.00) * 31.82);  // 90 / 2sqrt2
         //double degrees2 = Math.round(degrees * 100.0) / 100.0;
         //String angle  = ((float)degrees2) + DEGREES;

         //String drops  = String.valueOf(device.telemetry.drops);
         //String bumps  = String.valueOf(device.telemetry.bumps);
         //String distance = calculateDistance (device.rssi) +" " + "m";

         mAssetName.setText ( assetname );
         addressTv.setText  ( address );
         surftempTv.setText ( surftemp );
         airtempTv.setText  ( airtemp );
         humidityTv.setText ( humidity );
         pressureTv.setText ( pressure );
         rssiTv.setText     (rssi);
         batteryTv.setText ( battery );
     //    angleTv.setText  ( angle );
     ////    dropsTv.setText  ( drops );
     //    bumpsTv.setText  ( bumps );
       //      distanceTv.setText  ( distance );
         return rowView;
    }



    //
    int TX1Meter = - 75;
    float N        = (float) 3.0;
    public String calculateDistance(int rssi ) {
           int difference =  TX1Meter - rssi;

           float exponent = (float) ((float)difference / (10.0 * N));
           Log.d("DiSTANCE", "DIFFERENCE " + difference);
           Log.d("DiSTANCE", "EXPONENT " + exponent);
           double dist = Math.pow(10, exponent);
           double distance = Math.round(dist * 1.0) / 1.0;
           if (distance < 1.0) {
               return "<1";
           }
           return String.valueOf(distance);
    }

}

/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *    file: AssetListAdapter.java
 *
 * List adapter for asset display
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
package com.ice.stickershock_shockvx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import static com.ice.stickershock_shockvx.Constants.*;

public class AssetListAdapter extends ArrayAdapter<Sticker> {
    private final Context context;
    private final List<Sticker> myList;

    public AssetListAdapter(Context context, List<Sticker> myList) {
        super(context, R.layout.list_rowasset ,myList);
        this.context = context;
        this.myList = myList;
    }


    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;

        View rowView  = inflater.inflate(R.layout.list_rowasset, parent, false);

        TextView mAssetName  = rowView.findViewById(R.id.assetname);
        TextView surftempTv  = rowView.findViewById(R.id.surftemp);
        TextView airtempTv   = rowView.findViewById(R.id.airtemp);
        TextView humidityTv  = rowView.findViewById(R.id.moisture);
        TextView pressureTv  = rowView.findViewById(R.id.pressure);
        TextView rssiTv      = rowView.findViewById(R.id.rssi);
        TextView batteryTv   = rowView.findViewById(R.id.batterylevel);
        TextView addressTv   = rowView.findViewById(R.id.stickerid);
//      TextView angleTv     = (TextView) rowView.findViewById(R.id.angle);
//      TextView dropsTv     = (TextView) rowView.findViewById(R.id.drops);
//      TextView bumpsTv     = (TextView) rowView.findViewById(R.id.bumps);
//      TextView distanceTv  = (TextView) rowView.findViewById(R.id.distance);

        Sticker device = myList.get(position);

        String assetname = device.name;
        String address   = device.address;
        String surftemp  = ((float)device.surface / 100) + DEGREES_C;
        String airtemp   = ((float)device.ambient / 100 ) + DEGREES_C;
        String humidity  = ((float)device.humidity / 100) + PERCENT;
        String pressure  = ((float)device.pressure ) + MILLIBAR;
        String rssi      = device.rssi +  DECIBEL;
        String battery   = ((float)device.batteryLevel) +  " %";

         // float degrees = (float) (((float)device.telemetry.angle / 1000.00) * 31.82);  // 90 / 2sqrt2
         // double degrees2 = Math.round(degrees * 100.0) / 100.0;
         // String angle  = ((float)degrees2) + DEGREES;

         // String drops  = String.valueOf(device.telemetry.drops);
         // String bumps  = String.valueOf(device.telemetry.bumps);
         // String distance = calculateDistance (device.rssi) +" " + "m";

        mAssetName.setText ( assetname );
        addressTv.setText  ( address );
        surftempTv.setText ( surftemp );
        airtempTv.setText  ( airtemp );
        humidityTv.setText ( humidity );
        pressureTv.setText ( pressure );
        rssiTv.setText     (rssi);
        batteryTv.setText ( battery );
 //     angleTv.setText  ( angle );
 //     dropsTv.setText  ( drops );
 //     bumpsTv.setText  ( bumps );
 //     distanceTv.setText  ( distance );
        return rowView;
    }

}

//=============================================================================
// project: ShockVx
//  module: Stickershock Android App for cold chain tracking.
//  author: Velvetwire, llc
//    file: TelemetryFragment.java
//
// Read Telemetry from connected sticker
//
// (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
//=============================================================================
package com.ice.stickershock_shockvx;


import androidx.fragment.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.ice.stickershock_shockvx.bluetooth.BluetoothLeService;
import static com.ice.stickershock_shockvx.bluetooth.Actions.*;


public class TelemetryFragment extends Fragment {
    TextView mSurfTemp;
    TextView mAirTemp;
    TextView mHumidity;
    TextView mPressure;
    TextView mFaceup;
    TextView mForces;
    TextView mBattery;
    TextView mRssiValue;

    private final String DEGREES_C = "\u2103";
    private final String DEGREES_F = "\u2109";
    private final String DEGREES = "\u00B0";
    private final String MILLIBAR = " mB";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.telemetry, container, false);

        mSurfTemp  = v.findViewById(R.id.surfaceValue);
        mAirTemp   = v.findViewById(R.id.ambientValue);
        mHumidity  = v.findViewById(R.id.humidityValue);
        mPressure  = v.findViewById(R.id.pressureValue);
        mFaceup  = v.findViewById(R.id.faceValue);
        mForces  = v.findViewById(R.id.forcesValue);
        mBattery   = v.findViewById(R.id.battery);
        mRssiValue = v.findViewById(R.id.rssi);

    //    readRssi();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        readRssi();
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(mGattUpdateReceiver);
    }



    public static TelemetryFragment newInstance(String text) {

        TelemetryFragment f = new TelemetryFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if ( ACTION_AMBIENT_AVAILABLE.equals(action)) {
                String value = intent.getStringExtra( EXTRA_DATA);
                mAirTemp.setText( value );
                mHumidity.setText( value );
            }

            if ( ACTION_SURFACE_AVAILABLE.equals(action)) {
                float fValue = intent.getFloatExtra( FLOAT_DATA, 0.0f );
                String value = Float.toString(fValue);
                mSurfTemp.setText( value );
                mPressure.setText( value );
            }

            if ( ACTION_HANDLING_AVAILABLE.equals(action)) {
                String value = intent.getStringExtra( EXTRA_DATA) + DEGREES;
                mFaceup.setText( value );
                mForces.setText( value );
            }

            if ( ACTION_BATTERY_LEVEL_AVAILABLE.equals(action)) {

                int intData = intent.getIntExtra( INT_DATA, 0);
    //            mBattery.setText(String.valueOf(intData + "%"));
            }
            if ( ACTION_RSSI_DATA_AVAILABLE.equals(action)) {

                int rssiData = intent.getIntExtra( INT_DATA,0);
                Log.d("RECVD", "RSSI " + rssiData);
       //         mRssiValue.setText(String.valueOf(rssiData + " dB"));
                readBattery();
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction( ACTION_AMBIENT_AVAILABLE );
        intentFilter.addAction( ACTION_HANDLING_AVAILABLE );
        intentFilter.addAction( ACTION_SURFACE_AVAILABLE );
        intentFilter.addAction( ACTION_BATTERY_LEVEL_AVAILABLE );
        intentFilter.addAction( ACTION_RSSI_DATA_AVAILABLE );
        return intentFilter;
    }

    public void readRssi() {
        broadcastUpdate( ACTION_READ_RSSI );
    }

    public void readBattery() {
        broadcastUpdate( ACTION_BATTERY_LEVEL );
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        getActivity().sendBroadcast(intent);
    }
}


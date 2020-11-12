/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *    file: TelemetryFragment.java
 *
 * Read Telemetry from Connected Sticker
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
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
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.Nullable;

import static com.ice.stickershock_shockvx.bluetooth.Actions.*;
import static com.ice.stickershock_shockvx.Constants.*;

public class TelemetryFragment extends Fragment {
    private static String stickerId;
    TextView mSurfTemp;
    TextView mAirTemp;
    TextView mHumidity;
    TextView mPressure;
    TextView mFaceup;
    TextView mForces;
    TextView mBattery;
    TextView mRssiValue;
    TextView mStickerId;

    Button   mBackAssets;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.telemetry, container, false);

        mSurfTemp  = v.findViewById( R.id.surfaceValue );
        mAirTemp   = v.findViewById( R.id.ambientValue );
        mHumidity  = v.findViewById( R.id.humidityValue );
        mPressure  = v.findViewById( R.id.pressureValue );
        mFaceup    = v.findViewById( R.id.faceValue );
        mForces    = v.findViewById( R.id.forcesValue );
        mBattery   = v.findViewById( R.id.batteryLevel );
        mRssiValue = v.findViewById( R.id.rssi );
        mStickerId = v.findViewById( R.id.stickerid );

        mStickerId.setText ( stickerId );

        // Clear back stack and return to main assets screen
        mBackAssets = v.findViewById( R.id.assetButton );
        mBackAssets.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainAssetScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    //    readRssi();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver( mGattUpdateReceiver, makeGattUpdateIntentFilter() );
        readRssi();
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver( mGattUpdateReceiver );
    }


    public static TelemetryFragment newInstance(String text) {

        TelemetryFragment f = new TelemetryFragment();
        Bundle b = new Bundle();
        b.putString("DeviceId", text);
        stickerId = text;
        f.setArguments(b);

        return f;
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if ( RESPONSE_AMBIENT_AVAILABLE.equals(action)) {
                float fValue      = intent.getFloatExtra( PRESSURE_DATA, ZERO_FLOAT );
                String value      = String.format("%.3f", fValue);
                String pressValue = value + BAR;
                mPressure.setText( pressValue );

                fValue = intent.getFloatExtra( HUMIDITY_DATA, ZERO_FLOAT)  * 100.0f;
                value = String.format("%.2f", fValue);
                String humidityValue = value + PERCENT;
                mHumidity.setText( humidityValue );

                fValue = intent.getFloatExtra( AMBIENT_DATA, ZERO_FLOAT );
                value = String.format("%.2f", fValue);
                String ambientValue = value + DEGREES_C;
                mAirTemp.setText( ambientValue );
            }

            if ( RESPONSE_SURFACE_AVAILABLE.equals(action)) {
                float fValue = intent.getFloatExtra( SURFACE_DATA, ZERO_FLOAT );
                String value = String.format("%.2f", fValue);
                value = value + DEGREES_C;
                mSurfTemp.setText( value );
            }

            if ( RESPONSE_HANDLING_AVAILABLE.equals(action)) {
                float fValue     = intent.getFloatExtra( FACEUP_DATA, ZERO_FLOAT );
                String value     = String.format("%.2f", fValue);
                String faceValue = value + DEGREES;
                mFaceup.setText( faceValue );

                fValue = intent.getFloatExtra( FORCES_DATA, ZERO_FLOAT );
                value = String.format("%.2f", fValue);
                String forcesValue = value + " g";
                mForces.setText( forcesValue );
                readRssi();
            }

            if ( RESPONSE_BATTERY_LEVEL.equals(action)) {

                int intData = intent.getIntExtra( INT_DATA, 0);
                mBattery.setText(String.valueOf("Battery " + intData + PERCENT));
            }
            if ( RESPONSE_RSSI_DATA.equals(action)) {

                int rssiData = intent.getIntExtra( INT_DATA,0);
                Log.d("RECVD", "RSSI " + rssiData);
                mRssiValue.setText(String.valueOf("Signal " + rssiData + " dB"));
                readBattery();
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction( RESPONSE_AMBIENT_AVAILABLE );
        intentFilter.addAction( RESPONSE_HANDLING_AVAILABLE );
        intentFilter.addAction( RESPONSE_SURFACE_AVAILABLE );
        intentFilter.addAction( RESPONSE_BATTERY_LEVEL );
        intentFilter.addAction( RESPONSE_RSSI_DATA );
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


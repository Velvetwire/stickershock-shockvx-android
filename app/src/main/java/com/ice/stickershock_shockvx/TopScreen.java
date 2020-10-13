package com.ice.stickershock_shockvx;


import android.app.ListActivity;
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

import com.ice.stickershock_shockvx.bluetooth.BluetoothLeService;

import java.util.ArrayList;
import java.util.List;

import static com.ice.stickershock_shockvx.bluetooth.BluetoothLeService.ACTION_BATTERY_LEVEL;
import static com.ice.stickershock_shockvx.bluetooth.BluetoothLeService.ACTION_BATTERY_LEVEL_AVAILABLE;
import static com.ice.stickershock_shockvx.bluetooth.BluetoothLeService.ACTION_READ_RSSI;
import static com.ice.stickershock_shockvx.bluetooth.BluetoothLeService.ACTION_RSSI_DATA_AVAILABLE;


public class TopScreen extends ListActivity {

    Button mNewAsset;
    TextView mAirTemp;
    TextView mHumidity;
    TextView mPressure;
    TextView mBattery;
    TextView mRssiValue;
    AssetListAdapter bla;
    List<Sticker> assetList = new ArrayList<Sticker>();

    private final String DEGREES_C = "\u2103";
    private final String DEGREES_F = "\u2109";
    private final String DEGREES = "\u00B0";
    private final String MILLIBAR = " mB";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ble_list);

        mNewAsset = findViewById( R.id.addAssetButton );
        Sticker s1 = new Sticker("TEST STICKER", 2551, 2343, 4610, 10132);
        Sticker s2 = new Sticker("Sticker topleft", 2456, 2158, 4710, 10132);
        Sticker s3 = new Sticker("Sticker s3", 2612, 2343, 5310, 10132);
        Sticker s4 = new Sticker("Sticker s3", 2709, 2233, 4440, 10066);

        assetList.add(s1);
        assetList.add(s2);
        assetList.add(s3);
        assetList.add(s4);
        bla = new AssetListAdapter(this, assetList);
        setListAdapter(bla);

        mNewAsset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TopScreen.this, ReadStickerNfc.class);
                startActivity(intent);
            }
        });
    //    readRssi();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        readRssi();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }



    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
/*
            if ( ACTION_TELEMETRY_AVAILABLE.equals(action)) {
                String value = intent.getStringExtra( EXTRA_DATA) + DEGREES_C;
                mAirTemp.setText( value );
                mSurfTemp.setText( value );
                mPressure.setText( value );
                mHumidity.setText( value );
            }
*/
            if ( ACTION_BATTERY_LEVEL_AVAILABLE.equals(action)) {

                int intData = intent.getIntExtra(BluetoothLeService.INT_DATA, 0);
                mBattery.setText(String.valueOf(intData + "%"));
            }
            if ( ACTION_RSSI_DATA_AVAILABLE.equals(action)) {

                int rssiData = intent.getIntExtra(BluetoothLeService.INT_DATA,0);
                Log.d("RECVD", "RSSI " + rssiData);
                mRssiValue.setText(String.valueOf(rssiData + " dB"));
                readBattery();
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

 //       intentFilter.addAction( ACTION_TELEMETRY_AVAILABLE );
 //       intentFilter.addAction( ACTION_BATTERY_LEVEL_AVAILABLE );
 //       intentFilter.addAction( ACTION_RSSI_DATA_AVAILABLE );
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
        sendBroadcast(intent);
    }
}


//=============================================================================
// project: ShockVx
//  module: Stickershock Android App for cold chain tracking.
//  author: Velvetwire, llc
//    file: MainAssetScreen.java
//
// Start screen for android shockVx app
// Lists all Stickershock beacons with telemetry
// Is not connected to any.
// Also contains a list of previously found beacons
//
// (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
//=============================================================================
package com.ice.stickershock_shockvx;


import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;

import com.ice.stickershock_shockvx.advertisement.IdentityAD;
import com.ice.stickershock_shockvx.advertisement.StandardAD;
import com.ice.stickershock_shockvx.advertisement.TelemetryAD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.ice.stickershock_shockvx.advertisement.Broadcast.*;
import static com.ice.stickershock_shockvx.bluetooth.GattAttributes.*;


public class MainAssetScreen extends ListActivity {

    Button mNewAsset;
    AssetListAdapter bla;
    List<Sticker> assetList = new ArrayList<Sticker>();

    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScanner mBluetoothLeScanner;
    ScanFilter mScanFilter;
    ScanSettings mScanSettings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_list);

        mNewAsset = findViewById( R.id.addAssetButton );

        bla = new AssetListAdapter(this, assetList);
        setListAdapter(bla);

        setScanFilter();
        setScanSettings();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        mBluetoothLeScanner.startScan(Arrays.asList(mScanFilter), mScanSettings, mScanCallback);

        // retrieved saved assets from file system
        mNewAsset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mBluetoothLeScanner.stopScan(mStopScanCallback);
               Intent intent = new Intent(MainAssetScreen.this, ReadStickerNfc.class);
               startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    //    mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
     //   mBluetoothLeScanner.startScan(Arrays.asList(mScanFilter), mScanSettings, mScanCallback);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }


// ---------------------------------------------------
//        Beacon and BLE advertisement detection

    // Set up scan filter for only detecting our beacons



    // now we can set up scan filter anyway we want, these values are just sample code
    private void setScanFilter() {
        ScanFilter.Builder mBuilder = new ScanFilter.Builder();

        mScanFilter = mBuilder.build();
    }


    private void setScanSettings() {
        ScanSettings.Builder mBuilder = new ScanSettings.Builder();
        mBuilder.setReportDelay(0);
        mBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        mScanSettings = mBuilder.build();
    }


    protected ScanCallback mScanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Sticker mySticker = new Sticker();
            ScanRecord mScanRecord = result.getScanRecord();

            mySticker.address = result.getDevice().getAddress();
            Log.d("BEACON", "RESULT " + result.toString());
            byte[] standardData = mScanRecord.getServiceData( ParcelUuid.fromString( ASSET_BROADCAST_STANDARD ) );
            byte[] extendedData = mScanRecord.getServiceData( ParcelUuid.fromString( ASSET_BROADCAST_EXTENDED ) );

            if ( standardData != null) {
                extractFieldsFromBytes( standardData, mySticker );
                extractFieldsFromBytes( extendedData,  mySticker);
            }

            updateList(mySticker, mySticker.address);
        }
    };

    protected ScanCallback mStopScanCallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.d(TAG, "BEACON SCAN STOPPED");
        }
    };


    private int TELEMETRY_RECORD  = 0x54;
    private int IDENTITY_RECORD  = 0x49;

    private boolean extractFieldsFromBytes(byte[] raw, Sticker mySticker) {

        List<byte []> adverts = new ArrayList<byte []>();
        int totalLength = raw.length;
        int startString = 0;
        byte stringLength = 0;
        int endString = 0;


        while ( endString < totalLength) {
            stringLength = (byte) (raw[startString] + 1);
           //  Log.d("NEW STRING B","StartString " + startString + "LENGTH " + stringLength + " END " + endString + " TOTAL " + totalLength);
            endString += stringLength;
            byte [] newArray = Arrays.copyOfRange(raw, startString, endString + 1);
            startString += stringLength;
            adverts.add(newArray);
            // Log.d("NEW STRING A","StartString " + startString + "LENGTH " + stringLength + " END " + endString + " TOTAL " + totalLength);
        }

        for ( byte [] byteArray : adverts) {
            byte packetType = byteArray[1];
            Log.d("BYTEARRAY", "TYPE " + packetType);
            if ( packetType == BROADCAST_TYPE_IDENTITY ) {
                Log.d("IDENTITY", "TIMECODE " + Integer.toString( (((byteArray[3] & 0xff) << 8) | byteArray[2] & 0xff)));
                Log.d("IDENTITY", "HASH" + Integer.toHexString( ((( byteArray[5] & 0xff) << 8) | byteArray[4] & 0xff)));
                Log.d("IDENTITY", "HORIZON " + byteArray[14]);
                mySticker.batteryLevel = byteArray[15];
                Log.d("IDENTITY", "BATTERY " +  byteArray[15]);
            }
            if ( packetType == BROADCAST_TYPE_TEMPERATURE ) {
                Log.d("SURFACE", "TEMP " + Integer.toString( (((byteArray[3] & 0xff) << 8) | byteArray[2] & 0xff)));
                mySticker.surface = (((byteArray[3] & 0xff) << 8) | byteArray[2] & 0xff);
            }
            if ( packetType == BROADCAST_TYPE_ATMOSPHERE ) {
                Log.d("EXTENDED", "TEMP " + Integer.toString( (((byteArray[3] & 0xff) << 8) | byteArray[2] & 0xff)));
                mySticker.ambient = (((byteArray[3] & 0xff) << 8) | byteArray[2] & 0xff);
                // Log.d("EXTENDED", "TEMPLO " + Integer.toString( (((byteArray[5] & 0xff) << 8) | byteArray[4] & 0xff)));
                // Log.d("EXTENDED", "TEMPHI " + Integer.toString( (((byteArray[7] & 0xff) << 8) | byteArray[6] & 0xff)));
                Log.d("EXTENDED", "HUMID " + Integer.toString( (((byteArray[9] & 0xff) << 8) | byteArray[8] & 0xff)));
                mySticker.humidity = (((byteArray[9] & 0xff) << 8) | byteArray[2] & 0xff);
                        // Log.d("EXTENDED", "HUMIDLO " + Integer.toString( (((byteArray[11] & 0xff) << 8) | byteArray[10] & 0xff)));
                // Log.d("EXTENDED", "HUMIDHI " + Integer.toString( (((byteArray[13] & 0xff) << 8) | byteArray[12] & 0xff)));
                Log.d("EXTENDED", "PRESSURE " + Integer.toString( (((byteArray[15] & 0xff) << 8) | byteArray[14] & 0xff)));
                mySticker.pressure = (((byteArray[15] & 0xff) << 8) | byteArray[14] & 0xff);
                // Log.d("EXTENDED", "PRESSLO " + Integer.toString( (((byteArray[17] & 0xff) << 8) | byteArray[16] & 0xff)));
                // Log.d("EXTENDED", "PRESSHI " + Integer.toString( (((byteArray[19] & 0xff) << 8) | byteArray[18] & 0xff)));

                return true;
            }
            if ( packetType == BROADCAST_TYPE_HANDLING) {
                Log.d("TEMPERATURE","PACKETLEN " + byteArray[0]);
                Log.d("TEMPERATURE", "TYPE "     + byteArray[1]);
            }
        }


        if (raw.length > 20) {
            return true;
        }
        return false;
    }


    private void updateList(Sticker newSticker, String address) {
        // check list to see if sticker is already there
        for (int i = 0; i < assetList.size(); i++) {
            if ( assetList.get(i).address.equals(address) == true) {
                assetList.set(i, newSticker);
                bla.notifyDataSetChanged();
                return;
            }
        }
        assetList.add(newSticker);
        bla.notifyDataSetChanged();
    }
}



// Scan Result
// {device=F8:D7:82:34:F2:18,
//    scanRecord=ScanRecord [
//       mAdvertiseFlags=5,
//       mServiceUuids=[56780000-5657-5353-2020-56454c564554],
//       mServiceSolicitationUuids=[],
//       mManufacturerSpecificData={},
//       mServiceData={0000180a-0000-1000-8000-00805f9b34fb=[3, 53, 66, -124, 121, 127, -116, -11]},
//       mTxPowerLevel=-2147483648,
//       mDeviceName=null],
//     rssi=-38,
//     timestampNanos=900564925985824,
//     eventType=27,
//     primaryPhy=1,
//     secondaryPhy=0,
//     advertisingSid=255,
//     txPower=127,
//     periodicAdvertisingInterval=0}
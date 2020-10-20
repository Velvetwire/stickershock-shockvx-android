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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.ice.stickershock_shockvx.bluetooth.Actions.*;



public class MainAssetScreen extends ListActivity {

    Button mNewAsset;
    AssetListAdapter bla;
    List<Sticker> assetList = new ArrayList<Sticker>();
    List<Beacon> beaconList = new ArrayList<Beacon>();

    BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScanner mBluetoothLeScanner;
    ScanFilter mScanFilter;
    ScanSettings mScanSettings;

    private static int MANUFACTURER_ID = 22103;         // Change this

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asset_list);

        mNewAsset = findViewById( R.id.addAssetButton );
        Sticker s1 = new Sticker("TEST STICKER", 2551, 2343, 4610, 10132);
        Sticker s2 = new Sticker("Sticker topleft", 2456, 2158, 4710, 10132);

        assetList.add(s1);
        assetList.add(s2);

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
    //    readRssi();
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
//        ByteBuffer mManufacturerData = ByteBuffer.allocate(23);
        //       ByteBuffer mManufacturerDataMask = ByteBuffer.allocate(24);
//
//        mBuilder.setManufacturerData(MANUFACTURER_ID, mManufacturerData.array(), mManufacturerDataMask.array());
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
            Beacon myBeacon = new Beacon();
            ScanRecord mScanRecord = result.getScanRecord();
            Log.d("BEACON", "SCAN " + result.toString() + " MANUF_ID" );
            byte[] manufacturerData = mScanRecord.getManufacturerSpecificData( MANUFACTURER_ID );

            if (manufacturerData != null) {
                myBeacon.rssi = result.getRssi();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    myBeacon.txPowerLevel = result.getTxPower();
                }

                myBeacon.address = result.getDevice().getAddress();
                Log.d("BEACON", "RSSI " + result.toString());

                byte[] raw = mScanRecord.getBytes();
                String rawOut = "";
                for (int i = 0; i < raw.length; i++) {
                    rawOut += String.valueOf(raw[i]);
                    rawOut += " ";
                }
                Log.d("AD", "RAW " + rawOut);
                extractRecordsFromBytes(raw, myBeacon.telemetry, myBeacon.identity);
                updateList(myBeacon, myBeacon.address);


                // mRssiText.setText( mRssi );
            }
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

    private boolean extractRecordsFromBytes(byte[] raw, TelemetryAD myTelemetry, IdentityAD myIdentity) {
        byte[] firstHeader = new byte[3];
        byte[] secondHeader = new byte[3];
        byte[] identityArray = new byte[29];
        byte[] telemetryArray = new byte[27];
        byte position = 0;
        byte firstLength = (byte) (raw[0] + 1);
        byte secondLength = (byte) (raw[firstLength] + 1);
        byte thirdLength = (byte) (raw[secondLength] + 1);

        if (raw.length > 50) {
            System.arraycopy(raw, 0, firstHeader, 0, firstLength);
            position += firstLength;
            System.arraycopy(raw, position, identityArray, 0, secondLength);
            position += secondLength;
            System.arraycopy(raw, position, secondHeader, 0, 3);
            System.arraycopy(raw, 34, telemetryArray, 0, 26);
            myTelemetry.populateFromByteArray(telemetryArray);
            myIdentity.populateFromByteArray(identityArray);

            Log.d("TELEMETRY", "PACKETLEN " + myTelemetry.packetlen);
            Log.d("TELEMETRY", "FLAG " + myTelemetry.flag);
            Log.d("TELEMETRY", "MANUFACTURER 0x" + Integer.toHexString(myTelemetry.manufacturer & 0xffff));
            Log.d("TELEMETRY", "RQTYPE " + Integer.toHexString(myTelemetry.requestType & 0xffff));
            Log.d("TELEMETRY", "LEN " + myTelemetry.smLength);
            Log.d("TELEMETRY", "TIME " + myTelemetry.timecode);
            Log.d("TELEMETRY", "SURFACE 0x" + Integer.toHexString(myTelemetry.surface & 0xffff));
            Log.d("TELEMETRY", "AMBIENT 0x" + Integer.toHexString(myTelemetry.ambient & 0xffff));
            Log.d("TELEMETRY", "HUMIDITY 0x" + Integer.toHexString(myTelemetry.humidity & 0xffff));
            Log.d("TELEMETRY", "PRESSURE 0x" + Integer.toHexString(myTelemetry.pressure & 0xffff));
            Log.d("TELEMETRY", "ALT " + myTelemetry.alternate);
            Log.d("TELEMETRY", "BATTERY " + myTelemetry.voltage);
            Log.d("TELEMETRY", "ANGLE " + myTelemetry.angle);
            Log.d("TELEMETRY", "DROPS " + myTelemetry.drops);
            Log.d("TELEMETRY", "BUMPS " + myTelemetry.bumps);

            Log.d("IDENTITY", "PACKETLEN " + myIdentity.packetlen);
            Log.d("IDENTITY", "FLAG " + myIdentity.flag);
            Log.d("IDENTITY", "MANUFACTURER " + myIdentity.manufacturer);
            Log.d("IDENTITY", "RQTYPE " + myIdentity.requestType);
            Log.d("IDENTITY", "LEN " + myIdentity.smLength);
            Log.d("IDENTITY", "TIME " + myIdentity.timecode);
            Log.d("IDENTITY", "IDENTITY " + myIdentity.identity);
            Log.d("IDENTITY", "VALIDITY " + myIdentity.validity);
            Log.d("IDENTITY", "BATTERY " + myIdentity.battery);
            Log.d("IDENTITY", "SIGNAL " + myIdentity.signal);


            return true;
        }
        return false;
    }

    private void updateList(Beacon newBeacon, String address) {
        // check list to see if beacon is already there
        for (int i = 0; i < beaconList.size(); i++) {
            if (beaconList.get(i).address.equals(address) == true) {
                beaconList.set(i, newBeacon);
                bla.notifyDataSetChanged();
                return;
            }
        }
        beaconList.add(newBeacon);
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
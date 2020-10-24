//=============================================================================
// project: Stickershock Explore Android
//  author: Velvetwire, llc
//  file: BluetoothControlActivity.c
//
//  Control Bluetooth LE devices.
//  Connect, display data, and display GATT services and characteristics
//
//=============================================================================
package com.ice.stickershock_shockvx.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.ice.stickershock_shockvx.R;
import com.ice.stickershock_shockvx.Sticker;
import com.ice.stickershock_shockvx.TabbedActivity;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.bluetooth.BluetoothGattCharacteristic.*;
import static com.ice.stickershock_shockvx.bluetooth.Actions.*;
import static com.ice.stickershock_shockvx.Constants.*;
/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.
 * The Activity communicates with {@code BluetoothLeService}, which in turn
 * interacts with the Bluetooth LE API.
 **/


public class BluetoothControlActivity extends Activity {
    private final static String TAG = BluetoothControlActivity.class.getSimpleName();


    private TextView mSerial;
    private Sticker mSticker;

    private int stickerState = STICKER_OPEN;

    private String mDeviceName = null;
    private String mDeviceUnit = null;
    private String mDeviceAddress = null;

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    // Using BluetoothLeService to handle bluetooth connection

    // create class for storing notification strings. We can then store all notifications in
    // a list structure, and then turn on all the notifies on.
    private class Notification {
         String service;
         String characteristic;

        public Notification(String service_uuid, String characteristic_uuid) {
            this.service        = service_uuid;
            this.characteristic = characteristic_uuid;
        }
    }

    public List<Notification> notifyList = new ArrayList<Notification>();


    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            Log.e(TAG, "Connecting to" + mDeviceAddress);
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.
    // This can be a result of read or notification operations.

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                updateConnectionState(R.string.connected);
                invalidateOptionsMenu();

            }
            else if (ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                updateConnectionState(R.string.disconnected);
                invalidateOptionsMenu();

            }
            else if (ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                enableAllNotifications();

                // here is where available data is found
                // as far as notification there seems to be a timing problem where one
                // can only set one notification at a time.

            }
            else if (ACTION_NOTIFY_SUCCESS.equals(action)) {
                enableAllNotifications();
            }

            else if (ACTION_READ_DATA_AVAILABLE.equals(action)) {
                String extraData = intent.getStringExtra ( STRING_DATA );
                Log.d("DISCOVERED", "READ DATA AVAILABLE" + extraData);
            }
            else if ( ACTION_MANUFACTURER_AVAILABLE.equals(action)) {
                String extraData = intent.getStringExtra ( STRING_DATA );
                mSticker.make = extraData;
                Log.d("Manufacturer", extraData);
                //mMake.setText(extraData);
            }
            else if ( ACTION_MODEL_AVAILABLE .equals(action)) {
                String extraData = intent.getStringExtra ( STRING_DATA );
                mSticker.model = extraData;
                Log.d("Model", extraData);

            }
            else if ( ACTION_FIRMWARE_AVAILABLE .equals(action)) {
                String extraData = intent.getStringExtra ( STRING_DATA );
                mSticker.firmware = extraData;
                Log.d("Firmware", extraData);
            }
            else if ( ACTION_HARDWARE_AVAILABLE .equals(action)) {
                String extraData = intent.getStringExtra ( STRING_DATA );
                mSticker.hardware = extraData;
                Log.d("Hardware", extraData);
            }
            else if ( ACTION_SERIAL_AVAILABLE .equals(action)) {
                String extraData = intent.getStringExtra ( STRING_DATA );
                mSticker.serial = extraData;
                mSerial.setText(extraData);
                Log.d("Serial", extraData);

            }
            else if ( ACTION_WRITE_DATA_AVAILABLE.equals(action)) {
                String  extraData = intent.getStringExtra(EXTRA_DATA);
                Log.d("ACTION", "WRITE DATA SUCCESS " + extraData);
            }
            else if ( ACTION_SENSOR_DATA_AVAILABLE.equals(action)) {
                String extraData = intent.getStringExtra( EXTRA_DATA );
                Log.d("SENSOR_DATA", "available " + extraData);

            }
         }
    };

    public void goToTabbedActivity()
    {
        Log.d("NOTIFY DONE", "GO TO TABBEDACTIVITY");

        Intent i = new Intent(BluetoothControlActivity.this, TabbedActivity.class);
        i.putExtra( EXTRAS_DEVICE_STATE, stickerState);
        i.putExtra( EXTRAS_DEVICE_UNIT, mDeviceUnit );
        startActivity(i);
    }

    // Enable all notifications that are in notifyList. Wait for callback to
    // set next notify
    public void enableAllNotifications()
    {
        Notification n;
        final Intent intent;

        if (notifyList.size() > 0) {                          // all notifications to be set in list
            n = notifyList.remove(0);                  // select next notification
            intent = new Intent( ACTION_SET_NOTIFICATION );
            intent.putExtra("SERVICE", n.service);
            intent.putExtra("CHARACTERISTIC", n.characteristic);
            sendBroadcast(intent);
        // all notifications processed, so go to telemetry page
        } else {
            goToTabbedActivity();
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connecting);

        final Intent intent = getIntent();
        mDeviceName    = intent.getStringExtra( EXTRAS_DEVICE_NAME );
        mDeviceAddress = intent.getStringExtra( EXTRAS_DEVICE_ADDRESS );
        mDeviceUnit    = intent.getStringExtra( EXTRAS_DEVICE_UNIT );

        // Sets up UI references.
        mSerial       = (TextView) findViewById(R.id.serial);

        mSticker = new Sticker();
        mSticker.setAddress(mDeviceAddress);

        getActionBar().setTitle("Connect to Stickershock" + mDeviceAddress);
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        final boolean b = bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Log.d(TAG, "BLUETOOTH CONTROL ACTIVITY" + mDeviceAddress + b);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "inside onResume");
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result= " + result);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(mGattUpdateReceiver);
        } catch(IllegalArgumentException e) {
              e.printStackTrace();
         }
        unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gatt_services, menu);
        if (mConnected) {
            menu.findItem(R.id.menu_connect).setVisible(false);
            menu.findItem(R.id.menu_disconnect).setVisible(true);
        } else {
            menu.findItem(R.id.menu_connect).setVisible(true);
            menu.findItem(R.id.menu_disconnect).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_connect:
                mBluetoothLeService.connect(mDeviceAddress);
                return true;
            case R.id.menu_disconnect:
                mBluetoothLeService.disconnect();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateConnectionState(final int resourceId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //mConnectionState.setText(resourceId);
            }
        });
    }


    private byte[] reverseArray(byte[] array)  {
        for(int i=0; i<array.length/2; i++) {
            byte temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
        return array;
    }

    private float byteArrayToFloat(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        return buffer.getFloat();
    }

    private byte[] floatToByteArray(float data) {
        return ByteBuffer.allocate(4).putFloat(data).array();
    }

    public void switchToTelemetry() {
    }



    // Retrieve read fields from sticker. Since this activity is attached to service, these bluetooth
    // services can be called directly, but can also be called using broadcast messages
    private boolean retrieveStickerProperties() {

        if ( mSticker.make == null) {
            transmitBroadcast( ACTION_GET_MANUFACTURER );
        }
        else if ((mSticker.model == null)) {
            transmitBroadcast( ACTION_GET_MODEL );
        }
        else if (( mSticker.serial == null)) {
            transmitBroadcast( ACTION_GET_SERIAL );
        }
        else if (( mSticker.hardware == null)) {
            transmitBroadcast( ACTION_GET_HARDWARE_REV );
        }

        return mSticker.isComplete();
    }

    private void transmitBroadcast(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    // Iterate through the supported GATT Services/Characteristics.

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        int i = 0;
        String service_uuid = null;
        String characteristic_uuid = null;
        String unknownServiceString = getResources().getString(R.string.unknown_service);
        String unknownCharaString   = getResources().getString(R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            service_uuid = gattService.getUuid().toString();
            Log.d("SERVICE", service_uuid);
            currentServiceData.put(
                    LIST_NAME, GattAttributes.lookup(service_uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, service_uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);

                int permissions = gattCharacteristic.getProperties();
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                characteristic_uuid = gattCharacteristic.getUuid().toString();

                // if characteristic can be set to notify, do it
                String properties = "";
                if ((permissions & PROPERTY_READ) > 0) {
                    properties = "R ";
                }
                if ((permissions & PROPERTY_WRITE) > 0) {
                    properties += "W ";
                }
                if ((permissions & PROPERTY_NOTIFY) > 0) {
                        notifyList.add (new Notification(service_uuid, characteristic_uuid));
                        properties += "N ";
                }
                    Log.d("    CHAR", characteristic_uuid + " " + properties);
                currentCharaData.put(
                        LIST_NAME, GattAttributes.lookup(characteristic_uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, characteristic_uuid);
                gattCharacteristicGroupData.add(currentCharaData);
                int charaProps = gattCharacteristic.getProperties();

                mGattCharacteristics.add(charas);
                gattCharacteristicData.add(gattCharacteristicGroupData);
            }

        }

    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction( ACTION_GATT_CONNECTED);
        intentFilter.addAction( ACTION_GATT_DISCONNECTED);
        intentFilter.addAction( ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction( ACTION_DATA_AVAILABLE);
        intentFilter.addAction( ACTION_READ_DATA_AVAILABLE);
        intentFilter.addAction( ACTION_WRITE_DATA_AVAILABLE);
        intentFilter.addAction( ACTION_SENSOR_DATA_AVAILABLE );
        intentFilter.addAction( ACTION_MANUFACTURER_AVAILABLE );
        intentFilter.addAction( ACTION_MODEL_AVAILABLE );
        intentFilter.addAction( ACTION_FIRMWARE_AVAILABLE );
        intentFilter.addAction( ACTION_HARDWARE_AVAILABLE );
        intentFilter.addAction( ACTION_SERIAL_AVAILABLE );
        intentFilter.addAction( ACTION_NOTIFY_SUCCESS );
        intentFilter.addAction( ACTION_NOTIFY_DONE );
        return intentFilter;
    }


}


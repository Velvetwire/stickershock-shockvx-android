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
import com.ice.stickershock_shockvx.TrackAsset;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY;
import static com.ice.stickershock_shockvx.bluetooth.BluetoothLeService.*;


/**
 * For a given BLE device, this Activity provides the user interface to connect, display data,
 * and display GATT services and characteristics supported by the device.
 * The Activity communicates with {@code BluetoothLeService}, which in turn
 * interacts with the Bluetooth LE API.
 **/

public class BluetoothControlActivity extends Activity {
    private final static String TAG = BluetoothControlActivity.class.getSimpleName();

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

//    private TextView mConnectionState;
//    private TextView mManufacturer;
//    private TextView mMake;
//    private TextView mModel;
    private TextView mSerial;
//    private TextView mHardware;
//    private TextView mFirmware;

    private Sticker mSticker;

    private String mDeviceName = null;
    private String mDeviceAddress = null;

    private BluetoothLeService mBluetoothLeService;
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private boolean mConnected = false;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";

    private final String DEGREES_C = "\u2103";
    private final String DEGREES_F = "\u2109";
    private final String DEGREES = "\u00B0";
    private final String MILLIBAR = " mB";

    public final float INTERVAL_ACQUIRE = 0.5f;
    public final float INTERVAL_FAST = 1.0f;
    public final float INTERVAL_MEDIUM = 5.0f;
    public final float INTERVAL_SLOW = 15.0f;
    // Using BluetoothLeService to handle bluetooth connection

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
                Log.d("ACTION", "GATT SERVICES");
                // Show all the supported services and characteristics on the user interface.
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
                Log.d("DISCOVERED", "Gatt services");

                // here is where available data is found
                // as far as notification there seems to be a timing problem where one
                // can only set one notification at a time.

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
            else if (ACTION_WRITE_DATA_AVAILABLE.equals(action)) {
                String  extraData = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                Log.d("ACTION", "WRITE DATA SUCCESS " + extraData);
            //    mBluetoothLeService.setInterval(INTERVAL_ACQUIRE);
                enableDiscovered(   );
            }

            else if (ACTION_TELEMETRY_AVAILABLE.equals(action)) {
                String extraData = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                Log.d("ACTION", "SURFTEMP " + extraData);
                // mSticker.notifySurfaceTemp = true;
                // mSticker.notifyAirTemp = true;
                //  mSticker.notifyHumidity = true;
                //  mSticker.notifyPressure = true;
                //  enableDiscovered( );

            }
        }
    };



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connecting);

        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

        // Sets up UI references.
        mSerial       = (TextView) findViewById(R.id.serial);

        //((TextView) findViewById(R.id.battery)).setText(mDeviceAddress);
        //mConnectionState = (TextView) findViewById(R.id.connection_state);
        //mManufacturer = (TextView) findViewById(R.id.manufacturer);
        //mMake         = (TextView) findViewById(R.id.make);
        //mModel        = (TextView) findViewById(R.id.model);
        //mHardware     = (TextView) findViewById(R.id.hardware);
        //mFirmware     = (TextView) findViewById(R.id.firmware);

        mSticker = new Sticker();
        mSticker.setAddress(mDeviceAddress);

        getActionBar().setTitle("Stickershock");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Bind to BluetoothLeService to handle Le communication
    //    Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
     //   final boolean b = bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);


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

    // data is sent to routine as tag and data with colon separator
    // Looks like AIR:29.427
    //            HUM:61.52
    //            PRE:1016.88
    //            SUR:21.125


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
        Intent intent=new Intent(BluetoothControlActivity.this, TrackAsset.class);
       startActivity(intent);
    }


    // turn on notifications after callback
    private boolean enableDiscovered(  ) {
/*
        if ( mSticker.notifyHumidity == false) {
            transmitBroadcast( ACTION_NOTIFY_TELEMETRY );
        }
*/
    return true;
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
                Log.d("    CHAR", characteristic_uuid);

                // if characteristic can be set to notify, do it
                if ((permissions & PROPERTY_NOTIFY) > 0) {
                    final Intent intent = new Intent( ACTION_SET_NOTIFICATION );
                    intent.putExtra("SERVICE", service_uuid);
                    intent.putExtra("CHARACTERISTIC", characteristic_uuid);
                    sendBroadcast(intent);
                }

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
        return intentFilter;
    }


}


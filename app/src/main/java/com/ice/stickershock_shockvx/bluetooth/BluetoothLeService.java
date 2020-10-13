package com.ice.stickershock_shockvx.bluetooth;


import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.UUID;

import static com.ice.stickershock_shockvx.bluetooth.GattAttributes.*;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING   = 1;
    private static final int STATE_CONNECTED    = 2;

    /* BROADCAST RESPONSES
     *
     */
    public final static String ACTION_GATT_CONNECTED =  "ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "ACTION_DATA_AVAILABLE";
    public final static String ACTION_SENSOR_DATA_AVAILABLE = "ACTION_SENSOR_DATA_AVAILABLE";
    public final static String ACTION_READ_DATA_AVAILABLE = "ACTION_READ_DATA_AVAILABLE";
    public final static String ACTION_WRITE_DATA_AVAILABLE = "ACTION_WRITE_DATA_AVAILABLE";
    public final static String ACTION_RSSI_DATA_AVAILABLE = "ACTION_RSSI_DATA_AVAILABLE";
    public final static String ACTION_BATTERY_LEVEL_AVAILABLE = "ACTION_BATTERY_LEVEL_AVAILABLE";
    public final static String ACTION_BATTERY_STATE_AVAILABLE = "ACTION_BATTERY_STATE_AVAILABLE";

    public final static String ACTION_MANUFACTURER_AVAILABLE = "ACTION_MANUFACTURER_AVAILABLE";
    public final static String ACTION_MODEL_AVAILABLE        = "ACTION_MODEL_AVAILABLE";
    public final static String ACTION_FIRMWARE_AVAILABLE     = "ACTION_FIRMWARE_AVAILABLE";
    public final static String ACTION_HARDWARE_AVAILABLE     = "ACTION_HARDWARE_AVAILABLE";
    public final static String ACTION_SERIAL_AVAILABLE       = "ACTION_SERIAL_AVAILABLE";

    public final static String ACTION_TELEMETRY_AVAILABLE     = "ACTION_TELEMTRY _AVAILABLE";
    public final static String ACTION_HUMIDITY_AVAILABLE     = "ACTION_HUMIDITY_AVAILABLE";
    public final static String ACTION_PRESSURE_AVAILABLE     = "ACTION_PRESSURE_AVAILABLE";
    public final static String ACTION_AIRTEMP_AVAILABLE      = "ACTION_AIRTEMP_AVAILABLE";
    public final static String ACTION_SURFTEMP_AVAILABLE     = "ACTION_SURFTEMP_AVAILABLE";


    // Broadcast Data Types
    public final static String EXTRA_DATA          = "EXTRA_DATA";
    public final static String INT_DATA            = "INT_DATA";
    public final static String STRING_DATA         = "STRING_DATA";

    public final static String ACTION_DISCONNECT   = "ACTION_DISCONNECT";
    // Broadcast Requests
    public final static String ACTION_LED_INCOMING  = "ACTION_LED_INCOMING";
    public final static String ACTION_SET_INTERVAL  = "ACTION_SET_INTERVAL";
    public final static String ACTION_READ_RSSI     = "ACTION_READ_RSSI";
    public final static String ACTION_BATTERY_LEVEL = "ACTION_BATTERY_LEVEL";
    public final static String ACTION_BATTERY_STATE = "ACTION_BATTERY_STATE";

    public final static String ACTION_GET_MANUFACTURER = "ACTION_GET_MANUFACTURER";
    public final static String ACTION_GET_MODEL        = "ACTION_GET_MODEL";
    public final static String ACTION_GET_FIRMWARE     = "ACTION_GET_FIRMWARE";
    public final static String ACTION_GET_HARDWARE_REV = "ACTION_GET_HARDWARE_REV";
    public final static String ACTION_GET_SERIAL       = "ACTION_GET_SERIAL";

    // Broadcast Notification Requests
    public final static String ACTION_SET_NOTIFICATION = "ACTION_SET_NOTIFICATION";

//    public final static UUID UUID_AIR_TEMP      = UUID.fromString( SENSOR_AIR_TEMP_CHAR);
//    public final static UUID UUID_PRESSURE      = UUID.fromString( SENSOR_PRESSURE_CHAR);
//    public final static UUID UUID_HUMIDITY      = UUID.fromString( SENSOR_HUMIDITY_CHAR);
//    public final static UUID UUID_SURFACE_TEMP  = UUID.fromString( SENSOR_SURFACE_TEMP_CHAR);

    public final static UUID UUID_MANUFACTURER  = UUID.fromString( MANUFACTURER_NAME );
    public final static UUID UUID_BATTERY_LEVEL = UUID.fromString( BATTERY_LEVEL );
    public final static UUID UUID_BATTERY_STATE = UUID.fromString( BATTERY_STATE );
    public final static UUID UUID_MODEL         = UUID.fromString ( MODEL_NUMBER );
    public final static UUID UUID_SERIAL        = UUID.fromString( SERIAL_NUMBER );
    public final static UUID UUID_HARDWARE_REV  = UUID.fromString( HARDWARE_REV );
    public final static UUID UUID_FIRMWARE      = UUID.fromString( FIRMWARE );




    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        /* Callback for read requests
            Presently handles
            BATTERY_LEVEL
            BATTERY_STATE
            MANUFACTURER
            MODEL
            FIRMWARE
            HARDWARE
            SERIAL
         */
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            Log.d(TAG, " CALLBACK READ");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                processReadData(characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG, " CALLBACK WRITE " + characteristic.toString());
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_WRITE_DATA_AVAILABLE, characteristic);
            }
        }

        /* Callback for read requests
              Presently handles
              Notification requests for
              SENSORS

             data notifies from
             SENSORS
             */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            Log.d(TAG, "CALLBACK CHANGED " + characteristic.getUuid().toString());
            broadcastUpdate(ACTION_SENSOR_DATA_AVAILABLE, characteristic);
        }


        /*
          Callback for RSSI request
         */
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "RSSI" + rssi + " dB");
                broadcastIntUpdate(ACTION_RSSI_DATA_AVAILABLE, rssi);
            }
        }
    };


// receives broadcast messages and performs action

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            Log.d("ACTION", action);
            if ( ACTION_DISCONNECT.equals(action)) {
                disconnect();
                close();
            }
            if ( ACTION_LED_INCOMING.equals(action)) {
                turnOnLed();
            }
            if ( ACTION_SET_INTERVAL.equals(action)) {
                float interval = 1.0f;
                setInterval(1.0f);
            }
            if ( ACTION_READ_RSSI.equals(action)) {
                readRssi();
            }
            if ( ACTION_BATTERY_LEVEL.equals(action)) {
                readBatteryLevel();
            }
            if ( ACTION_BATTERY_STATE.equals(action)) {
                readBatteryState();
            }
            if ( ACTION_GET_MANUFACTURER.equals(action)) {
                getManufacturer();
            }
            if ( ACTION_GET_MODEL.equals(action)) {
                getModel();
            }
            if ( ACTION_GET_FIRMWARE.equals(action)) {
                getFirmware();
            }
            if ( ACTION_GET_HARDWARE_REV.equals(action)) {
                getHardware();
            }
            if ( ACTION_GET_SERIAL.equals(action)) {
                getSerial();
            }

            if ( ACTION_SET_NOTIFICATION.equals(action)) {
                String service = intent.getStringExtra("SERVICE");
                String characteristic = intent.getStringExtra("CHARACTERISTIC");
                setNotification(service, characteristic);
            }

        }
    };


    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastIntUpdate(final String action, int value) {
        final Intent intent = new Intent(action);
        intent.putExtra(INT_DATA, value);
        sendBroadcast(intent);
    }

    private void broadcastStringUpdate(final String action, String value) {
        final Intent intent = new Intent(action);
        intent.putExtra(STRING_DATA, value);
        sendBroadcast(intent);
    }

    //
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {

        final Intent intent = new Intent(action);


        if (action.equals(ACTION_SENSOR_DATA_AVAILABLE)) {
            final byte[] data = characteristic.getValue();
            float outData = byteArrayToFloat(reverseArray(data));


        }
        if (action.equals(ACTION_WRITE_DATA_AVAILABLE)) {
            intent.putExtra(EXTRA_DATA, "1");
        }

        sendBroadcast(intent);
    }

    // Data comes from sensor in low to high order byte [00 01 02 03]
    // so we need to reverse array

    private void processReadData(BluetoothGattCharacteristic characteristic) {
        UUID uuid = characteristic.getUuid();

        if (uuid.equals(UUID_BATTERY_LEVEL)) {
            int value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            broadcastIntUpdate(ACTION_BATTERY_LEVEL_AVAILABLE, value);
        }else
        if (uuid.equals(UUID_BATTERY_STATE)) {
            int value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            broadcastIntUpdate(ACTION_BATTERY_STATE_AVAILABLE, value);
        }
        if (uuid.equals(UUID_MANUFACTURER)) {
            String svalue = characteristic.getStringValue(0);
            broadcastStringUpdate(ACTION_MANUFACTURER_AVAILABLE, svalue);
        }
        if (uuid.equals(UUID_MODEL)) {
            String svalue = characteristic.getStringValue(0);
            broadcastStringUpdate(ACTION_MODEL_AVAILABLE, svalue);
        }
        if (uuid.equals(UUID_FIRMWARE)) {
            String svalue = characteristic.getStringValue(0);
            broadcastStringUpdate(ACTION_FIRMWARE_AVAILABLE, svalue);
        }
        if (uuid.equals(UUID_HARDWARE_REV)) {
            String svalue = characteristic.getStringValue(0);
            broadcastStringUpdate(ACTION_HARDWARE_AVAILABLE, svalue);
        }
        if (uuid.equals(UUID_SERIAL)) {
            String svalue = characteristic.getStringValue(0);
            broadcastStringUpdate(ACTION_SERIAL_AVAILABLE, svalue);
        }
        else
            broadcastUpdate(ACTION_READ_DATA_AVAILABLE, characteristic);
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

    // --------------------------------------------------
    // Binders for service
    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    // --------------------------------------------------

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through BluetoothManager.

        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Cannot initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Cannot obtain a BluetoothAdapter.");
            return false;
        }
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(BluetoothGatt, int, int)} callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                Log.d(TAG, "Connecting to ..." + address);
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }

        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Cannot connect.");
            return false;
        }
        // Directly connect to the device, set the autoConnect parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection." + address);
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        Log.w(TAG, "Disconnecting");
        mBluetoothGatt.disconnect();
    }

    /**
     * Close connection to BLE device, and release resources
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicRead(BluetoothGatt, BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Write to a given {@code BluetoothGattCharacteristic}. The read result is reported
     * asynchronously through the {@code BluetoothGattCallback#onCharacteristicWrite(BluetoothGatt, BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to write to
     */
    public void writeCharacteristic(BluetoothGattCharacteristic characteristic, byte [] dataArray) {
        boolean status1 = false;
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        Log.d("WRITE", "value " + dataArray);
        characteristic.setValue( dataArray );
        mBluetoothGatt.writeCharacteristic(characteristic);

    }

    /**
     * Enables or disables notification on a given characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled If true, enable notification.  False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        UUID uuidDescriptor = UUID.fromString( CLIENT_CHARACTERISTIC_CONFIG);

    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;

        return mBluetoothGatt.getServices();
    }


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction( ACTION_DISCONNECT );
        intentFilter.addAction( ACTION_LED_INCOMING );
        intentFilter.addAction( ACTION_SET_INTERVAL );
        intentFilter.addAction( ACTION_READ_RSSI);
        intentFilter.addAction( ACTION_BATTERY_LEVEL );
        intentFilter.addAction( ACTION_BATTERY_STATE );
        intentFilter.addAction( ACTION_GET_MANUFACTURER );
        intentFilter.addAction( ACTION_GET_MODEL );
        intentFilter.addAction( ACTION_GET_FIRMWARE );
        intentFilter.addAction( ACTION_GET_HARDWARE_REV );
        intentFilter.addAction( ACTION_GET_SERIAL );
        intentFilter.addAction( ACTION_SET_NOTIFICATION );
        return intentFilter;
    }


    /*
     * Bluetooth read and write calls
     * Calls are accessed by using BroadcastReceiver system
     * BluetoothLeService runs all the time, but only way to communicate with service
     * without binding, which can be difficult, is to use the Broadcast Messaging system


       Set data scanning interval
       Responds to messages
       ACTION_SAMPLE_1SEC
       ACTION_SAMPLE_5SEC
       ACTION_SAMPLE_15SEC
     */

    public void openSensor( String stickerId  ) {
        byte[] dataArray = stickerId.getBytes();
        Log.d("OPEN SENSOR", dataArray[0] + " " +  dataArray[1] + " " + dataArray[2] + " " + dataArray[3]);

        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.SENSOR_CONTROL_SERVICE));
        BluetoothGattCharacteristic mIntervalChar = mService.getCharacteristic(UUID.fromString(SENSOR_CONTROL_OPENED));
        writeCharacteristic(mIntervalChar, dataArray);
    }

    public void closeSensor( String stickerId  ) {
        byte[] dataArray = stickerId.getBytes();
        Log.d("OPEN SENSOR", dataArray[0] + " " +  dataArray[1] + " " + dataArray[2] + " " + dataArray[3]);

        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.SENSOR_CONTROL_SERVICE));
        BluetoothGattCharacteristic mIntervalChar = mService.getCharacteristic(UUID.fromString(SENSOR_CONTROL_CLOSED));
        writeCharacteristic(mIntervalChar, dataArray);
    }

     public void setInterval(float interval)   {
        byte[] dataArray = reverseArray(floatToByteArray(interval));
        Log.d("INTERVAL", dataArray[0] + " " +  dataArray[1] + " " + dataArray[2] + " " + dataArray[3]);

//        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.SENSOR_TELEMETRY_SERVICE));
//        BluetoothGattCharacteristic mIntervalChar = mService.getCharacteristic(UUID.fromString(SENSOR_TELEMETRY_INTERVAL));
//        writeCharacteristic(mIntervalChar, dataArray);
    }
    /*
       Set data scanning interval
       ACTION_SAMPLE_1SEC
     */
    public void turnOnLed()   {
        byte[] dataArray = new byte[1];  // byte array of length 4
        dataArray[0] = 0x05;

        Log.d("LED", "Value " + dataArray[0]);
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(SENSOR_CONTROL_SERVICE));
        BluetoothGattCharacteristic mIdentifyChar = mService.getCharacteristic(UUID.fromString(SENSOR_CONTROL_IDENTIFY));
        writeCharacteristic(mIdentifyChar, dataArray);
    }

    public void readRssi() {
        mBluetoothGatt.readRemoteRssi();
    }

    public void readBatteryLevel() {
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.BATTERY_SERVICE));
        BluetoothGattCharacteristic mLevelChar = mService.getCharacteristic(UUID.fromString(BATTERY_LEVEL));
        readCharacteristic(mLevelChar);
    }

    public void readBatteryState() {
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.BATTERY_SERVICE));
        BluetoothGattCharacteristic mIdentifyChar = mService.getCharacteristic(UUID.fromString(BATTERY_STATE));
        readCharacteristic(mIdentifyChar);
    }

    public void getManufacturer() {
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.DEVICE_INFORMATION_SERVICE));
        BluetoothGattCharacteristic mLevelChar = mService.getCharacteristic(UUID.fromString(MANUFACTURER_NAME));
        readCharacteristic(mLevelChar);
    }

    public void getModel() {
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.DEVICE_INFORMATION_SERVICE));
        BluetoothGattCharacteristic mLevelChar = mService.getCharacteristic(UUID.fromString(MODEL_NUMBER));
        readCharacteristic(mLevelChar);
    }

    public void getFirmware() {
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.DEVICE_INFORMATION_SERVICE));
        BluetoothGattCharacteristic mLevelChar = mService.getCharacteristic(UUID.fromString(FIRMWARE));
        readCharacteristic(mLevelChar);
    }

    public void getHardware() {
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.DEVICE_INFORMATION_SERVICE));
        BluetoothGattCharacteristic mIdentifyChar = mService.getCharacteristic(UUID.fromString(HARDWARE_REV));
        readCharacteristic(mIdentifyChar);
    }

    public void getSerial() {
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.DEVICE_INFORMATION_SERVICE));
        BluetoothGattCharacteristic mIdentifyChar = mService.getCharacteristic(UUID.fromString(SERIAL_NUMBER ));
        readCharacteristic(mIdentifyChar);
    }

    public void setNotification ( String service, String value) {
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString( service ));
        BluetoothGattCharacteristic mChar = mService.getCharacteristic(UUID.fromString( value ));
        mBluetoothGatt.setCharacteristicNotification(mChar, true);

        Log.w(TAG, "SET NOTIFY " + value);
        BluetoothGattDescriptor descriptor = mChar.getDescriptor(UUID.fromString( CLIENT_CHARACTERISTIC_CONFIG));
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
    }

    public void setAtmosphericLowerAlarm( String lowerAlarm) {
            byte[] dataArray = lowerAlarm.getBytes();
            Log.d(TAG , "SET LOWER ALARM ATMOSPHERIC " + lowerAlarm);

            BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.SENSOR_ATMOSPHERE_SERVICE));
            BluetoothGattCharacteristic mIntervalChar = mService.getCharacteristic(UUID.fromString(SENSOR_ATMOSPHERE_LOWER));
            writeCharacteristic(mIntervalChar, dataArray);
        }

    public void setAtmosphericUpperAlarm( String upperAlarm) {
        byte[] dataArray = upperAlarm.getBytes();
        Log.d(TAG , "SET UPPER ALARM ATMOSPHERIC " + upperAlarm);

        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.SENSOR_ATMOSPHERE_SERVICE));
        BluetoothGattCharacteristic mIntervalChar = mService.getCharacteristic(UUID.fromString(SENSOR_ATMOSPHERE_UPPER));
        writeCharacteristic(mIntervalChar, dataArray);
    }

    public void setSurfaceLowerAlarm( String lowerAlarm) {
        byte[] dataArray = lowerAlarm.getBytes();
        Log.d(TAG , "SET LOWER ALARM SURFACE " + lowerAlarm);

        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.SENSOR_SURFACE_SERVICE));
        BluetoothGattCharacteristic mIntervalChar = mService.getCharacteristic(UUID.fromString(SENSOR_SURFACE_LOWER));
        writeCharacteristic(mIntervalChar, dataArray);
    }

    public void setSurfaceUpperAlarm( String upperAlarm) {
        byte[] dataArray = upperAlarm.getBytes();
        Log.d(TAG , "SET UPPER ALARM SURFACE " + upperAlarm);

        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(GattAttributes.SENSOR_SURFACE_SERVICE));
        BluetoothGattCharacteristic mIntervalChar = mService.getCharacteristic(UUID.fromString(SENSOR_SURFACE_UPPER));
        writeCharacteristic(mIntervalChar, dataArray);
    }



}


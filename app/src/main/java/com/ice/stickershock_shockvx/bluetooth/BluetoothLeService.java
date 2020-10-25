//=============================================================================
// project: ShockVx
//  module: Stickershock Android App for cold chain tracking.
//  author: Velvetwire, llc
//    file: BluetoothLeService.java
//
// Service class for bluetooth connections
// Rest of app communicates with service through broadcast messages
// Service class is declared in AndroidManifest
//
// (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
//=============================================================================

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
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import static com.ice.stickershock_shockvx.bluetooth.GattAttributes.*;
import static com.ice.stickershock_shockvx.bluetooth.Actions.*;
import static com.ice.stickershock_shockvx.Constants.*;
import static com.ice.stickershock_shockvx.Helpers.*;
import static java.lang.System.currentTimeMillis;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String           mBluetoothDeviceAddress;
    private BluetoothGatt    mBluetoothGatt;

    private int mConnectionState = STATE_DISCONNECTED;


    // Implements callback methods for GATT events that the app cares about.  For example,
    // connection change and services discovered.
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;

            if (newState == BluetoothProfile.STATE_CONNECTED ) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                mBluetoothGatt.discoverServices();

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
                broadcastUpdate( ACTION_GATT_SERVICES_DISCOVERED );
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }


        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            Log.d(TAG, " CALLBACK READ");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("READ", "VALUESTR " + characteristic.getStringValue(0));
                Log.d("READ", "VALUE " + characteristic.getValue());
                processReadData(characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            broadcastCharacteristicWrite ( characteristic, status );

        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {

            broadcastCharacteristicUpdate( characteristic );
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt,
                                            BluetoothGattDescriptor descriptor, int status) {
            Log.d(TAG, "ON DESCRIPTOR READ " + descriptor.getUuid().toString() + " " + status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {
            Log.d(TAG, "ON DESCRIPTOR WRITE " + descriptor.getUuid().toString() + " " + status);
            broadcastUpdate( RESPONSE_NOTIFY_SUCCESS );
        }

        /*
          Callback for RSSI request
         */
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "RSSI" + rssi + " dB");
                broadcastIntUpdate( RESPONSE_RSSI_DATA, rssi);
            }
        }
    };



// -------------------------------------------------------
// receives broadcast messages and performs appropriate action

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            Log.d("ACTION", action);
            switch ( action ) {
                case ACTION_DISCONNECT:
                    disconnect();
                    break;

                case ACTION_LED_INCOMING:
                    turnOnLed();
                    break;

                case ACTION_SET_INTERVAL:
                    float interval = 1.0f;
                    setInterval(1.0f);
                    break;

                case ACTION_SET_UTC_TIME:
                    setStickerTime();

                case ACTION_READ_RSSI:
                    readRssi();
                    break;

                case ACTION_BATTERY_LEVEL:
                    getCharacteristicReadValue ( BATTERY_SERVICE, BATTERY_LEVEL );
                    break;

                case ACTION_BATTERY_STATE:
                    getCharacteristicReadValue ( BATTERY_SERVICE, BATTERY_STATE );
                    break;

                case ACTION_GET_MANUFACTURER:
                    getCharacteristicReadValue ( DEVICE_INFORMATION_SERVICE, MANUFACTURER_NAME );
                    break;

                case ACTION_GET_MODEL:
                    getCharacteristicReadValue ( DEVICE_INFORMATION_SERVICE, MODEL_NUMBER );
                    break;

                case ACTION_GET_FIRMWARE:
                    getCharacteristicReadValue ( DEVICE_INFORMATION_SERVICE, FIRMWARE );
                    break;

                case ACTION_GET_HARDWARE_REV:
                    getCharacteristicReadValue ( DEVICE_INFORMATION_SERVICE, HARDWARE_REV );
                    break;

                case ACTION_GET_SERIAL:
                    getCharacteristicReadValue ( DEVICE_INFORMATION_SERVICE, SERIAL_NUMBER );
                    break;

                case ACTION_SET_NOTIFICATION:
                    String service = intent.getStringExtra("SERVICE");
                    String characteristic = intent.getStringExtra("CHARACTERISTIC");
                    setNotification(service, characteristic);
                    break;

                case ACTION_CHECK_STICKER_STATUS:
                    readOpenSticker();
                    break;

                case ACTION_CHECK_STICKER_CLOSED:
                    readClosedSticker();
                    break;

                case ACTION_OPEN_STICKER:
                    openSticker();
                    break;

                case ACTION_CLOSE_STICKER:
                    closeSticker();
                    break;

                default:
                    break;
            }
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastIntUpdate(final String action, int value) {
        Intent intent = new Intent(action);
        intent.putExtra( INT_DATA, value);
        sendBroadcast(intent);
    }

    private void broadcastStringUpdate(final String action, String value) {
        final Intent intent = new Intent(action);
        intent.putExtra( STRING_DATA, value);
        sendBroadcast(intent);
    }


    private void broadcastCharacteristicWrite ( BluetoothGattCharacteristic characteristic, int status ) {
        Log.d(TAG, " CALLBACK WRITE " + characteristic.getUuid().toString());
        if ( status == BluetoothGatt.GATT_SUCCESS ) {
            if ( characteristic.getUuid().toString().equals ( SENSOR_CONTROL_OPEN )) {
                broadcastUpdate( RESPONSE_STICKER_OPENED );
            }
            if ( characteristic.getUuid().toString().equals ( SENSOR_TELEMETRY_INTERVAL  )) {
                broadcastUpdate( RESPONSE_SET_INTERVAL);
            }
            if ( characteristic.getUuid().toString().equals ( SENSOR_ACCESS_TIME  )) {
                broadcastUpdate( RESPONSE_SET_UTC );
            }
        }
    }

    private void broadcastCharacteristicUpdate( BluetoothGattCharacteristic characteristic) {
        float outData, outData2, outData3;
        Intent intent = new Intent();
        String cString = characteristic.getUuid().toString();
        final byte[] data = reverseArray ( characteristic.getValue() );

        ByteBuffer buffer = ByteBuffer.wrap(data);

        switch ( cString )  {
         case SENSOR_HANDLING_VALUE:        // 48614d76
            intent.setAction( RESPONSE_HANDLING_AVAILABLE );
            outData = buffer.getFloat();
            outData2 = buffer.getFloat();
            outData3 = buffer.getFloat();

            intent.putExtra( FACEUP_DATA, outData2 );
            intent.putExtra( FORCES_DATA, outData3 );
            break;

         case SENSOR_SURFACE_VALUE:     // 53744d76
            intent.setAction( RESPONSE_SURFACE_AVAILABLE );
            outData = buffer.getFloat();
            intent.putExtra( SURFACE_DATA, outData );
            break;

         case SENSOR_ATMOSPHERE_VALUE:        //  41740000
            intent.setAction( RESPONSE_AMBIENT_AVAILABLE );
            outData  = buffer.getFloat();     // pressure
            outData2 = buffer.getFloat();     // humidity
            outData3 = buffer.getFloat();     // ambient

            intent.putExtra( PRESSURE_DATA, outData );
            intent.putExtra( HUMIDITY_DATA, outData2 );
            intent.putExtra( AMBIENT_DATA, outData3 );
            break;

         case  BATTERY_LEVEL:     //  41740000
            intent.setAction( RESPONSE_BATTERY_LEVEL );
            break;

         default:
            break;
        }
        sendBroadcast(intent);
    }
    // Data comes from sensor in low to high order byte [00 01 02 03]
    // so we need to reverse array
    private void processReadData(BluetoothGattCharacteristic characteristic) {
        String uuid = characteristic.getUuid().toString();
        int value = 0;
        String svalue = "";

        switch ( uuid )  {
        case BATTERY_LEVEL:
            value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            broadcastIntUpdate( RESPONSE_BATTERY_LEVEL, value);
            break;
        case BATTERY_STATE:
            value = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
            broadcastIntUpdate( RESPONSE_BATTERY_STATE,  value);
            break;
        case MANUFACTURER_NAME:
            svalue = characteristic.getStringValue(0);
            broadcastStringUpdate( RESPONSE_MANUFACTURER_AVAILABLE, svalue);
            break;
        case MODEL_NUMBER:
            svalue = characteristic.getStringValue(0);
            broadcastStringUpdate( RESPONSE_MODEL_AVAILABLE, svalue);
            break;
        case FIRMWARE:
            svalue = characteristic.getStringValue(0);
            broadcastStringUpdate( RESPONSE_FIRMWARE_AVAILABLE, svalue);
            break;
        case HARDWARE_REV:
            svalue = characteristic.getStringValue(0);
            broadcastStringUpdate( RESPONSE_HARDWARE_AVAILABLE, svalue);
            break;
        case SERIAL_NUMBER:
            svalue = characteristic.getStringValue(0);
            broadcastStringUpdate( RESPONSE_SERIAL_AVAILABLE, svalue);
            break;

        // to determine state of sticker, read SENSOR_CONTROL_OPEN
        // if it returns 0, sticker is ready to be opened
        // if the call returns a value, then the SENSOR_CONTROL_CLOSE field needs inspection
        case SENSOR_CONTROL_OPEN:
            svalue = characteristic.getStringValue(0);
            byte[] bvalue = characteristic.getValue();

            if ( bvalue[0] > 0) {
                Log.d("SERVICE", "ACTION_STICKER_OPENED " + svalue);
                broadcastStringUpdate(ACTION_CHECK_STICKER_CLOSED, svalue);
            }
            else {
                Log.d("SERVICE", "ACTION_STICKER_NEW " + svalue);
                broadcastStringUpdate(RESPONSE_STICKER_NEW, svalue);
            }
            break;

        case SENSOR_CONTROL_CLOSE:
             svalue = characteristic.getStringValue(0);
                byte[] cvalue = characteristic.getValue();
                Log.d("SERVICE", "ACTION_STICKER_CLOSE " + svalue);
                if ( cvalue[0] > 0)
                    broadcastStringUpdate( RESPONSE_STICKER_CLOSED, svalue);
                else
                    broadcastStringUpdate( RESPONSE_STICKER_OPENED, svalue);
                break;

         default:
                break;
        }
 //       else
 //           broadcastUpdate(ACTION_READ_DATA_AVAILABLE, characteristic);
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

        if ( mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if ( mBluetoothManager == null) {
                Log.e(TAG, "Cannot initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if ( mBluetoothAdapter == null) {
            Log.e(TAG, "Cannot obtain a BluetoothAdapter.");
            return false;
        }
        registerReceiver( mGattUpdateReceiver, makeGattUpdateIntentFilter());
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
    // public void writeCharacteristic(BluetoothGattCharacteristic characteristic, String dataArray) {
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
        intentFilter.addAction( ACTION_CHECK_STICKER_STATUS );
        intentFilter.addAction( ACTION_CHECK_STICKER_CLOSED );
        intentFilter.addAction( ACTION_OPEN_STICKER );
        intentFilter.addAction( ACTION_CLOSE_STICKER );

        intentFilter.addAction( ACTION_MEASUREMENT_INTERVAL_15 );
        intentFilter.addAction( ACTION_MEASUREMENT_INTERVAL_60 );
        intentFilter.addAction( ACTION_RECORDS_INTERVAL_15 );
        intentFilter.addAction( ACTION_RECORDS_INTERVAL_60 );
        intentFilter.addAction( ACTION_HANDLING_NONE );
        intentFilter.addAction( ACTION_HANDLING_CAREFUL );
        intentFilter.addAction( ACTION_HANDLING_FRAGILE );
        intentFilter.addAction( ACTION_ORIENTATION_FLAT );
        intentFilter.addAction( ACTION_ORIENTATION_UPRIGHT );
        intentFilter.addAction( ACTION_SET_UTC_TIME );

        intentFilter.addAction( RESPONSE_NOTIFY_SUCCESS );
        intentFilter.addAction( RESPONSE_STICKER_NEW );
        intentFilter.addAction( RESPONSE_STICKER_OPENED );
        intentFilter.addAction( RESPONSE_STICKER_CLOSED );
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

    public int getUtcTime () {
        int currentTime = (int) System.currentTimeMillis () / 1000;
        return currentTime;
    }


    public void setStickerTime(  ) {

        int utcSeconds = getUtcTime();
        byte utcTime[] = reverseArray( intToByteArray ( utcSeconds ));

        Log.d("SET STICKER TIME", utcTime[0] + " " +  utcTime[1] + " " + utcTime[2] + " " + utcTime[3]);

        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString( SENSOR_ACCESS_SERVICE ));
        BluetoothGattCharacteristic mIntervalChar = mService.getCharacteristic(UUID.fromString( SENSOR_ACCESS_TIME ));
        writeCharacteristic ( mIntervalChar, utcTime );
    }
    // SENSOR_CONTROL_OPENED VALUES
    // 0 not opened
    // opened -  set to UTC time opened



    public void openSticker(  ) {
        byte[] newUuid = new byte[16];
        for (int i = 0; i < 16; i++) {
            newUuid [i] = 0x41;
        }

        Log.d("OPEN SENSOR", newUuid[0] + " " +  newUuid[1] + " " + newUuid[2] + " " + newUuid[3]);

        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString( SENSOR_CONTROL_SERVICE ));
        BluetoothGattCharacteristic mIntervalChar = mService.getCharacteristic(UUID.fromString( SENSOR_CONTROL_OPEN  ));
        writeCharacteristic ( mIntervalChar, newUuid );
    }

    public void readOpenSticker () {
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString( SENSOR_CONTROL_SERVICE));
        BluetoothGattCharacteristic mOpenChar = mService.getCharacteristic(UUID.fromString( SENSOR_CONTROL_OPEN  ));
        readCharacteristic(mOpenChar);
    }

    public void closeSticker(  ) {
        byte[] dataArray = new byte[16];

        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(SENSOR_CONTROL_SERVICE));
        BluetoothGattCharacteristic mIntervalChar = mService.getCharacteristic(UUID.fromString(SENSOR_CONTROL_CLOSE ));
        writeCharacteristic(mIntervalChar, dataArray);
    }

    public void readClosedSticker () {
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString( SENSOR_CONTROL_SERVICE));
        BluetoothGattCharacteristic mOpenChar = mService.getCharacteristic(UUID.fromString( SENSOR_CONTROL_CLOSE ));
        readCharacteristic(mOpenChar);
    }

    public void setInterval(float interval)   {
        byte[] dataArray = reverseArray(floatToByteArray(interval));
        Log.d("INTERVAL", dataArray[0] + " " +  dataArray[1] + " " + dataArray[2] + " " + dataArray[3]);

        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(SENSOR_TELEMETRY_SERVICE));
        BluetoothGattCharacteristic mIntervalChar = mService.getCharacteristic(UUID.fromString(SENSOR_TELEMETRY_INTERVAL));
        writeCharacteristic(mIntervalChar, dataArray);
    }
    /*
       Set data scanning interval
       ACTION_SAMPLE_1SEC
     */
    public void turnOnLed()   {
        byte[] dataArray = new byte[1];  // byte array of length 4
        String dataString = "0x05";

        Log.d("LED", "Value " + dataArray[0]);
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString(SENSOR_CONTROL_SERVICE));
        BluetoothGattCharacteristic mIdentifyChar = mService.getCharacteristic(UUID.fromString(SENSOR_CONTROL_IDENTIFY));
        writeCharacteristic(mIdentifyChar, dataArray);
    }

    public void readRssi() {
        mBluetoothGatt.readRemoteRssi();
    }




    public void getCharacteristicReadValue ( String service, String characteristic) {
        BluetoothGattService mService = mBluetoothGatt.getService(UUID.fromString( service ));
        BluetoothGattCharacteristic mIdentifyChar = mService.getCharacteristic(UUID.fromString( characteristic ));
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
/*
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
*/
}



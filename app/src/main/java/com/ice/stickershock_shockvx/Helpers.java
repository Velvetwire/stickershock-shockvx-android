package com.ice.stickershock_shockvx;


import android.util.Log;

import java.nio.ByteBuffer;
import java.util.HashMap;

import static com.ice.stickershock_shockvx.bluetooth.Actions.ACTION_BATTERY_LEVEL;
import static com.ice.stickershock_shockvx.bluetooth.Actions.ACTION_READ_RSSI;

/**
 * This class includes the GATT attributes for Stickershock Shock VX.
 */
public class Helpers {

    public static byte[] reverseArray(byte[] array)  {
        for(int i=0; i<array.length/2; i++) {
            byte temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
        return array;
    }

    public static float byteArrayToFloat(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        return buffer.getFloat();
    }

    public static byte[] floatToByteArray(float data) {
        return ByteBuffer.allocate(4).putFloat(data).array();
    }

    public static byte[] intToByteArray( int data) {
        return ByteBuffer.allocate(4).putInt(data).array();
    }



    //
    int TX1Meter = - 75;
    float N        = (float) 3.0;
    public String calculateDistance(int rssi ) {
        int difference =  TX1Meter - rssi;

        float exponent = (float) ((float)difference / (10.0 * N));
        Log.d("DISTANCE", "DIFFERENCE " + difference);
        Log.d("DISTANCE", "EXPONENT " + exponent);
        double dist = Math.pow(10, exponent);
        double distance = Math.round(dist * 1.0) / 1.0;
        if (distance < 1.0) {
            return "<1";
        }
        return String.valueOf(distance);
    }

};
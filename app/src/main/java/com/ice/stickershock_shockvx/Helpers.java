package com.ice.stickershock_shockvx;


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



};
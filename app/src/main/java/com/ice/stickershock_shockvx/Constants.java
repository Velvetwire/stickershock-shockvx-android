/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *        file: Constants.java
 *
 *  Constants and strings
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
package com.ice.stickershock_shockvx;


import java.util.HashMap;

/**
 * This class includes some constants for Stickershock Shock VX.
 */
public class Constants {
    private static HashMap<String, String> attributes = new HashMap();


//  Intent variables for stickers
    public static final String EXTRAS_DEVICE_NAME    = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_UNIT    = "DEVICE_UNIT";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static final String EXTRAS_STICKER_STATE  = "STICKER_STATE";

    public static final String EXTRAS_UNIT    = "unit";
    public static final String EXTRAS_CONTROL = "control";
    public static final String EXTRAS_PRIMARY = "primary";

    // Sticker states
    public static int STICKER_NEW    = 0;
    public static int STICKER_OPEN   = 1;
    public static int STICKER_CLOSED = 2;

    // Bluetooth connection states
    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING   = 1;
    public static final int STATE_CONNECTED    = 2;

    // Units
    public static final String DEGREES_C      = "\u2103";
    public static final String DEGREES_F      = "\u2109";
    public static final String DEGREES        = "\u00B0";
    public static final String MILLIBAR       = " mB";
    public static final String BAR            = " bar";
    public static final String PERCENT        = "%";
    public static final String DECIBEL        = " dB";

    public static final float SECONDS_15      = 15.0f;
    public static final float SECONDS_60      = 60.0f;
    public static final float ZERO_FLOAT      = 0.0f;

    public static final int REQUEST_ENABLE_BT = 1;
    public static final int VIBRATE_150_MS    = 150;

    public static final String TEXT_XML       = "text/xml";
};
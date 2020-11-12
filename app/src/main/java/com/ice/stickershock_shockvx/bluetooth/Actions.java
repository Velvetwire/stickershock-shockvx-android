/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *        file: Actions.java
 *
 *  Constants and strings
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
package com.ice.stickershock_shockvx.bluetooth;


import java.util.HashMap;

/**
 * This class includes the GATT attributes for Stickershock Shock VX.
 */
public class Actions {
    private static HashMap<String, String> attributes = new HashMap();

    // BROADCAST ACTIONS

    public final static String ACTION_GATT_CONNECTED           = "ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED        = "ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE           = "ACTION_DATA_AVAILABLE";

    // BROADCAST RESPONSES
    public final static String RESPONSE_READ_DATA_AVAILABLE    = "RESPONSE_READ_DATA_AVAILABLE";
    public final static String RESPONSE_WRITE_DATA_AVAILABLE   = "RESPONSE_WRITE_DATA_AVAILABLE";
    public final static String RESPONSE_RSSI_DATA              = "RESPONSE_RSSI_DATA";
    public final static String RESPONSE_BATTERY_LEVEL          = "RESPONSE_BATTERY_LEVEL";
    public final static String RESPONSE_BATTERY_STATE          = "RESPONSE_BATTERY_STATE";

    // DEVICE INFO REQUESTS

    public final static String ACTION_GET_MANUFACTURER         = "ACTION_GET_MANUFACTURER";
    public final static String ACTION_GET_MODEL                = "ACTION_GET_MODEL";
    public final static String ACTION_GET_FIRMWARE             = "ACTION_GET_FIRMWARE";
    public final static String ACTION_GET_HARDWARE_REV         = "ACTION_GET_HARDWARE_REV";
    public final static String ACTION_GET_SERIAL               = "ACTION_GET_SERIAL";

    public final static String RESPONSE_MANUFACTURER_AVAILABLE = "RESPONSE_MANUFACTURER_AVAILABLE";
    public final static String RESPONSE_MODEL_AVAILABLE        = "RESPONSE_MODEL_AVAILABLE";
    public final static String RESPONSE_FIRMWARE_AVAILABLE     = "RESPONSE_FIRMWARE_AVAILABLE";
    public final static String RESPONSE_HARDWARE_AVAILABLE     = "RESPONSE_HARDWARE_AVAILABLE";
    public final static String RESPONSE_SERIAL_AVAILABLE       = "RESPONSE_SERIAL_AVAILABLE";

    public final static String RESPONSE_HANDLING_AVAILABLE     = "RESPONSE_HANDLING_AVAILABLE";
    public final static String RESPONSE_SURFACE_AVAILABLE      = "RESPONSE_SURFACE_AVAILABLE";
    public final static String RESPONSE_AMBIENT_AVAILABLE      = "RESPONSE_AMBIENT_AVAILABLE";

    // BROADCAST DATA TYPES
    public final static String EXTRA_DATA                      = "EXTRA_DATA";
    public final static String INT_DATA                        = "INT_DATA";
    public final static String STRING_DATA                     = "STRING_DATA";
    public final static String FLOAT_DATA                      = "FLOAT_DATA";
    public final static String BYTE_DATA                       = "BYTE_DATA";
    public final static String ALARM_DATA                      = "ALARM_DATA";


    public final static String PRESSURE_DATA                   = "PRESSURE_DATA";
    public final static String HUMIDITY_DATA                   = "HUMIDITY_DATA";
    public final static String AMBIENT_DATA                    = "AMBIENT_DATA";
    public final static String FACEUP_DATA                     = "FACEUP_DATA";
    public final static String FORCES_DATA                     = "FORCES_DATA";
    public final static String SURFACE_DATA                    = "SURFACE_DATA";

    public final static String ACTION_DISCONNECT               = "ACTION_DISCONNECT";

    // BROADCAST REQUESTS
    public final static String ACTION_LED_INCOMING             = "ACTION_LED_INCOMING";
    public final static String ACTION_SET_INTERVAL             = "ACTION_SET_INTERVAL";
    public final static String RESPONSE_SET_INTERVAL           = "RESPONSE_SET_INTERVAL";
    public final static String ACTION_READ_RSSI                = "ACTION_READ_RSSI";
    public final static String ACTION_BATTERY_LEVEL            = "ACTION_BATTERY_LEVEL";
    public final static String ACTION_BATTERY_STATE            = "ACTION_BATTERY_STATE";



    // BROADCAST NOTIFICATION REQUESTS
    public final static String ACTION_SET_NOTIFICATION         = "ACTION_SET_NOTIFICATION";
    public final static String RESPONSE_NOTIFY_SUCCESS         = "RESPONSE_NOTIFY_SUCCESS";
    public final static String RESPONSE_NOTIFY_DONE            = "RESPONSE_NOTIFY_DONE";

    public final static String ACTION_SET_UTC_TIME             = "ACTION_SET_UTC_TIME";
    public final static String RESPONSE_SET_UTC                = "RESPONSE_SET_UTC_SUCCESS ";

    public final static String ACTION_CHECK_STICKER_STATUS     = "CHECK_STICKER_STATUS";
    public final static String ACTION_CHECK_STICKER_CLOSED     = "CHECK_STICKER_CLOSED";
    public final static String ACTION_OPEN_STICKER             = "ACTION_OPEN_STICKER";
    public final static String ACTION_CLOSE_STICKER            = "ACTION_CLOSE_STICKER";

    public final static String RESPONSE_STICKER_NEW            = "RESPONSE_STICKER_NEW";
    public final static String RESPONSE_STICKER_OPENED         = "RESPONSE_STICKER_OPENED";
    public final static String RESPONSE_STICKER_CLOSED         = "RESPONSE_STICKER_CLOSED";

    // SETTINGS ACTIONS
    public final static String ACTION_MEASUREMENT_INTERVAL_15  = "ACTION_MEASUREMENT_INTERVAL_15";
    public final static String ACTION_MEASUREMENT_INTERVAL_60  = "ACTION_MEASUREMENT_INTERVAL_60";
    public final static String ACTION_RECORDS_INTERVAL_15      = "ACTION_RECORDS_INTERVAL_15";
    public final static String ACTION_RECORDS_INTERVAL_60      = "ACTION_RECORDS_INTERVAL_60";
    public final static String ACTION_HANDLING_NONE            = "ACTION_HANDLING_NONE";
    public final static String ACTION_HANDLING_CAREFUL         = "ACTION_HANDLING_CAREFUL";
    public final static String ACTION_HANDLING_FRAGILE         = "ACTION_HANDLING_FRAGILE";
    public final static String ACTION_ORIENTATION_FLAT         = "ACTION_HANDLING_FLAT";
    public final static String ACTION_ORIENTATION_UPRIGHT      = "ACTION_HANDLING_UPRIGHT";
    public final static String ACTION_SET_ANGLE_ALARM          = "ACTION_SET_ANGLE_ALARM";
    public final static String ACTION_SET_AMBIENT_LOWER        = "ACTION_SET_AMBIENT_LOWER";
    public final static String ACTION_SET_AMBIENT_UPPER        = "ACTION_SET_AMBIENT_UPPER";
    public final static String ACTION_SET_SURFACE_LOWER        = "ACTION_SET_SURFACE_LOWER";
    public final static String ACTION_SET_SURFACE_UPPER        = "ACTION_SET_SURFACE_UPPER";
};
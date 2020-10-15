package com.ice.stickershock_shockvx.bluetooth;


import java.util.HashMap;

/**
 * This class includes the GATT attributes for Stickershock Shock VX.
 */
public class Actions {
    private static HashMap<String, String> attributes = new HashMap();
    /* BROADCAST RESPONSES
     *
     */
    public final static String ACTION_GATT_CONNECTED           = "ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED        = "ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE           = "ACTION_DATA_AVAILABLE";
    public final static String ACTION_SENSOR_DATA_AVAILABLE    = "ACTION_SENSOR_DATA_AVAILABLE";
    public final static String ACTION_READ_DATA_AVAILABLE      = "ACTION_READ_DATA_AVAILABLE";
    public final static String ACTION_WRITE_DATA_AVAILABLE     = "ACTION_WRITE_DATA_AVAILABLE";
    public final static String ACTION_RSSI_DATA_AVAILABLE      = "ACTION_RSSI_DATA_AVAILABLE";
    public final static String ACTION_BATTERY_LEVEL_AVAILABLE  = "ACTION_BATTERY_LEVEL_AVAILABLE";
    public final static String ACTION_BATTERY_STATE_AVAILABLE  = "ACTION_BATTERY_STATE_AVAILABLE";

    public final static String ACTION_MANUFACTURER_AVAILABLE   = "ACTION_MANUFACTURER_AVAILABLE";
    public final static String ACTION_MODEL_AVAILABLE          = "ACTION_MODEL_AVAILABLE";
    public final static String ACTION_FIRMWARE_AVAILABLE       = "ACTION_FIRMWARE_AVAILABLE";
    public final static String ACTION_HARDWARE_AVAILABLE       = "ACTION_HARDWARE_AVAILABLE";
    public final static String ACTION_SERIAL_AVAILABLE         = "ACTION_SERIAL_AVAILABLE";

    public final static String ACTION_HANDLING_AVAILABLE       = "ACTION_HANDLING_AVAILABLE";
    public final static String ACTION_SURFACE_AVAILABLE        = "ACTION_SURFACE_AVAILABLE";
    public final static String ACTION_AMBIENT_AVAILABLE        = "ACTION_AMBIENT_AVAILABLE";

    // Broadcast Data Types
    public final static String EXTRA_DATA = "EXTRA_DATA";
    public final static String INT_DATA = "INT_DATA";
    public final static String STRING_DATA = "STRING_DATA";
    public final static String FLOAT_DATA = "FLOAT_DATA";
    public final static String BYTE_DATA  = "BYTE_DATA";
    public final static String PRESSURE_DATA  = "PRESSURE_DATA";
    public final static String HUMIDITY_DATA  = "HUMIDITY_DATA";
    public final static String AMBIENT_DATA   = "AMBIENT_DATA";
    public final static String FACEUP_DATA    = "FACEUP_DATA";
    public final static String FORCES_DATA    = "FORCES_DATA_DATA";

    public final static String ACTION_DISCONNECT       = "ACTION_DISCONNECT";
    // Broadcast Requests
    public final static String ACTION_LED_INCOMING     = "ACTION_LED_INCOMING";
    public final static String ACTION_SET_INTERVAL     = "ACTION_SET_INTERVAL";
    public final static String ACTION_READ_RSSI        = "ACTION_READ_RSSI";
    public final static String ACTION_BATTERY_LEVEL    = "ACTION_BATTERY_LEVEL";
    public final static String ACTION_BATTERY_STATE    = "ACTION_BATTERY_STATE";

    public final static String ACTION_GET_MANUFACTURER = "ACTION_GET_MANUFACTURER";
    public final static String ACTION_GET_MODEL        = "ACTION_GET_MODEL";
    public final static String ACTION_GET_FIRMWARE     = "ACTION_GET_FIRMWARE";
    public final static String ACTION_GET_HARDWARE_REV = "ACTION_GET_HARDWARE_REV";
    public final static String ACTION_GET_SERIAL       = "ACTION_GET_SERIAL";

    // Broadcast Notification Requests
    public final static String ACTION_SET_NOTIFICATION = "ACTION_SET_NOTIFICATION";
    public final static String ACTION_NOTIFY_SUCCESS = "ACTION_NOTIFY_SUCCESS";
    public final static String ACTION_NOTIFY_DONE = "ACTION_NOTIFY_DONE";

    public final static String ACTION_OPEN_STICKER = "ACTION_OPEN_STICKER";
    public final static String ACTION_CLOSE_STICKER = "ACTION_CLOSE_STICKER";

    public final static String ACTION_MEASUREMENT_INTERVAL_15 = "ACTION_MEASUREMENT_INTERVAL_15";
    public final static String ACTION_MEASUREMENT_INTERVAL_60 = "ACTION_MEASUREMENT_INTERVAL_60";
    public final static String ACTION_RECORDS_INTERVAL_15 = "ACTION_RECORDS_INTERVAL_15";
    public final static String ACTION_RECORDS_INTERVAL_60 = "ACTION_RECORDS_INTERVAL_60";
    public final static String ACTION_HANDLING_NONE = "ACTION_HANDLING_NONE";
    public final static String ACTION_HANDLING_CAREFUL = "ACTION_HANDLING_CAREFUL";
    public final static String ACTION_HANDLING_FRAGILE = "ACTION_HANDLING_FRAGILE";
    public final static String ACTION_ORIENTATION_FLAT = "ACTION_HANDLING_FLAT";
    public final static String ACTION_ORIENTATION_UPRIGHT = "ACTION_HANDLING_UPRIGHT";

};
/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *        file: SensorControl.java
 *
 *  Sensor Control Commands
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
package com.ice.stickershock_shockvx.appsupport;

public class SensorControl {

    // Control Service Identifier
    public static final String SENSOR_CONTROL_SERVICE       = "56780000-5657-5353-2020-56454c564554";
    public static final String SENSOR_CONTROL_IDENTIFY      = "56784964-5657-5353-2020-56454c564554";  // W

    // Tracking identification characteristics
    public static final String SENSOR_CONTROL_NODE          = "5678546e-5657-5353-2020-56454c564554";  // R W
    public static final String SENSOR_CONTROL_LOCK          = "5678546c-5657-5353-2020-56454c564554";  // W

    // Tracking window characteristics
    public static final String SENSOR_CONTROL_OPEN          = "5678546f-5657-5353-2020-56454c564554";  // Sensor Open
    public static final String SENSOR_CONTROL_CLOSE         = "56785463-5657-5353-2020-56454c564554";  // Sensor Close
    public static final String SENSOR_CONTROL_WINDOW        = "56785477-5657-5353-2020-56454c564554";  // read only
    public static final String SENSOR_CONTROL_SUMMARY       = "56784975-5657-5353-2020-56454c564554";  // short status, char memory, char storage

    public class ControlWindow {
        int      opened;     // UTC time opened (0 = not opened) 4 bytes
        int      closed;     // UTC time closed (0 = not closed) 4 bytes
    }

    enum  TrackingWindow  {
        TRACKING_WINDOW_OPENED,
        TRACKING_WINDOW_CLOSED
    };

    public class ControlSummary {
        int            status;         // Status flags             (16)
        char           memory;         // Memory percentage available (0 - 100)
        char           storage;        // Storage percentage available (0 - 100)
    }

    public static final char CONTROL_SURFACE_SENSOR_OK   = (1 << 0);
    public static final char CONTROL_AMBIENT_SENSOR_OK   = (1 << 1);
    public static final char CONTROL_HUMIDITY_SENSOR_OK  = (1 << 2);
    public static final char CONTROL_PRESSURE_SENSOR_OK  = (1 << 3);
    public static final char CONTROL_MOVEMENT_SENSOR_OK  = (1 << 4);
}

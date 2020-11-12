/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *        file: SensorSurface.java
 *
 *  Sensor Surface Commands
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
package com.ice.stickershock_shockvx.appsupport;

public class SensorSurface {

    // Surface Service
    public static final String SENSOR_SURFACE_SERVICE       = "53740000-5657-5353-2020-56454c564554";
    public static final String SENSOR_SURFACE_VALUE         = "53744d76-5657-5353-2020-56454c564554";   // read notify
    public static final String SENSOR_SURFACE_LOWER         = "53744c6c-5657-5353-2020-56454c564554";   // read write
    public static final String SENSOR_SURFACE_UPPER         = "5374556c-5657-5353-2020-56454c564554";   // read write

    public static final String SENSOR_SURFACE_EVENT         = "53745265-5657-5353-2020-56454c564554";
    public static final String SENSOR_SURFACE_COUNT         = "53745263-5657-5353-2020-56454c564554";

    // archive event
    public class SurfaceEvent {

        int     time;               // UTC time stamp
        char    temperature;        // Temperature value (1/100 Degree Celsius)
    }
}

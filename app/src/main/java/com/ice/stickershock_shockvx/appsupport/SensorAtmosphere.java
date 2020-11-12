/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *        file: SensorAtmosphere.java
 *
 *  Sensor Atmosphere Commands
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
package com.ice.stickershock_shockvx.appsupport;

public class SensorAtmosphere {

    // Atmospheric Service
    public static final String SENSOR_ATMOSPHERE_SERVICE    = "41740000-5657-5353-2020-56454c564554";
    public static final String SENSOR_ATMOSPHERE_VALUE      = "41744d76-5657-5353-2020-56454c564554";   // read notify
    public static final String SENSOR_ATMOSPHERE_LOWER      = "41744c6c-5657-5353-2020-56454c564554";   // read write
    public static final String SENSOR_ATMOSPHERE_UPPER      = "4174556c-5657-5353-2020-56454c564554";   // read write
    public static final String SENSOR_ATMOSPHERE_EVENT      = "41745265-5657-5353-2020-56454c564554";
    public static final String SENSOR_ATMOSPHERE_COUNT      = "41745263-5657-5353-2020-56454c564554";

    public class AtmosphereValues {
        float       ambient;         // Ambient temperature (deg C)
        float       humidity;        // Humidity (saturation)
        float       pressure;        // Air pressure (bars)
    }

    // archive entry event

    public class AtmosphereEvent {

        int     time;               // UTC time stamp

        char    temperature;        // Temperature value (1/100 Degree Celsius)
        char    humidity;           // Humidity value (1/100 percent)
        char    pressure;           // Pressure value (millibars)

    }
}

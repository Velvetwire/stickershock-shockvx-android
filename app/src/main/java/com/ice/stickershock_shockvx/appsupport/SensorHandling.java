/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *        file: SensorHandling.java
 *
 *  Sensor Handling Commands
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
package com.ice.stickershock_shockvx.appsupport;

public class SensorHandling {


    // Handling Service
    public static final String SENSOR_HANDLING_SERVICE      = "48610000-5657-5353-2020-56454c564554";
    public static final String SENSOR_HANDLING_VALUE        = "48614d76-5657-5353-2020-56454c564554";
    public static final String SENSOR_HANDLING_LIMIT        = "48614c76-5657-5353-2020-56454c564554";



    public class HandlingValues {
        float           force;              // Force (in gravs)
        float           angle;              // Angle (in degrees)

        char            orientation;        // Orientation face (0 = unknown, don't care)
    }

    enum  OrientationFace  {

        ORIENTATION_FACE_UNKNOWN,
        ORIENTATION_FACE_UPRIGHT,
        ORIENTATION_FACE_INVERTED,
        ORIENTATION_FACE_LEFTHAND,
        ORIENTATION_FACE_RIGHTHAND,
        ORIENTATION_FACE_DOWN,
        ORIENTATION_FACE_UP,
        ORIENTATION_Faces

    };
}

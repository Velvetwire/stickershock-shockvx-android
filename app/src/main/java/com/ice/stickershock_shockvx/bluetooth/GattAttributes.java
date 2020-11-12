/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *        file: GattAttributes.java
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
public class   GattAttributes {
    private static HashMap<String, String> attributes = new HashMap();

    // GENERIC BLUETOOTH SERVICES
    public static final String GENERIC_ACCESS_SERVICE       = "00001800-0000-1000-8000-00805f9b34fb";
    public static final String GENERIC_ATTRIBUTE_SERVICE    = "00001801-0000-1000-8000-00805f9b34fb";
    public static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    // ShockVX SERVICES
    // ACCESS SERVICE
    public static final String SENSOR_ACCESS_SERVICE        = "00004143-5657-5353-2020-56454c564554";
    public static final String SENSOR_ACCESS_TIME           = "00005554-5657-5353-2020-56454C564554";
    public static final String SENSOR_ACCESS_PASSKEY        = "0000504b-5657-5353-2020-56454C564554";

    // RECORDS SERVICE
   public static final String SENSOR_RECORDS_SERVICE        = "54720000-5657-5353-2020-56454c564554";
   public static final String SENSOR_RECORDS_INTERVAL       = "54725269-5657-5353-2020-56454c564554";
   // public static final String SENSOR_RECORDS_CURSOR      = "54725263-5657-5353-2020-56454c564554";
   //   public static final String SENSOR_RECORDS_DATA      = "54725264-5657-5353-2020-56454c564554";

  // Sensor access commands

  // Expected primary and control UUIDs for a Stickershock device
    public static final String LOADER_CONTROL_SERVICE       = "00004655-0000-1000-8000-00805f9b34fb";
    public static final String UPDATE_CONTROL_SERVICE       = "00004655-5657-5353-2020-56454c564554";

    public static final int PackageMagic                    = 0x50465353;


    static {
        // Standard Services.
        attributes.put( GENERIC_ACCESS_SERVICE,       "Generic Access" );
        attributes.put( GENERIC_ATTRIBUTE_SERVICE, "Generic Attribute" );

        // Stickershock Services
        attributes.put( SENSOR_ACCESS_SERVICE,    "Sensor Access Service");
        attributes.put( SENSOR_RECORDS_SERVICE,   "Sensor Records Service");

    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }

}


/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *        file: DeviceUpdate.java
 *
 *  Device Update Commands
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
package com.ice.stickershock_shockvx.appsupport;

public class DeviceUpdate {

    // DEVICE UPDATE
    public static final String DEVICE_UPDATE_STATUS_PREFIX = "00005056";
    public static final String DEVICE_UPDATE_REGION_PREFIX = "0000524d";
    public static final String DEVICE_UPDATE_RECORD_PREFIX = "00005552";

    public static final int UPDATE_REQUEST_RESET           = 0x504f5453;     // Request shutdown notice ('STOP')
    public static final int UPDATE_REQUEST_ERASE           = 0x4f52455a;     // Request setttings erase ('ZERO')
    public static final int UPDATE_REQUEST_CLEAR           = 0x45504957;     // Request storage erase ('WIPE')
    public static final int UPDATE_REQUEST_BLANK           = 0x45455246;     // Request firmware erase ('FREE')

    public static final int UPDATE_RESPONSE_EMPTY          = 0x454e4f4e;     // Respond empty package ('NONE')
    public static final int UPDATE_RESPONSE_PACKAGE        = 0x45444f43;     // Respond with package ('CODE')
    public static final int UPDATE_RESPONSE_ACCEPTED       = 0x454e4f44;     // Respond accepted ('DONE')
    public static final int UPDATE_RESPONSE_REJECTED       = 0x4c494146;     // Respond rejected ('FAIL')
}

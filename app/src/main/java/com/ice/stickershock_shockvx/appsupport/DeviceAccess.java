/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *        file: DeviceAccess.java
 *
 *  Device Access Commands
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
package com.ice.stickershock_shockvx.appsupport;

public class DeviceAccess {

    // DEVICE ACCESS
    public static final String SENSOR_ACCESS_CONTROL       = "00004357-5657-5353-2020-56454C564554";  // R W N

    public static final String DEVICE_ACCESS_PASSKEY_PREFIX = "0000504B";

    public static final int ACCESS_REQUEST_SHUTDOWN        = 0x504f5453;      // Request shutdown notice ('STOP')
    public static final int ACCESS_REQUEST_FACTORY         = 0x4f52455a;      // Request factory erase ('ZERO')
    public static final int ACCESS_REQUEST_REBOOT          = 0x544f4f42;      // Request reboot ('BOOT')
    public static final int ACCESS_REQUEST_LOADER          = 0x4d414f4c;      // Request boot loader ('LOAD')
    public static final int ACCESS_REQUEST_ERASE           = 0x45504957;      // Request storage erase ('WIPE')

    public static final int ACCESS_RESPONSE_ACCEPTED       = 0x454e4f44;      // Respond accepted ('DONE')
    public static final int ACCESS_RESPONSE_REJECTED       = 0x4c494146;      // Respond rejected ('FAIL')
    public static final int ACCESS_RESPONSE_LOCKED         = 0x4b434f4c;      // Respond access locked ('LOCK')
    public static final int ACCESS_RESPONSE_OPENED         = 0x4e45504f;      // Respond access open ('OPEN')

    enum  AccessResponse {
        ACCESS_RESPONSE_SUCCESS,
        ACCESS_RESPONSE_FAILURE
    };

    enum  AccessStatus  {
        ACCESS_RESPONSE_OPENED,
        ACCESS_RESPONSE_LOCKED
    };

}

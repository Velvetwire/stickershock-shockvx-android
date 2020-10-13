package com.ice.stickershock_shockvx.bluetooth;


import java.util.HashMap;

/**
 * This class includes the GATT attributes for Stickershock Shock VX.
 */
public class GattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String GENERIC_ACCESS_SERVICE       = "00001800-0000-1000-8000-00805f9b34fb";
    public static String GENERIC_ATTRIBUTE_SERVICE    = "00001801-0000-1000-8000-00805f9b34fb";
    public static String DEVICE_INFORMATION_SERVICE   = "0000180a-0000-1000-8000-00805f9b34fb";
    public static String BATTERY_SERVICE              = "0000180f-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    // Access Service  -- Future use
    public static String SENSOR_ACCESS_SERVICE        = "00004143-5657-5353-2020-56454c564554";
    public static String SENSOR_ACCESS_TIME           = "00005554-5657-5353-2020-56454C564554";  // write
    public static String SENSOR_ACCESS_CONTROL        = "00004357-5657-5353-2020-56454C564554";  // read write notify
    public static String SENSOR_ACCESS_PASSKEY        = "0000504B-5657-5353-2020-56454C564554";  // read write

    // Control Service
    public static String SENSOR_CONTROL_SERVICE       = "56780000-5657-5353-2020-56454c564554";
    public static String SENSOR_CONTROL_NODE          = "5678546e-5657-5353-2020-56454c564554";  // read write
    public static String SENSOR_CONTROL_LOCK          = "5678546c-5657-5353-2020-56454c564554";  // WRITE
    public static String SENSOR_CONTROL_OPENED        = "5678546f-5657-5353-2020-56454c564554";  // read write  write id
    public static String SENSOR_CONTROL_CLOSED        = "56785463-5657-5353-2020-56454c564554";  // read write
    public static String SENSOR_CONTROL_WINDOW        = "56785477-5657-5353-2020-56454c564554";  // read write time period
    public static String SENSOR_CONTROL_IDENTIFY      = "56784964-5657-5353-2020-56454c564554";  // WRITE

    // Handling Service
    public static String SENSOR_HANDLING_SERVICE      = "48610000-5657-5353-2020-56454c564554";
    public static String SENSOR_HANDLING_VALUE        = "48614d76-5657-5353-2020-56454c564554";
    public static String SENSOR_HANDLING_LIMIT        = "48614c76-5657-5353-2020-56454c564554";

    // Telemetry Service
    public static String SENSOR_TELEMETRY_SERVICE     = "54650000-5657-5353-2020-56454c564554";
    public static String SENSOR_TELEMETRY_ARCHIVAL    = "54654169-5657-5353-2020-56454c564554";  // read write
    public static String SENSOR_TELEMETRY_INTERVAL    = "54654d69-5657-5353-2020-56454c564554";  // read write

    // Atmospheric Service
    public static String SENSOR_ATMOSPHERE_SERVICE    = "41740000-5657-5353-2020-56454c564554";
    public static String SENSOR_ATMOSPHERE_VALUE      = "41744c76-5657-5353-2020-56454c564554";   // read notify
    public static String SENSOR_ATMOSPHERE_LOWER      = "41744c6c-5657-5353-2020-56454c564554";   // read write
    public static String SENSOR_ATMOSPHERE_UPPER      = "4174556c-5657-5353-2020-56454c564554";   // read write

    // Surface Service
    public static String SENSOR_SURFACE_SERVICE       = "53740000-5657-5353-2020-56454c564554";
    public static String SENSOR_SURFACE_VALUE         = "53744d76-5657-5353-2020-56454c564554";   // read notify
    public static String SENSOR_SURFACE_LOWER         = "53744c6c-5657-5353-2020-56454c564554";   // read write
    public static String SENSOR_SURFACE_UPPER         = "5374556c-5657-5353-2020-56454c564554";   // read write

    // Records Service
   public static String SENSOR_RECORDS_SERVICE       = "54720000-5657-5353-2020-56454c564554";
 //   public static String SENSOR_RECORDS_INTERVAL      = "54725269-5657-5353-2020-56454C564554";
 //   public static String SENSOR_RECORDS_CURSOR        = "54725263-5657-5353-2020-56454C564554";
 //   public static String SENSOR_RECORDS_DATA          = "54725264-5657-5353-2020-56454C564554";

    public static String DEVICE_NAME                  = "00002a00-0000-1000-8000-00805f9b34fb";
    public static String MODEL_NUMBER                 = "00002a24-0000-1000-8000-00805f9b34fb";
    public static String SERIAL_NUMBER                = "00002a25-0000-1000-8000-00805f9b34fb";
    public static String FIRMWARE                     = "00002a26-0000-1000-8000-00805f9b34fb";
    public static String HARDWARE_REV                 = "00002a27-0000-1000-8000-00805f9b34fb";
    public static String MANUFACTURER_NAME            = "00002a29-0000-1000-8000-00805f9b34fb";

    public static String BATTERY_LEVEL                = "00002a19-0000-1000-8000-00805f9b34fb";
    public static String BATTERY_STATE                = "00002a1a-0000-1000-8000-00805f9b34fb";


    // Sensor access commands

    public static int kAccessRequestShutdown  = 0x504F5453;      // Request shutdown notice ('STOP')
    public static int kAccessRequestFactory   = 0x4F52455A;      // Request factory erase ('ZERO')
    public static int kAccessRequestReboot    = 0x544F4F42;      // Request reboot ('BOOT')
    public static int kAccessRequestLoader    = 0x4D414F4C;      // Request boot loader ('LOAD')
    public static int kAccessRequestErase     = 0x45504957;      // Request storage erase ('WIPE')

    public static int kAccessResponseAccepted = 0x454E4F44;      // Respond accepted ('DONE')
    public static int kAccessResponseRejected = 0x4C494146;      // Respond rejected ('FAIL')
    public static int kAccessResponseLocked   = 0x4B434F4C;      // Respond access locked ('LOCK')
    public static int kAccessResponseOpened   = 0x4E45504F;      // Respond access open ('OPEN')



    static {
        // Standard Services.
        attributes.put(GENERIC_ACCESS_SERVICE,     "Generic Access");
        attributes.put(GENERIC_ATTRIBUTE_SERVICE, "Generic Attribute");
        attributes.put(DEVICE_INFORMATION_SERVICE, "Device Information Service");
        attributes.put(BATTERY_SERVICE, "Battery Service");


        // Standard Characteristics.
        attributes.put(DEVICE_NAME, "Device Name");
        attributes.put("00002a01-0000-1000-8000-00805f9b34fb", "Appearance");
        attributes.put("00002a04-0000-1000-8000-00805f9b34fb", "Preferred Connections");
        attributes.put("00002a05-0000-1000-8000-00805f9b34fb", "Service Changed");
        attributes.put("00002aa6-0000-1000-8000-00805f9b34fb", "Address Resolution");

        attributes.put(MODEL_NUMBER, "Model Number");
        attributes.put(SERIAL_NUMBER, "Serial Number");
        attributes.put(HARDWARE_REV, "Hardware Rev");
        attributes.put(MANUFACTURER_NAME, "Manufacturer Name");
        attributes.put(FIRMWARE, "Firmware");

        attributes.put(BATTERY_LEVEL, "Battery Level");
        attributes.put(BATTERY_STATE, "Battery State");

        // Stickershock Services
        attributes.put(SENSOR_ACCESS_SERVICE,    "Sensor Access Service");
        attributes.put(SENSOR_CONTROL_SERVICE,   "Sensor Control Service");
        attributes.put(SENSOR_HANDLING_SERVICE,  "Sensor Handling Service");
        attributes.put(SENSOR_RECORDS_SERVICE,   "Sensor Records Service");
        attributes.put(SENSOR_TELEMETRY_SERVICE, "Sensor Telemetry Service");
        attributes.put(SENSOR_SURFACE_SERVICE,   "Sensor Surface Service");
        attributes.put(SENSOR_ATMOSPHERE_SERVICE, "Sensor Atmosphere Service");

        // Stickershock Characteristics=
        attributes.put(SENSOR_CONTROL_IDENTIFY,   "Sensor Control Identify");
        attributes.put(SENSOR_SURFACE_VALUE,      "Sensor Surface Value");
        attributes.put(SENSOR_ATMOSPHERE_VALUE,   "Sensor Atmosphere Value");
        attributes.put(SENSOR_TELEMETRY_ARCHIVAL, "Sensor Telemetry Archival");
        attributes.put(SENSOR_TELEMETRY_INTERVAL, "Sensor Telemetry Interval");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}

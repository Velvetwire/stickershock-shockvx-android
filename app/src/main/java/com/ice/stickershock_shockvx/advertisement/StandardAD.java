package com.ice.stickershock_shockvx.advertisement;

public class StandardAD {

    byte  size;          // record size
    byte  type;
    public int   surface;       // ambient temperature in 1/100 degrees C
    public int   ambient;       // ambient temperature in 1/100 degrees C
    public int   humidity;      // humidity in 1/100 percent
    public int   pressure;      // pressure in mbar
    public int   alternate;     // alternate temperature in 1/100 degree C
    public int   voltage;       // battery voltage in mV
    public short angle;         // orientation and tilt angle
    public byte  drops;        // number of drops
    public byte  bumps;        // number of bumps

    public void StandardAd() {

    }

    public void populateFromByteArray(byte[] incoming) {

        this.surface      = (((incoming[1] & 0xff) << 8) | incoming[0] & 0xff);
        this.ambient      = (((incoming[3] & 0xff) << 8) | incoming[2] & 0xff);
        this.humidity     = (((incoming[5] & 0xff) << 8) | incoming[3] & 0xff);
        this.pressure     = (((incoming[17] & 0xff) << 8) | incoming[16] & 0xff);
        this.alternate    = (((incoming[19] & 0xff) << 8) | incoming[18] & 0xff);
        this.voltage      = (((incoming[21] & 0xff) << 8) | incoming[20] & 0xff);
        this.angle        = (short)(((incoming[23] & 0xff) << 8) | incoming[22] & 0xff);

    }
}



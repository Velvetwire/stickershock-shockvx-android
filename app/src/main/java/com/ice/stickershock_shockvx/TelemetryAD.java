package com.ice.stickershock_shockvx;

import java.nio.ByteBuffer;

public class TelemetryAD {
    byte  packetlen;
    byte  flag;
    int manufacturer;
    byte  requestType;
    byte  smLength;
    int   timecode;      // 4 bytes
    int   surface;       // surface temperature in 1/100 degrees C
    int   ambient;       // ambient temperature in 1/100 degrees C
    int   humidity;      // humidity in 1/100 percent
    int   pressure;      // pressure in mbar
    int   alternate;     // alternate temperature in 1/100 degree C
    int   voltage;       // battery voltage in mV
    short angle;         // orientation and tilt angle
    byte  drops;        // number of drops
    byte  bumps;        // number of bumps

    public void TelemetryAd() {

    }

    public void populateFromByteArray(byte[] incoming) {

        this.packetlen    = incoming[0];
        this.flag         = incoming[1];
        this.manufacturer = (((incoming[3] & 0xff) << 8) | incoming[2] & 0xff);
        this.requestType  = incoming[4];
        this.smLength     = incoming[5];
        this.timecode     = ((incoming[9] << 24) & 0xff) | ((incoming[8] << 16) & 0xff) | ((incoming[7] << 8) & 0xff) | incoming[6] & 0xff;

        this.surface      = (((incoming[11] & 0xff) << 8) | incoming[10] & 0xff);
        this.ambient      = (((incoming[13] & 0xff) << 8) | incoming[12] & 0xff);
        this.humidity     = (((incoming[15] & 0xff) << 8) | incoming[14] & 0xff);
        this.pressure     = (((incoming[17] & 0xff) << 8) | incoming[16] & 0xff);
        this.alternate    = (((incoming[19] & 0xff) << 8) | incoming[18] & 0xff);
        this.voltage      = (((incoming[21] & 0xff) << 8) | incoming[20] & 0xff);
        this.angle        = (short)(((incoming[23] & 0xff) << 8) | incoming[22] & 0xff);
        this.drops        = incoming[24];
        this.bumps        = incoming[25];
    }
}



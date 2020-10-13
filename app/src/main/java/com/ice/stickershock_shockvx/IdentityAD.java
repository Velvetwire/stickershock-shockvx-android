package com.ice.stickershock_shockvx;

public class IdentityAD {
    byte  packetlen;
    byte  flag;
    int manufacturer;
    byte  requestType;
    byte  smLength;
    int timecode;        // 4 bytes
    byte[] identity = new byte[8];     // identity
    byte[] validity = new byte[8];    // surface temperature in 1/100 degrees C
    byte battery;        // number of drops
    byte signal;        // number of bumps

    public void IdentityAd() {

    }

    public void populateFromByteArray(byte[] incoming) {
        this.packetlen    = incoming[0];
        this.flag         = incoming[1];
        this.manufacturer = (((incoming[3] & 0xff) << 8) | incoming[2] & 0xff);
        this.requestType  = incoming[4];
        this.smLength     = incoming[5];
        this.timecode     = ((incoming[9] << 24) & 0xff) | ((incoming[8] << 16) & 0xff) | ((incoming[7] << 8) & 0xff) | incoming[6] & 0xff;
        this.identity[0]  = incoming[10];
        this.identity[1]  = incoming[11];
        this.identity[2]  = incoming[12];
        this.identity[3]  = incoming[13];
        this.identity[4]  = incoming[14];
        this.identity[5]  = incoming[15];
        this.identity[6]  = incoming[16];
        this.identity[7]  = incoming[17];

        this.validity[0]  = incoming[18];
        this.validity[1]  = incoming[19];
        this.validity[2]  = incoming[20];
        this.validity[3]  = incoming[21];
        this.validity[4]  = incoming[22];
        this.validity[5]  = incoming[23];
        this.validity[6]  = incoming[24];
        this.validity[7]  = incoming[25];

        this.battery      = incoming[26];
        this.signal       = incoming[27];
    }
}

package com.ice.stickershock_shockvx.advertisement;

public class IdentityAD {
    int timecode;        // 4 bytes
    byte[] identity = new byte[8];     // identity

    byte signal;                      // rssi
    byte battery;                     // battery

    public void IdentityAd() {

    }

    public void processIdentity(byte[] incoming) {
        this.timecode     = ((incoming[5] << 24) & 0xff) | ((incoming[4] << 16) & 0xff) | ((incoming[3] << 8) & 0xff) | incoming[2] & 0xff;
        this.identity[0]  = incoming[6];
        this.identity[1]  = incoming[7];
        this.identity[2]  = incoming[8];
        this.identity[3]  = incoming[9];
        this.identity[4]  = incoming[10];
        this.identity[5]  = incoming[11];
        this.identity[6]  = incoming[12];
        this.identity[7]  = incoming[13];

        this.battery      = incoming[14];
        this.signal       = incoming[15];
    }
}

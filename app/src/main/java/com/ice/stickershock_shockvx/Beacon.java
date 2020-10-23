package com.ice.stickershock_shockvx;


import com.ice.stickershock_shockvx.advertisement.IdentityAD;
import com.ice.stickershock_shockvx.advertisement.StandardAD;

public class Beacon {
    public String address;
    public int rssi;
    public int txPowerLevel;
    public StandardAD telemetry;
    public IdentityAD identity;


    public Beacon() {
        // Auto-generated constructor
        this.address= null;
        this.rssi = 0;
        this.txPowerLevel = 100;
        this.telemetry = new StandardAD();
        this.identity = new IdentityAD();

    }

    public Beacon(String s1, StandardAD s2, IdentityAD s3) {
        // Auto-generated constructor
        this.address= s1;
        this.telemetry = s2;
        this.identity = s3;

    }

    // for reference here is what is received from Beacon
    // first byte is length of following record
    //    02 01 06
    //    1B FF 57 56 49 16 00 00 00 00 7D 6C 09 AA 56 eb 0D 25 00 00 00 00 00 00 00 00 64 c9
    //    02 0A 00
    //    19 FF 57 56 54 14 00 00 00 00 41 0A AA 0C 1C 02 f2 03 63 0c 5e 10 21 fc 00 00
    //    00 00

}

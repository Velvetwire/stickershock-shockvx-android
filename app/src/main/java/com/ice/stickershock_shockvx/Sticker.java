package com.ice.stickershock_shockvx;


import android.util.Log;

import com.ice.stickershock_shockvx.advertisement.IdentityAD;
import com.ice.stickershock_shockvx.advertisement.StandardAD;

public class Sticker {
    public String     name;
    public String     address;
    public String     annotation;
    public StandardAD telemetry;
    public IdentityAD identity;

    public int    batteryLevel;
    public String batteryState;

    public int rssi;
    public int txPowerLevel;

    // telemetry data
    int   surface;       // surface temperature in 1/100 degrees C
    int   ambient;       // ambient temperature in 1/100 degrees C
    int   humidity;      // humidity in 1/100 percent
    int   pressure;      // pressure in mbar

    // alarm and limits
    int   surfaceAlarmLo;
    int   surfaceAlarmHi;
    int   ambientAlarmLo;
    int   ambientAlarmHi;
    int   humidityAlarmLo;
    int   humidityAlarmHi;
    int   pressureAlarmLo;
    int   pressureAlarmHi;

    // handling
    int   orientation;   // orientation
    int   angle;         // pressure in mbar

    public String make;
    public String model;
    public String firmware;
    public String hardware;
    public String serial;

    public int     sticker_state;           //



    public Sticker() {
        this.name = null;
        this.address = null;

        this.batteryLevel = 100;
        this.batteryState = null;
        this.rssi         = 0;


    }

    Sticker(String s1, int i2, int i3, int i4, int i5, String s2) {
        this.name = s1;
        this.surface = i2;
        this.ambient = i3;
        this.humidity = i4;
        this.pressure = i5;
        this.address  = s2;
    }

    Sticker(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {
        this.name = s1;
        this.make = s3;
        this.model = s4;
        this.serial = s5;
        this.hardware = s6;
        this.firmware = s7;
        this.address = s8;
    }

    public void setAddress(String s1) {
        this.address = s1;
    }

    public boolean isComplete() {
        if ((make !=null) && (model !=null) && ( serial !=null) && (hardware !=null)) {
            return true;
        } else
            return false;
     }

    public void clear() {
        this.name = null;
        this.make = null;
        this.model = null;
        this.serial = null;
        this.hardware = null;
        this.firmware = null;
        this.address = null;
    }

   /* isNotifyComplete
    *
    *  returns true when all 4 sensors have their characteristic notifys enabled
    */



}

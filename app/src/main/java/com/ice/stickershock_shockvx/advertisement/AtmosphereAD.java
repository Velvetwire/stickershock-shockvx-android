package com.ice.stickershock_shockvx.advertisement;

public class AtmosphereAD {

    byte  size;          // record size
    byte  type;
    public Measurement  temperature;       // ambient temperature in 1/100 degrees C
    public Measurement  humidity;       // ambient temperature in 1/100 degrees C
    public Measurement  pressure;       // ambient temperature in 1/100 degrees C


    private class Measurement {
        char  measurement;       // ambient temperature in 1/100 degrees C
        char  incursion;       // ambient temperature in 1/100 degrees C
        char  excursion;      // humidity in 1/100 percent
    }

    public void StandardAd() {

    }

    public void populateFromByteArray(byte[] incoming) {

        this.size         = incoming[0];
        this.type         = incoming[1];
        this.temperature.measurement = (char) (((incoming[3] & 0xff) << 8) | incoming[2] & 0xff);
        this.temperature.incursion   = (char) (((incoming[5] & 0xff) << 8) | incoming[4] & 0xff);
        this.temperature.excursion   = (char) (((incoming[7] & 0xff) << 8) | incoming[6] & 0xff);
        this.humidity.measurement    = (char) (((incoming[9] & 0xff) << 8) | incoming[8] & 0xff);
        this.humidity.incursion      = (char) (((incoming[11] & 0xff) << 8) | incoming[10] & 0xff);
        this.humidity.excursion      = (char) (((incoming[13] & 0xff) << 8) | incoming[12] & 0xff);
        this.pressure.measurement    = (char) (((incoming[15] & 0xff) << 8) | incoming[14] & 0xff);
        this.pressure.incursion      = (char) (((incoming[17] & 0xff) << 8) | incoming[16] & 0xff);
        this.pressure.excursion      = (char) (((incoming[19] & 0xff) << 8) | incoming[18] & 0xff);

    }
}



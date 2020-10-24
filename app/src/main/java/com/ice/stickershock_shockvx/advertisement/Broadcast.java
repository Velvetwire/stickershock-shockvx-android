package com.ice.stickershock_shockvx.advertisement;

public class Broadcast {
    public final static byte BROADCAST_TYPE_IDENTITY           = 0x01;
    public final static byte BROADCAST_TYPE_VARIANT            = 0x07;
    public final static byte BROADCAST_TYPE_TEMPERATURE        = 0x21;
    public final static byte BROADCAST_TYPE_ATMOSPHERE         = 0x23;
    public final static byte BROADCAST_TYPE_HANDLING           = 0x31;


    public final static byte BROADCAST_ORIENTATION_FACE  = (byte) (1 << 7);       //  Orientation alert flag
    public final static byte BROADCAST_ORIENTATION_DROP  = (byte) (1 << 6);        //  Drop alert flag
    public final static byte BROADCAST_ORIENTATION_BUMP  = (byte) (1 << 5);        //  Bump alert flag
    public final static byte BROADCAST_ORIENTATION_TILT  = (byte) (1 << 4);        //  Tilt alert flag
    public final static byte BROADCAST_ORIENTATION_ANGLE = (byte) (1 << 3);       //  Valid angle flag




}

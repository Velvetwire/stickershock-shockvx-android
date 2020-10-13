/*
 * ReadSensorView.java
 *    Main routine for handling sensor data once BLE connection is made
 *    ReadSensorView handles the Tab interface and the individual
 *    fragments that each tab navigates to
 *       1. Telemetry Fragment
 *       2. DatalogFragment
 *       3. SettingsFragment
 *       4. Trends Fragment
 *       5. File Fragment
 */
package com.ice.stickershock_shockvx;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;



public class SensorState  {


    // in seconds
    public static int captureRate = 15;
    public static int sampleRate = 1;
    public static int ratio      = 1;

    public static Sticker currentSticker = new Sticker();



}










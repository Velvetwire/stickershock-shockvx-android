//=============================================================================
// project: ShockVx
//  module: Stickershock Android App for cold chain tracking.
//  author: Velvetwire, llc
//    file: TabbedActivity.java
//
//   Main routine for newly tagged sticker
//   TabbedActivity handles the Tab interface and the individual
//   fragments that each tab navigates to
//       1. Tracking Fragment
//       2. Telemetry Fragment
//       3. SettingsFragment
//
// (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
//=============================================================================

package com.ice.stickershock_shockvx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import static com.ice.stickershock_shockvx.bluetooth.Actions.*;
import static com.ice.stickershock_shockvx.Constants.*;


public class TabbedActivity extends AppCompatActivity {
    private static final int NUM_PAGES = 3;
    public static ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    String mDeviceUnit;
    int mStickerState;

    int[] tabIcons = {
            R.drawable.tracking,
            R.drawable.telemetry,
            R.drawable.settings
    };

    private String[] tabTitles = new String[]{"Tracking", "Telemetry", "Settings"};
    // tab titles


    private String stickerName = "test";
    public static Sticker currentSticker = new Sticker();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        final Intent intent = getIntent();
        mDeviceUnit    = intent.getStringExtra( EXTRAS_DEVICE_UNIT );
        mStickerState    = intent.getIntExtra( EXTRAS_STICKER_STATE, 0 );
        setContentView( R.layout.frag_activity_main );
        viewPager = findViewById( R.id.mypager );

        pagerAdapter = new MyPagerAdapter(this );
        viewPager.setAdapter( pagerAdapter );

        //inflating tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        //displaying tabs
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab( @NonNull TabLayout.Tab tab, int position ) {
                tab.setText( tabTitles[position] );
                tab.setIcon( tabIcons[position] );
            }
        }).attach();

    }



    private class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }


        @Override
        public Fragment createFragment(int pos) {
            switch (pos) {
                case 0: {
                    if ( mStickerState == STICKER_NEW )
                       return TrackAssetFragment.newInstance("");
                    if ( mStickerState == STICKER_OPEN )
                        return AcceptAssetFragment.newInstance("");
                }
                case 1: {
                    return TelemetryFragment.newInstance("");
                }
                case 2: {
                    return SettingsFragment.newInstance("t 3");

                }
                default:
                    return TelemetryFragment.newInstance("");
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
        transmitBroadcast( ACTION_DISCONNECT );
        Intent intent = new Intent(TabbedActivity.this, MainAssetScreen.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    private void transmitBroadcast(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if ( RESPONSE_MANUFACTURER_AVAILABLE.equals(action)) {
                String extraData = intent.getStringExtra ( STRING_DATA );
                currentSticker.make = extraData;
            }
            else if ( RESPONSE_MODEL_AVAILABLE .equals(action)) {
                String extraData = intent.getStringExtra ( STRING_DATA );
                currentSticker.model = extraData;
            }
            else if ( RESPONSE_FIRMWARE_AVAILABLE .equals(action)) {
                String extraData = intent.getStringExtra ( STRING_DATA );
                currentSticker.firmware = extraData;
            }
            else if ( RESPONSE_HARDWARE_AVAILABLE .equals(action)) {
                String extraData = intent.getStringExtra ( STRING_DATA );
                currentSticker.hardware = extraData;;
            }
            else if ( RESPONSE_SERIAL_AVAILABLE .equals(action)) {
                String extraData = intent.getStringExtra ( STRING_DATA );
                currentSticker.serial = extraData;;
            }

        }
    };


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction( ACTION_DATA_AVAILABLE);
        intentFilter.addAction( RESPONSE_READ_DATA_AVAILABLE);
        intentFilter.addAction( RESPONSE_WRITE_DATA_AVAILABLE);
        intentFilter.addAction( ACTION_SENSOR_DATA_AVAILABLE );
        intentFilter.addAction( RESPONSE_MANUFACTURER_AVAILABLE );
        intentFilter.addAction( RESPONSE_MODEL_AVAILABLE );
        intentFilter.addAction( RESPONSE_FIRMWARE_AVAILABLE );
        intentFilter.addAction( RESPONSE_HARDWARE_AVAILABLE );
        intentFilter.addAction( RESPONSE_SERIAL_AVAILABLE );
        return intentFilter;
    }


}










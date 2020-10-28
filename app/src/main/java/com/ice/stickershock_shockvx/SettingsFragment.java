package com.ice.stickershock_shockvx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


import static com.ice.stickershock_shockvx.bluetooth.Actions.*;


public class SettingsFragment extends Fragment {

    Button mBackAssets;
    Button mMeas15Button;
    Button mMeas60Button;
    Button mRec15Button;
    Button mRec60Button;
    Switch mSurfSwitch;
    TextView mSurfMinVal;
    Button mSurfMinMinusButton;
    Button mSurfMinPlusButton;

    TextView mSurfMaxVal;
    Button mSurfMaxMinusButton;
    Button mSurfMaxPlusButton;
    Switch mAmbientSwitch;
    TextView mAmbientMinVal;
    Button mAmbientMinMinusButton;
    Button mAmbientMinPlusButton;

    TextView mAmbientMaxVal;
    Button mAmbientMaxMinusButton;
    Button mAmbientMaxPlusButton;

    Button mNone;
    Button mCareful;
    Button mFragile;

    Button mFlat;
    Button mUpright;
    Switch mTipSwitch;
    TextView mAngleVal;
    Button mAngleMin;
    Button mAngleMax;

    Button mIndicate;;

    int mSurfaceAlarmMin = 20;
    int mSurfaceAlarmMax = 20;
    int mAmbientAlarmMin = 20;
    int mAngle           = 0;

    public static Sticker currentSticker = new Sticker();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        mMeas15Button          = v.findViewById( R.id.meas15button );
        mMeas60Button          = v.findViewById( R.id.meas60button );
        mRec15Button           = v.findViewById( R.id.rec15button );
        mRec60Button           = v.findViewById( R.id.rec60button );
        mSurfSwitch            = v.findViewById( R.id.surfSwitch);
        mSurfMinVal            = v.findViewById( R.id.surfminVal);
        mSurfMinMinusButton    = v.findViewById( R.id.surfminminus );
        mSurfMinPlusButton     = v.findViewById( R.id.surfminplus );

        mSurfMaxVal            = v.findViewById( R.id.surfmaxVal);
        mSurfMaxMinusButton    = v.findViewById( R.id.surfmaxminus );
        mSurfMaxPlusButton     = v.findViewById( R.id.surfmaxplus );

        mAmbientSwitch         = v.findViewById( R.id.ambientSwitch);
        mAmbientMinVal         = v.findViewById( R.id.ambminVal);
        mAmbientMinMinusButton = v.findViewById( R.id.ambminminus );
        mAmbientMinPlusButton  = v.findViewById( R.id.ambminplus );

        mAmbientMaxVal         = v.findViewById( R.id.ambmaxVal);
        mAmbientMaxMinusButton = v.findViewById( R.id.ambmaxminus );
        mAmbientMaxPlusButton  = v.findViewById( R.id.ambmaxplus );


        mNone      = v.findViewById( R.id.noneButton );
        mCareful   = v.findViewById( R.id.carefulButton );
        mFragile   = v.findViewById( R.id.fragileButton );

        mFlat      = v.findViewById( R.id.flatButton );
        mUpright   = v.findViewById( R.id.uprightButton );
        mTipSwitch = v.findViewById( R.id.tipSwitch );
        mAngleVal  = v.findViewById( R.id.angleVal );
        mAngleMin  = v.findViewById( R.id.angleminButton );
        mAngleMax  = v.findViewById( R.id.anglemaxButton );


        mBackAssets = v.findViewById( R.id.assetButton );
        mBackAssets.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainAssetScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


// -----------------------------------------------------------------------
//      MEASUREMENT Handlers
// -----------------------------------------------------------------------
        mMeas15Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                transmitBroadcast( ACTION_MEASUREMENT_INTERVAL_15 );
            }
        });

        mMeas60Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                transmitBroadcast( ACTION_MEASUREMENT_INTERVAL_60 );
            }
        });

        //   Records
        mRec15Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                transmitBroadcast( ACTION_RECORDS_INTERVAL_15 );
            }
        });
        mRec60Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                transmitBroadcast( ACTION_RECORDS_INTERVAL_60 );
            }
        });

//      SURFACE SETTINGS
// -----------------------------------------------------------------------
        mSurfSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()  {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if ( isChecked ) {
                   ;
               } else {
                   ;
               }
            }
        });
        //   Surface Temp Alarm
        mSurfMinMinusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // subtract 5 degrees from min alarm and set
            }
        });

        mSurfMinPlusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // add 5 degrees to min alarm and set
            }
        });
        mSurfMaxMinusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // subtract 5 degrees from max alarm and set
            }
        });

        mSurfMaxPlusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // add 5 degrees to max alarm and set
            }
        });

 // -----------------------------------------------------------------------
//      AMBIENT SETTINGS
// -----------------------------------------------------------------------
        mAmbientSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()  {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked ) {
                    ;
                } else {
                    ;
                }
            }
    });

        //   Ambient Temp Alarm
        mAmbientMinMinusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // subtract 5 degrees from min alarm and set
            }
        });

        mAmbientMinPlusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // add 5 degrees to min alarm and set
            }
        });
        mAmbientMaxMinusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // subtract 5 degrees from max alarm and set

            }
        });
        mAmbientMaxPlusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // add 5 degrees to max alarm and set
            }
        });

// -----------------------------------------------------------------------
// Handling Settings
// -----------------------------------------------------------------------
        mNone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                transmitBroadcast( ACTION_HANDLING_NONE );
            }
        });
        mCareful.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                transmitBroadcast( ACTION_HANDLING_CAREFUL );
            }
        });
        mFragile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                transmitBroadcast( ACTION_HANDLING_FRAGILE );
            }
        });

        mTipSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()  {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked ) {
                    ;
                } else {
                    ;
                }
            }
        });
        //   Tipping row
        mFlat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                transmitBroadcast( ACTION_ORIENTATION_FLAT );
            }
        });
        mUpright.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                transmitBroadcast( ACTION_ORIENTATION_UPRIGHT );
            }
        });

        mAngleMin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // subtract 5 degrees from angle alarm and set
            }
        });
        mAngleMax.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // add 5 degrees to angle alarm and set
            }
        });

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver( mGattUpdateReceiver, makeGattUpdateIntentFilter());
        readRssi();
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver( mGattUpdateReceiver );
    }

    public static SettingsFragment newInstance(String text) {

        SettingsFragment f = new SettingsFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    public void getBatteryLevel() {
        transmitBroadcast( ACTION_BATTERY_LEVEL );
    }

    public void getBatteryState() {
        transmitBroadcast( ACTION_BATTERY_STATE );
    }


    public void setRecord(int interval) {
    }


    public void requestLed()  {
        transmitBroadcast( ACTION_LED_INCOMING );
    }

    private void transmitBroadcast(final String action) {
        final Intent intent = new Intent(action);
        getActivity().sendBroadcast(intent);
    }



    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

         if ( RESPONSE_BATTERY_LEVEL.equals(action) ) {

                int intData = intent.getIntExtra( INT_DATA, 0);
 //               mBattery.setText(String.valueOf(intData + "%"));
            }
         if ( RESPONSE_RSSI_DATA.equals(action) ) {
                int rssiData = intent.getIntExtra( INT_DATA,0);
//                mRssi.setText(String.valueOf(rssiData + " dB"));
 //               readBattery();
            }

        }
    };

    public void readRssi() {
        transmitBroadcast( ACTION_READ_RSSI );
    }

    public void readBattery() {
        transmitBroadcast( ACTION_BATTERY_LEVEL );
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction( RESPONSE_RSSI_DATA );
        intentFilter.addAction( RESPONSE_BATTERY_LEVEL );

        return intentFilter;
    }


    public void getCaptureRatio()  {
        int c = SensorState.captureRate;
        int s = SensorState.sampleRate;
        SensorState.ratio = c / s;
        Log.d("DATALOG" , "RATIO " + SensorState.ratio);

    }
}


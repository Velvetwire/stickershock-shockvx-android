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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ice.stickershock_shockvx.bluetooth.BluetoothLeService;

import static com.ice.stickershock_shockvx.bluetooth.Actions.*;


public class SettingsFragment extends Fragment {
    private BluetoothLeService mBluetoothLeService;
    EditText mName;
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


    Button mNone ;
    Button mCareful;
    Button mFragile;

    Button mFlat;
    Button mUpright;
    Switch mTipSwitch;
    TextView mAngleVal;
    Button mAngleMin;
    Button mAngleMax;

    Button mIndicate;;

    public static Sticker currentSticker = new Sticker();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings, container, false);
        mMeas15Button = v.findViewById( R.id.meas15button );
        mMeas60Button = v.findViewById( R.id.meas60button );
        mRec15Button  = v.findViewById( R.id.rec15button );
        mRec60Button   = v.findViewById( R.id.rec60button );
        mSurfSwitch      = v.findViewById( R.id.surfSwitch);
        mSurfMinVal          = v.findViewById( R.id.surfminVal);
        mSurfMinMinusButton  = v.findViewById( R.id.surfminminus );
        mSurfMinPlusButton   = v.findViewById( R.id.surfminplus );

        mSurfMaxVal    = v.findViewById( R.id.surfmaxVal);
        mSurfMaxMinusButton   = v.findViewById( R.id.surfmaxminus );
        mSurfMaxPlusButton   = v.findViewById( R.id.surfmaxplus );

        mAmbientSwitch      = v.findViewById( R.id.ambientSwitch);
        mAmbientMinVal    = v.findViewById( R.id.ambminVal);
        mAmbientMinMinusButton   = v.findViewById( R.id.ambminminus );
        mAmbientMinPlusButton   = v.findViewById( R.id.ambminplus );

        mAmbientMaxVal    = v.findViewById( R.id.ambmaxVal);
        mAmbientMaxMinusButton   = v.findViewById( R.id.ambmaxminus );
        mAmbientMaxPlusButton   = v.findViewById( R.id.ambmaxplus );


        mNone = v.findViewById( R.id.noneButton );
        mCareful = v.findViewById( R.id.carefulButton );
        mFragile = v.findViewById( R.id.fragileButton );

        mFlat = v.findViewById( R.id.flatButton );
        mUpright = v.findViewById( R.id.uprightButton );
        mTipSwitch = v.findViewById( R.id.tipSwitch );
        mAngleVal = v.findViewById( R.id.angleVal );
        mAngleMin = v.findViewById( R.id.angleminButton );
        mAngleMax = v.findViewById( R.id.anglemaxButton );

        // Handlers
        //   Measurement row
        mMeas15Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set measurement button to ic
            //    mMeas60Button.setChecked(false);
            }
        });
        mMeas60Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set measurement button to ic
          //      mMeas15Button.setChecked(false);
            }
        });

        //   Records
        mRec15Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set records button to ic
                //    mMeas60Button.setChecked(false);
            }
        });
        mRec60Button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set measurement button to ic
                //      mMeas15Button.setChecked(false);
            }
        });

        //   Surface Temp Alarm
        mSurfMinMinusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setInterval( 15 );
            }
        });
        mSurfMinPlusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SensorState.captureRate = 15;
          //      getCaptureRatio();
           //     mCapture1m.setChecked(false);
           //     mCapture5m.setChecked(false);
            }
        });
        mSurfMaxMinusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SensorState.captureRate = 60;

            }
        });
        mSurfMaxPlusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SensorState.captureRate = 300;
                getCaptureRatio();

            }
        });

        //   Ambient Temp Alarm
        mAmbientMinMinusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setInterval( 15 );
            }
        });
        mAmbientMinPlusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SensorState.captureRate = 15;
                //      getCaptureRatio();
                //     mCapture1m.setChecked(false);
                //     mCapture5m.setChecked(false);
            }
        });
        mAmbientMaxMinusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SensorState.captureRate = 60;

            }
        });
        mAmbientMaxPlusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SensorState.captureRate = 300;
                getCaptureRatio();

            }
        });

        //   Handling row
        mNone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set measurement button to ic
                //    mMeas60Button.setChecked(false);
            }
        });
        mCareful.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set measurement button to ic
                //      mMeas15Button.setChecked(false);
            }
        });
        mFragile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set measurement button to ic
                //      mMeas15Button.setChecked(false);
            }
        });

        //   Tipping row
        mFlat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set measurement button to ic
                //    mMeas60Button.setChecked(false);
            }
        });
        mUpright.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set measurement button to ic
                //      mMeas15Button.setChecked(false);
            }
        });

        mAngleMin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set measurement button to ic
                //    mMeas60Button.setChecked(false);
            }
        });
        mAngleMax.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // set measurement button to ic
                //      mMeas15Button.setChecked(false);
            }
        });
/*
//        mIndicate.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                requestLed();
//            }
//        });

        mName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = v.getText().toString();
                    SensorState.currentSticker.name = text;
                    return true;
                }
                return false;
            }
        });
*/
//        readRssi();

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

    public void setInterval(int interval) {
        String action = "";
        action = ACTION_SET_INTERVAL;
        transmitBroadcast(action);
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

         if ( ACTION_BATTERY_LEVEL_AVAILABLE.equals(action) ) {

                int intData = intent.getIntExtra( INT_DATA, 0);
 //               mBattery.setText(String.valueOf(intData + "%"));
            }
         if ( ACTION_RSSI_DATA_AVAILABLE.equals(action) ) {
                int rssiData = intent.getIntExtra( INT_DATA,0);
//                mRssi.setText(String.valueOf(rssiData + " dB"));
                readBattery();
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

        intentFilter.addAction( ACTION_RSSI_DATA_AVAILABLE );
        intentFilter.addAction( ACTION_BATTERY_LEVEL_AVAILABLE );

        return intentFilter;
    }


    public void getCaptureRatio()  {
        int c = SensorState.captureRate;
        int s = SensorState.sampleRate;
        SensorState.ratio = c / s;
        Log.d("DATALOG" , "RATIO " + SensorState.ratio);


    }
}


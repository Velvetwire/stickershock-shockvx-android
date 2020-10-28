package com.ice.stickershock_shockvx;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ice.stickershock_shockvx.bluetooth.BluetoothLeService;

import static com.ice.stickershock_shockvx.bluetooth.Actions.*;
import static com.ice.stickershock_shockvx.bluetooth.BluetoothLeService.*;

public class TrackAssetFragment extends Fragment {
    Button mTrackButton;
    Button mBackAssets;

    EditText mName, mLocation;
    TextView mStickerId;

    int stickerStatus = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.track_asset, container, false);
        super.onCreate(savedInstanceState);

        mTrackButton = (Button)   v.findViewById(R.id.trackButton );
        mName        = (EditText) v.findViewById(R.id.assetName );
        mLocation    = (EditText) v.findViewById(R.id.assetLocation );
        mStickerId   = (TextView) v.findViewById(R.id.stickerid );

        setInterval ( );

        mBackAssets = v.findViewById( R.id.assetButton );
        mBackAssets.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainAssetScreen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        mTrackButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // createId();
                        Log.d("TRACK", "CREATE NEW STICKER RECORD");
                        openSticker();
                    }
                }
        );

        mName.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.d("TRACK", "Save Text");
            }
        });

        mLocation.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.d("TRACK", "Save Text");
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver( mGattUpdateReceiver, makeGattUpdateIntentFilter() );
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver( mGattUpdateReceiver );
    }

    public static TrackAssetFragment newInstance(String text) {

        TrackAssetFragment f = new TrackAssetFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    private void setInterval() {
        final Intent intent = new Intent( ACTION_SET_INTERVAL );
        getActivity().sendBroadcast(intent);
    }
    private void openSticker() {
        final Intent intent = new Intent( ACTION_OPEN_STICKER );
        getActivity().sendBroadcast(intent);
    }

    private void setUtcTime () {
        final Intent intent = new Intent( ACTION_SET_UTC_TIME );
        getActivity().sendBroadcast(intent);
    }

    private void saveStickerInfo() {
        // send broadcast command to open sticker with id
        return;
    }

    private void disconnectSticker () {
        final Intent intent = new Intent( ACTION_DISCONNECT );
        getActivity().sendBroadcast(intent);
    }




// -----------------------------------------
    // Broadcast receiver and message passing

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if ( RESPONSE_SET_INTERVAL.equals(action)) {
                Log.d("TRACK", "NEW SET INTERVAL");
            }
            if ( RESPONSE_STICKER_NEW.equals(action)) {
                Log.d("TRACK", "NEW STICKER DATA");
                setUtcTime();
            }
            if ( RESPONSE_SET_UTC.equals(action)) {
                Log.d("TRACK", "NEW STICKER DATA");
                openSticker();
            }
            if ( RESPONSE_STICKER_OPENED.equals(action)) {
                Log.d("TRACK", "NEW STICKER OPENED");
                disconnectSticker();
            }
            if ( ACTION_GATT_DISCONNECTED.equals(action)) {
                Intent i = new Intent(getActivity(), MainAssetScreen.class);
                startActivity ( i );
            }
        }
    };

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction( ACTION_SET_INTERVAL );
        intentFilter.addAction( RESPONSE_SET_INTERVAL );
        intentFilter.addAction( RESPONSE_SET_UTC );
        intentFilter.addAction( RESPONSE_STICKER_OPENED );
        intentFilter.addAction( RESPONSE_STICKER_NEW );
        intentFilter.addAction( ACTION_GATT_DISCONNECTED );

        return intentFilter;
    }
}
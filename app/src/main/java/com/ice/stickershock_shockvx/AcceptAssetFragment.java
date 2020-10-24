package com.ice.stickershock_shockvx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.ice.stickershock_shockvx.bluetooth.Actions.*;

public class AcceptAssetFragment extends Fragment {
    Button mAcceptButton;
    EditText mName, mLocation;
    TextView mStickerId;
    // constants



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.accept_asset, container, false);
        super.onCreate(savedInstanceState);

        mAcceptButton = (Button)   v.findViewById(R.id.acceptButton);
        mName        = (EditText) v.findViewById(R.id.assetName);
        mLocation    = (EditText) v.findViewById(R.id.assetLocation);
        mStickerId   = (TextView) v.findViewById(R.id.stickerid);


        mAcceptButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // createId();
                        Log.d("TRACK", "ACCEPT STICKER RECORD AND CLOSE");
                        closeSticker();
                    }
                }
        );
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(mGattUpdateReceiver);
    }

    public static AcceptAssetFragment newInstance(String text) {

        AcceptAssetFragment f = new AcceptAssetFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }


    private void closeSticker() {
        final Intent intent = new Intent( ACTION_CLOSE_STICKER );
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

            if ( ACTION_STICKER_CLOSED.equals(action)) {
                Log.d("TRACK", "STICKER CLOSED");
             //   disconnectSticker();
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
        intentFilter.addAction( ACTION_SET_INTERVAL_OK );
        intentFilter.addAction( ACTION_STICKER_READ );
        intentFilter.addAction( ACTION_SET_UTC_SUCCESS );
        intentFilter.addAction( ACTION_STICKER_OPENED );
        intentFilter.addAction( ACTION_STICKER_NOT_OPENED );
        intentFilter.addAction( ACTION_GATT_DISCONNECTED );

        return intentFilter;
    }
}
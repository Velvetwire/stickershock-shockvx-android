package com.ice.stickershock_shockvx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TrackAssetFragment extends Fragment {
    Button mTrackButton;
    EditText mName, mLocation;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.track_asset, container, false);
        super.onCreate(savedInstanceState);

        mTrackButton = (Button)   v.findViewById(R.id.trackButton);
        mName        = (EditText) v.findViewById(R.id.assetName);
        mLocation    = (EditText) v.findViewById(R.id.assetLocation);

        mTrackButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // createId();

                        openSticker();
                        saveStickerInfo();
                        Intent intent = new Intent(getActivity(), MainAssetScreen.class);
                        startActivity(intent);


                    }
                }
        );
        return v;
    }


    public static TrackAssetFragment newInstance(String text) {

        TrackAssetFragment f = new TrackAssetFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
    private void openSticker() {
        // send broadcast command to open sticker with id
        return;
    }

    private void saveStickerInfo() {
        // send broadcast command to open sticker with id
        return;
    }
}


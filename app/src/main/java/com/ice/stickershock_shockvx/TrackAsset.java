package com.ice.stickershock_shockvx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TrackAsset extends Activity {
    Button mTrackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_asset);

        mTrackButton = (Button)findViewById(R.id.trackButton);

        mTrackButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // createId();

                        openSticker();
                        saveStickerInfo();
                        Intent intent = new Intent(TrackAsset.this, TopScreen.class);
                        startActivity(intent);


                    }
                }
        );
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


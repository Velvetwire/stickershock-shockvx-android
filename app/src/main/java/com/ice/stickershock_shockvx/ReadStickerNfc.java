/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *    file: ReadStickerNfc.java
 *
 * Read Nfc Tag for android shockVx app
 *
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
package com.ice.stickershock_shockvx;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ice.stickershock_shockvx.bluetooth.BluetoothScanActivity;

import java.io.File;
import java.io.IOException;

import static android.content.ContentValues.TAG;
import static com.ice.stickershock_shockvx.Constants.*;

public class ReadStickerNfc extends Activity {
    TextView mNfcMessage;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        // Get NFC Adapter
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcMessage = (TextView) findViewById(R.id.nfc_message);
    }


    /**
     * Here is routine which searches for NFC tags
     * filter is set up to look for NFC tags that have NDEF payloads
     * and MIME-Type text/xml
     * Other tag encodings will be ignored
     */
    @Override
    protected void onResume() {
        super.onResume();
//        mErrorView.setText("");

        IntentFilter ndefDetected = null;
        try {
            ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED, TEXT_XML);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }

        IntentFilter[] nfcIntentFilter = new IntentFilter[]{ ndefDetected, };

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if (mNfcAdapter != null)
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String unit = "";
        String primary = "";
        String control = "";
        boolean mTagFound = false;

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        Log.d(TAG, "onNewIntent: " + intent.getAction());

        if (tag != null) {
            vibratePhone( VIBRATE_150_MS );
            Log.d(TAG, "Message tag detected: " + tag);
            Ndef ndef = Ndef.get(tag);

            if (ndef != null) {
                try {
                    ndef.connect();
                    NdefMessage ndefMessage = ndef.getNdefMessage();
                    ndef.close();

                    if (ndefMessage != null) {
                        String message = new String(ndefMessage.getRecords()[0].getPayload());

                        unit    = extractFieldFromXML(message, "unit");
                        primary = extractFieldFromXML(message, "primary");
                        control = extractFieldFromXML(message, "control");
                        mTagFound = true;

                    } else {
                        Log.d(TAG, "NULL MESSAGE FROM NFC" );
                        mTagFound = false;
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (FormatException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "NDEF GET IS NULL: ");
            }
            if (mTagFound)  {
                Log.d(TAG, "UNIT: " + unit);
                Log.d(TAG,  "PRIMARY: " + primary );
                Log.d(TAG,  "CONTROL: " + control );
                Intent i = new Intent(ReadStickerNfc.this, BluetoothScanActivity.class);
                i.putExtra("unit", unit);
                i.putExtra("primary", primary);
                i.putExtra("control", control);
                startActivity(i);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:

                if (resultCode == RESULT_OK) {
                    // Bluetooth Advertisements supported on this device?
        //            if (mBluetoothAdapter.isMultipleAdvertisementSupported()) {
         //               showErrorText(R.string.blank);
         //           } else {
                        showErrorText(R.string.bt_ads_not_supported);
          //          }
                } else {
                    // User declined to enable Bluetooth, exit the app.
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }

            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void showErrorText(int message) {

        TextView view = (TextView) findViewById(R.id.error_textview);
        view.setText(message);
    }



    private String extractFieldFromXML(String message, String tag ) {
        String startTag = "<" + tag + ">";
        String endTag   = "</" + tag + ">";

        String substr = message.substring(message.indexOf(startTag) + startTag.length(), message.indexOf(endTag));
        return substr;
    }


    // Vibrate for 150 milliseconds
    private void vibratePhone(int milliseconds) {
        if (Build.VERSION.SDK_INT >= 26)
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        else ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(milliseconds);
    }

    private void findKnownTags(String filename) {
        File file = new File(getApplicationContext().getFilesDir(), filename);
    }
}

    /*
     *    Typical XML from sticker
     *    <tag>
     *      <device>
     *        <unit>1EA3-28F3-9599-0922</unit>                                // unit (look for this in advert
     *      </device>
     *      <service>
     *        <primary>56780000-5657-5353-2020-56454C564554</primary>         // sensor access service
     *        <control>00004143-5657-5353-2020-56454C564554</control>         // sensor control service
     *      </service>
     *   <tag>
     */


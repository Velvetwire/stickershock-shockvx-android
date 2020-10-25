package com.ice.stickershock_shockvx.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


import com.ice.stickershock_shockvx.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static com.ice.stickershock_shockvx.Constants.*;
import static com.ice.stickershock_shockvx.bluetooth.BluetoothControlActivity.*;
import static com.ice.stickershock_shockvx.bluetooth.GattAttributes.*;
import static com.ice.stickershock_shockvx.bluetooth.Actions.*;
import static com.ice.stickershock_shockvx.Helpers.*;
/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class         BluetoothScanActivity extends ListActivity {
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter    mBluetoothAdapter;
    private boolean             mScanning;
    private Handler             mHandler;
    private BluetoothLeScanner  mLEScanner;
    private ScanSettings        mScanSettings;
    List<ScanFilter>            mScanFilters = new ArrayList<ScanFilter>();


    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 3000;


    String mUnit = "";
    String mControl = "";
    String mPrimary = "";

    @RequiresApi( api = Build.VERSION_CODES.M )
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setTitle( R.string.track_asset );
        getActionBar().setDisplayHomeAsUpEnabled( true );

        mHandler = new Handler();

        final Intent intent = getIntent();
        mUnit    = intent.getStringExtra ( EXTRAS_UNIT );
        mPrimary = intent.getStringExtra ( EXTRAS_PRIMARY );
        mControl = intent.getStringExtra ( EXTRAS_CONTROL );
        if (mUnit == null)
            mUnit = "";

        if (mControl == null)
            mControl = "";

        // ACCESS_FINE_LOCATION needs to be enabled for the BLE to work.
        // In newer versions of android, prompt user to turn on FINE_LOCATION
        // as location is considered a "sensitive" permission

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_REQUEST_FINE_LOCATION );
        }
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService( Context.BLUETOOTH_SERVICE );
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
        }
        return true;
    }

    // initialize List view adapter
    @Override
    protected void onResume() {
        super.onResume();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);
        scanLeDevice(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLeDeviceListAdapter.clear();
        finish();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;

        connectDevice( device );
    }

    // Connect to device and pass control to BluetoothControlActivity
    protected void connectDevice(BluetoothDevice device) {
        final Intent intent = new Intent(this, BluetoothControlActivity.class);
        String deviceName = device.getName();
        if (deviceName == null)
            deviceName = "unknown";

        intent.putExtra( EXTRAS_DEVICE_UNIT, mUnit);
        intent.putExtra( EXTRAS_DEVICE_NAME, deviceName);
        intent.putExtra( EXTRAS_DEVICE_ADDRESS, device.getAddress());
        if (mScanning) {
            scanLeDevice(false);
            mScanning = false;
        }
        startActivity(intent);

    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<>();
            mInflator = BluetoothScanActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if(!mLeDevices.contains(device)) {
                mLeDevices.add(device);
                connectDevice (device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        // Construct ListView.
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;

            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                //viewHolder.deviceAddress = view.findViewById(R.id.device_address);
                viewHolder.deviceName    = view.findViewById(R.id.device_name);
                viewHolder.deviceUnit    = view.findViewById(R.id.device_unit);
                //viewHolder.deviceControl = view.findViewById(R.id.device_control);
                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            Log.d("DEVICE FOUND", device.toString());

            // Device name is not always found
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText("Stickershock");

            //viewHolder.deviceAddress.setText(device.getAddress());
            viewHolder.deviceUnit.setText(mUnit);
            //viewHolder.deviceControl.setText("");
            return view;
        }
    }

    // Device scan callback.
    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d("BluetoothLeScanner", result.toString());

            // Filter for tag type here as the ScanFilter does not seem to work reliably for ServiceUuids
            // Iterate through service IDs. If Stickershock Service is found, add to list.
            // For this case we are using the SENSOR_CONTROL_SERVICE

            if (result.getScanRecord().getServiceUuids() != null) {
                List<ParcelUuid> uuidList = result.getScanRecord().getServiceUuids();
                for (int i = 0; i < uuidList.size(); i++ )  {
                   String uuid = uuidList.get(i).toString();
                   Log.d("UUID", uuid + " : " + SENSOR_CONTROL_SERVICE);

                   if (uuid.equals( SENSOR_CONTROL_SERVICE) == true) {
                       mLeDeviceListAdapter.addDevice(result.getDevice());
                       mLeDeviceListAdapter.notifyDataSetChanged();
                   }
                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.d("BluetoothScanActivity", "scan Results " + results.size());
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d("BluetoothScanActivity", "scan Failed");
        }
    };

    static class ViewHolder {
        TextView deviceName;
        TextView deviceUnit;
        TextView deviceAddress;
        TextView deviceControl;
    }

    // Main scanning routine
    private void scanLeDevice(final boolean enable)  {
        final BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    bluetoothLeScanner.stopScan(( ScanCallback ) mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            setScanFilter();
            setScanSettings();

            bluetoothLeScanner.startScan( mScanFilters, mScanSettings,mLeScanCallback );
        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan( mLeScanCallback );
        }
        invalidateOptionsMenu();
    }

    // set scan filters, look only for stickers
    UUID SENSOR_CONTROL_SERVICE_UUID = UUID.fromString( SENSOR_CONTROL_SERVICE );
    UUID[] serviceUUIDs = new UUID[] { SENSOR_CONTROL_SERVICE_UUID};
    private void setScanFilter() {
        ScanFilter mScanFilter;
        ScanFilter.Builder mBuilder = new ScanFilter.Builder();

//          mBuilder.setServiceData ( mServiceData.array(), mServiceDataMask.array() );
//          mBuilder.setServiceSolicitationUuid ( ParcelUUID(uuid) );
        Log.d("ScanFilter", mControl);

        // get UUID filter from NFC tag.
//          mBuilder.setServiceUuid (new ParcelUuid( UUID.fromString(mControl)) );

        mScanFilter = mBuilder.build();
        mScanFilters.add(mScanFilter);
    }

    // Scan Settings
    private void setScanSettings() {
        ScanSettings.Builder mBuilder = new ScanSettings.Builder();

        mBuilder.setReportDelay(0);
        mBuilder.setScanMode( ScanSettings.SCAN_MODE_LOW_LATENCY );
        mScanSettings = mBuilder.build();
    }
}

package com.example.wifiscaner;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private WifiManager wifiManager;
    private ListView listView;
    private Button buttonScan;
    private int size = 0;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonScan = findViewById(R.id.scanBtn);
        listView = findViewById(R.id.wifiList);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
                }else{
                    results = wifiManager.getScanResults();
                    size = results.size();
                }
                scanWifi();
            }
        });
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "WiFi is disabled ... Enabling", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int i, long l) {
                Intent intentDescription = new Intent(getApplicationContext(), WifiDescription.class);
                ScanResult scanResult = results.get(i);
                intentDescription.putExtra("SSID", scanResult.SSID);
                intentDescription.putExtra("BSSID", scanResult.BSSID);
                intentDescription.putExtra("capabilities", scanResult.capabilities);
                intentDescription.putExtra("centerFreq", Integer.toString(scanResult.centerFreq0));
                intentDescription.putExtra("centerFreq1", Integer.toString(scanResult.centerFreq1));
                intentDescription.putExtra("frequency",  Integer.toString(scanResult.frequency));
                intentDescription.putExtra("level", Integer.toString(scanResult.level));
                intentDescription.putExtra("channelWidth", Integer.toString(scanResult.channelWidth));
                intentDescription.putExtra("timeStamp", Long.toString(scanResult.timestamp));
                startActivity(intentDescription);
            }
        });

        if(isServiceOK()){
            init();
        }
    }

    private void init(){
        Button btnMap = (Button) findViewById(R.id.mapBtn);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMap = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intentMap);
            }
        });
    }

    private void scanWifi() {
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this, "Scanning WiFi", Toast.LENGTH_SHORT).show();
    }

    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            results = wifiManager.getScanResults();
            unregisterReceiver(this);

            for (ScanResult scanResult : results) {
                arrayList.add(scanResult.SSID);
                adapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1001 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Do something with granted permission
            results = wifiManager.getScanResults();
            size = results.size();
        }
    }

    public Boolean isServiceOK(){
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        if (available == ConnectionResult.SUCCESS){
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this,"Cannot open maps", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}

package com.example.wifiscaner;

import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class WifiDescription extends AppCompatActivity {

    public final static int CHANNEL_WIDTH_20MHZ = 0;
    public final static int CHANNEL_WIDTH_40MHZ = 1;
    public final static int CHANNEL_WIDTH_80MHZ = 2;
    public final static int CHANNEL_WIDTH_160MHZ = 3;
    public final static int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;

    WifiData data = new WifiData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_description);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //get data
            data.setSsid(extras.getString("SSID"));
            data.setBssid(extras.getString("BSSID"));
            data.setCapabilities(extras.getString("capabilities"));
            data.setCenterFreq0(extras.getString("centerFreq"));
            data.setCenterFreq1(extras.getString("centerFreq1"));
            data.setFrequency(extras.getString("frequency"));
            data.setLevel(extras.getString("level"));
            data.setChannelWidth(Integer.parseInt(extras.getString("channelWidth")));
            data.setTimeStamp(Long.parseLong(extras.getString("timeStamp")));

            //get textView
            TextView ssidData = (TextView) findViewById(R.id.tv_SSID);
            TextView bssidData = (TextView) findViewById(R.id.tv_signal);
            TextView capabilityData = (TextView) findViewById(R.id.tv_security);
            TextView centerFreq0Data = (TextView) findViewById(R.id.tv_centerFreq0);
            TextView centerFreq1Data = (TextView) findViewById(R.id.tv_centerFreq1);
            TextView freqData = (TextView) findViewById(R.id.tv_Freq);
            TextView levelData = (TextView) findViewById(R.id.tv_Level);
            TextView chanelWidthData = (TextView) findViewById(R.id.tv_ChanelWidth);
            TextView timeStampData = (TextView) findViewById(R.id.tv_timeStamp);

            //set textView
            ssidData.setText("SSID: " + data.getSsid());
            bssidData.setText("BSSID: " + data.getBssid());
            capabilityData.setText("Capabilities: " + data.getCapabilities());
            centerFreq0Data.setText("Center Freq0: " + data.getCenterFreq0() + " MHz");
            centerFreq1Data.setText("Center Freq1: " + data.getCenterFreq1() + " MHz");
            freqData.setText("Frequency: " + data.getFrequency() + " MHz");
            levelData.setText("Level: " + data.getLevel() + "dBm");

            if (data.getChannelWidth() == CHANNEL_WIDTH_20MHZ){
                chanelWidthData.setText("Channel Width: 20 MHz" );
            }else if (data.getChannelWidth() == CHANNEL_WIDTH_40MHZ) {
                chanelWidthData.setText("Channel Width: 40 MHz" );
            } else if (data.getChannelWidth() == CHANNEL_WIDTH_80MHZ) {
                chanelWidthData.setText("Channel Width: 80 MHz" );
            }else if (data.getChannelWidth() == CHANNEL_WIDTH_160MHZ) {
                chanelWidthData.setText("Channel Width: 160 MHz" );
            }else {
                chanelWidthData.setText("Channel Width: 80 MHz + 80 MHz" );
            }

            Long timeStamp = data.getTimeStamp();
            Long days = timeStamp/ 86400000000L;
            timeStamp -= days * 86400000000L;
            Long hours = timeStamp / 3600000000L;
            timeStamp -= hours * 3600000000L;
            Long minutes = timeStamp / 60000000L;
            timeStampData.setText("Time since boot: " + days + " d " + hours + " h " + minutes + " m");
        }

        Button backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WifiDescription.this,"Saving...", Toast.LENGTH_SHORT).show();
                writeJSON(data);
            }
        });
    }

    public void writeJSON(WifiData data){
        //JSONObject object = new JSONObject();
        Map<String, String> object= new HashMap<String, String>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        object.put("ssid", data.getSsid());
        object.put("bssid", data.getBssid());
        object.put("capabilities", data.getCapabilities());
        object.put("centerfreq0", data.getCenterFreq0());
        object.put("centerfreq1", data.getCenterFreq1());
        object.put("frequency", data.getFrequency());
        object.put("level", data.getLevel());

        if (data.getChannelWidth() == CHANNEL_WIDTH_20MHZ){
            object.put("channelWidth", "20");
        }else if (data.getChannelWidth() == CHANNEL_WIDTH_40MHZ) {
            object.put("channelWidth", "40");
        } else if (data.getChannelWidth() == CHANNEL_WIDTH_80MHZ) {
            object.put("channelWidth", "80");
        }else if (data.getChannelWidth() == CHANNEL_WIDTH_160MHZ) {
            object.put("channelWidth", "160");
        }else {
            object.put("channelWidth", "80 + 80");
        }

        object.put("timeStamp", data.getTimeStamp().toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                getResources().getString(R.string.URL), new JSONObject(object),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(WifiDescription.this,response.toString(),Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(WifiDescription.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjReq);
    }
}

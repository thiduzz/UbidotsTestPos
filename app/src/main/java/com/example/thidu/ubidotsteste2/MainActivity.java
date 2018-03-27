package com.example.thidu.ubidotsteste2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.ubidots.Value;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 10010;
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_FINE_LOCATION = 10020;
    private static final String BATTERY_LEVEL = "level";
    private TextView mBatteryLevel;
    private Button mRefreshBtn;
    private LineChart chart;



    public Handler handlerReceiverClient = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what)
            {
                case 1616:
                    Log.e("TESTE-INSENSE",message.obj.toString());

                    List<Entry> entries = new ArrayList<Entry>();
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setGranularity(600000f); // one hour
                    for (Value v : (Value[])message.obj ) {
                        Timestamp stamp = new Timestamp(v.getTimestamp());
                        Date date = new Date(stamp.getTime());
                        // turn your data into Entry objects
                        entries.add(new Entry(v.getTimestamp(), (float)v.getValue()));
                    }
                    LineDataSet dataSet = new LineDataSet(entries, "Bateria"); // add entries to dataset
                    dataSet.setColor(R.color.colorPrimary);
                    dataSet.setValueTextColor(R.color.colorAccent); // styling, ...
                    LineData lineData = new LineData(dataSet);
                    chart.setData(lineData);
                    chart.invalidate(); // refresh
                    break;
                case 404:
                    Log.e("TESTE-INSENSE",message.obj.toString());
                    Toast.makeText(getApplicationContext(),"DEU RUIM!",Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }
    });

    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            int level = intent.getIntExtra(BATTERY_LEVEL, 0);
            mBatteryLevel.setText(Integer.toString(level) + "%");
            new ApiUbidotsBattery().execute(level);

        }

    };

    private BroadcastReceiver mConnReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if(currentNetworkInfo.isConnected()){
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                new ApiUbidotsWifi().execute(1);
            }else{
                Toast.makeText(getApplicationContext(), "Not Connected", Toast.LENGTH_LONG).show();
                new ApiUbidotsWifi().execute(0);
            }
        }
    };


    @Override
    protected void onStart() {

        super.onStart();
        registerReceiver(mBatteryReceiver, new
                IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        registerReceiver(mConnReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mConnReceiver);
        unregisterReceiver(mBatteryReceiver);
        super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBatteryLevel = (TextView) findViewById(R.id.batteryLevel);
        mRefreshBtn = (Button) findViewById(R.id.Refresh);
        mRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ApiUbidotsInfo(handlerReceiverClient).execute();
            }
        });
        chart = (LineChart) findViewById(R.id.chart);
    }
}

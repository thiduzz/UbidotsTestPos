package com.example.thidu.ubidotsteste2;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.ubidots.ApiClient;
import com.ubidots.Value;
import com.ubidots.Variable;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by thidu on 3/26/2018.
 */

public class ApiUbidotsInfo extends AsyncTask<Void, Void, Void> {
    private Handler handlerReceiverClient;

    private final String API_KEY = "BBFF-gOFGJRQjHnLUcSUyRZeYgSOFBr1Nez";
    private final String VARIABLE_ID = "5ab967b3642ab631f6cf8d3e";


    public ApiUbidotsInfo(Handler handlerReceiverClient) {
        this.handlerReceiverClient = handlerReceiverClient;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {

            ApiClient apiClient = new ApiClient().fromToken(API_KEY);
            Variable batteryLevel =
                    apiClient.getVariable(VARIABLE_ID);
            Value[] valores = batteryLevel.getValues();
            handlerReceiverClient.obtainMessage(1616, valores).sendToTarget();
        } catch (Exception ex){
            ex.printStackTrace();
            handlerReceiverClient.obtainMessage(500, ex.getMessage()).sendToTarget();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

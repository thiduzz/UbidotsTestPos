package com.example.thidu.ubidotsteste2;

import android.os.AsyncTask;

import com.ubidots.*;
import com.ubidots.ApiClient;
import com.ubidots.Variable;

/**
 * Created by thidu on 3/26/2018.
 */
public class ApiUbidotsBattery extends AsyncTask<Integer, Void, Void> {

    private final String API_KEY = "BBFF-gOFGJRQjHnLUcSUyRZeYgSOFBr1Nez";
    private final String VARIABLE_ID = "5ab967b3642ab631f6cf8d3e";

    @Override
    protected Void doInBackground(Integer... params) {

        ApiClient apiClient = new ApiClient().fromToken(API_KEY);
        Variable batteryLevel =
                apiClient.getVariable(VARIABLE_ID);
        batteryLevel.saveValue(Integer.parseInt(String.valueOf(params[0])));
        return null;
    }
}
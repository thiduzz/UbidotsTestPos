package com.example.thidu.ubidotsteste2;

import android.os.AsyncTask;

import com.ubidots.ApiClient;
import com.ubidots.Variable;

/**
 * Created by thidu on 3/26/2018.
 */
public class ApiUbidotsWifi extends AsyncTask<Integer, Void, Void> {

    private final String API_KEY = "BBFF-gOFGJRQjHnLUcSUyRZeYgSOFBr1Nez";
    private final String VARIABLE_ID = "5ab98585642ab649b61ae77b";

    @Override
    protected Void doInBackground(Integer... params) {

        ApiClient apiClient = new ApiClient().fromToken(API_KEY);
        Variable connection =
                apiClient.getVariable(VARIABLE_ID);
        connection.saveValue(params[0]);
        return null;
    }
}
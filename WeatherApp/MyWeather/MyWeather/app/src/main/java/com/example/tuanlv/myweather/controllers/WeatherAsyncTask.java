package com.example.tuanlv.myweather.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.example.tuanlv.myweather.models.GoogleData;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Ta Giang on 6/25/2015.
 */
public class WeatherAsyncTask extends AsyncTask<Void,Void,GoogleData> {
    Context mContext;
    String q;
    ProgressDialog dialog;
    double mLat;
    double mLon;
    TypePrediction mType;
    NumberFormat format = new DecimalFormat("#0.0");
    public WeatherAsyncTask(Context context, String q) {
        this.mContext=context;
        this.q=q;
        this.mType = TypePrediction.ADDRESS_NAME;

        this.dialog=new ProgressDialog(context);
        this.dialog.setTitle("Đang tải ...");
        this.dialog.setMessage("Vui lòng chờ...");
        this.dialog.setCancelable(true);
    }
    public WeatherAsyncTask(Context context, double lat, double lon){
        this.mContext = context;
        this.mLat = lat;
        this.mLon = lon;
        this.mType = TypePrediction.LATITUDE_LONGITUDE;

        this.dialog=new ProgressDialog(context);
        this.dialog.setTitle("Đang tải ...");
        this.dialog.setMessage("Vui lòng chờ...");
        this.dialog.setCancelable(true);
    }

    @Override
    protected GoogleData doInBackground(Void... voids) {
        try {
            GoogleData googleData;
            if(mType == TypePrediction.ADDRESS_NAME){
                googleData = WeatherGSon.getWeatherJsonObject(q, 5);
            } else if(mType == TypePrediction.LATITUDE_LONGITUDE){
                googleData = WeatherGSon.getWeatherJSonObject(mLat, mLon, 5);
            } else {
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
                return null;
            }
            return  googleData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog.show();
    }

    @Override
    protected void onPostExecute(GoogleData googleData) {
        super.onPostExecute(googleData);
        GoogleData data=googleData;
        if(data==null){
              dialog.show();
        }else{
            Update update=new Update(mContext,data);
            update.updateDataToView();
            this.dialog.dismiss();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}
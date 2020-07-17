package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class WeatherForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private class WeatherForecast extends AsyncTask<String, Integer, String> {

        private String min;
        private String max;
        private String UV;
        private String currentTemperature;
        private Bitmap pic;

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}
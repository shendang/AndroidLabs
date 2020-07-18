package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecastActivity extends AppCompatActivity {

    ProgressBar progressBar;
    TextView currentTempTextView;
    TextView maxTextView;
    TextView minTextView;
    TextView uvTextView;
    ImageView weatherIconImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        progressBar = findViewById(R.id.progressBar);
        currentTempTextView = findViewById(R.id.current_temp_text_view);
        maxTextView = findViewById(R.id.max_temp_text_view);
        minTextView = findViewById(R.id.min_temp_text_view);
        uvTextView = findViewById(R.id.uv_text_view);
        weatherIconImageView = findViewById(R.id.temp_icon_image_view);
        progressBar.setVisibility(View.VISIBLE);

        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        private String min;
        private String max;
        private String uv;
        private String currentTemperature;
        private Bitmap img;

        @Override
        protected String doInBackground(String... args) {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();


                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");


                //From part 3, slide 20
                String parameter = null;

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        //If you get here, then you are pointing at a start tag
                        if (xpp.getName().equals("temperature")) {
                            //If you get here, then you are pointing to a <Weather> start tag

                            currentTemperature = xpp.getAttributeValue(null, "value");
                            publishProgress(25);
                            max = xpp.getAttributeValue(null, "max");
                            publishProgress(50);
                            min = xpp.getAttributeValue(null, "min");
                            publishProgress(75);
                        } else if (xpp.getName().equals("weather")) {
                            String icon = xpp.getAttributeValue(null, "icon");
                            img = getImg(icon);
                            if (img != null) {
                                FileOutputStream outputStream = openFileOutput(icon + ".png", Context.MODE_PRIVATE);
                                img.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                                publishProgress(100);
                            }
                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                //create a URL object of what server to contact:
                URL urlForUV = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");

                //open the connection
                HttpURLConnection urlForUVConnection = (HttpURLConnection) urlForUV.openConnection();

                //wait for data:
                InputStream responseForUV = urlForUVConnection.getInputStream();

                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseForUV, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                double uvRating = uvReport.getDouble("value");
                uv = String.valueOf(uvRating);


            } catch (Exception e) {

            }

            return "Done";
        }

        public void onProgressUpdate(Integer... args) {


        }

        public void onPostExecute(String fromDoInBackground) {
            progressBar.setVisibility(View.INVISIBLE);
            weatherIconImageView.setImageBitmap(img);
            currentTempTextView.setText("Current Temperature: " + currentTemperature);
            maxTextView.setText("Max Temperature: " + max);
            minTextView.setText("Min Temperature: " + min);
            uvTextView.setText("UV rate: " + uv);
            Log.i("HTTP", fromDoInBackground);

        }


        private Bitmap getImg(String iconName) throws IOException {

            if (fileExistance(iconName + ".png")) {
                FileInputStream fis = null;
                try {
                    fis = openFileInput(iconName + ".png");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Log.i("GetImg", "Img already exists");
                return BitmapFactory.decodeStream(fis);

            }

            Bitmap image = null;
            URL url = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                image = BitmapFactory.decodeStream(connection.getInputStream());
            }
            Log.i("GetImg", "Downloaded new img");
            return image;
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

    }
}
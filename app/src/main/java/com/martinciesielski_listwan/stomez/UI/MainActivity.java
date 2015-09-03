package com.martinciesielski_listwan.stomez.UI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.martinciesielski_listwan.stomez.R;
import com.martinciesielski_listwan.stomez.weather.Current;
import com.martinciesielski_listwan.stomez.weather.Forecast;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends ActionBarActivity {

    public static final String Tag = MainActivity.class.getSimpleName();

    private Current mCurrent;

    double latitude = 43.2620210;
    double longitude =
            -79.9107060;

    // Creates variables to work with
    @Bind(R.id.timeLabel) TextView mTimeLabel;
    @Bind(R.id.temperatureLabel) TextView mTemperatureLabel;
    @Bind(R.id.humidityValue) TextView mHumidityValue;
    @Bind(R.id.precipValue) TextView mPrecipValue;
    @Bind(R.id.summaryLabel) TextView mSummaryLabel;
    @Bind(R.id.iconImageView) ImageView mIconImageView;
    @Bind(R.id.refreshImageView) ImageView mRefreshImageView;
    @Bind(R.id.progressBar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Hides progress bar
        //mProgressBar.setVisibility(View.INVISIBLE);
        //Imports all of the above
        ButterKnife.bind(this);

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getForecast(latitude, longitude);
            }
        });

        getForecast(latitude, longitude);


        Log.d(Tag, "Main UI Code is running, keeps working while background goes");


    }

    private void getForecast(double latitude, double longitude) {
        //Reason why we broke this down is because it lets us add in lat and long afterwards
        String apiKey = "31ba807736d4f824c0f8502b49e85696";
        String forecastUrl = "https://api.forecast.io/forecast/" +
                apiKey + "/" + latitude + "," + longitude ;
        if (isNetworkAvailable()) {
            toggleRefresh();

            //Once you add okhttpclient to the lsit of dependecies in android
            //manufest you will get shortcuts for those new classes
            // creates new class with default constructor
            OkHttpClient client = new OkHttpClient();
            //This is called chaining methods, each chained method goes to next line
            // This is what we send to the server
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();

            // Put the request inside the call object, you execute the call and
            // it returns a request object
            Call call = client.newCall(request);
            // the parameter is a callback object
            // executes the call by putting it in a que, code is executed from the first one there
            call.enqueue(new Callback() {
                // anonomous inner class
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    // If something bad happens, then we can catch them, if we dont they crash
                    try {
                        String jsonData = response.body().string();
                        Log.v(Tag, jsonData);
                        if (response.isSuccessful()) {
                            mCurrent = getCurrentDetails(jsonData);
                            // Since the UI to update cant be updated on a background thread
                            // this allows you update on main thread
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });

                             } else {
                            alertUserAboutError();
                        }
                        // IOException stands for input output exception
                        //you wrap your code in a try catch to see if its succeffull
                        // if it has this specific exception, then do this
                    }
                    catch (IOException e) {
                        // e is for logging an exception,
                        Log.e(Tag, "Exception caught: ", e);
                    }
                    catch (JSONException e){
                        Log.e(Tag, "Exception caught: ", e);
                    }

                }
            });
        }
        else {
            // make this into a fragment
            Toast.makeText(this, "Network is unavailable",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }


    }

    private void updateDisplay() {
        mTemperatureLabel.setText(mCurrent.getTemperature() + "");
        mTimeLabel.setText("At " + mCurrent.getFormatedTime() + " it will be");
        mHumidityValue.setText(mCurrent.getHumidity() + "");
        mPrecipValue.setText(mCurrent.getPrecipChance() + "%");
        mSummaryLabel.setText(mCurrent.getSummary());

        Drawable drawable = getResources().getDrawable(mCurrent.getIconID());
        mIconImageView.setImageDrawable(drawable);
    }

    private Forecast parseForecastDetails(String jsonData){
        Forecast forecast = new Forecast();

        return forecast;
    }
    // This special Json class can hold any object in the json format
    // The constructor allows us to pass in a string of json data
    // to create a new json objects
    private Current getCurrentDetails(String jsonData) throws JSONException {
        // throwing the exception means that whoever calls the function
        // must now handle the additional exception
        // Converts json data to a json objects we can manipulate
        JSONObject forecast = new JSONObject(jsonData);
        String timezone = forecast.getString("timezone");
        Log.i(Tag, "From JSON:" + timezone);

        JSONObject currently = forecast.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setTimeZone(timezone);


        return current;
    }

    private boolean isNetworkAvailable() {
        // the parameter is the name of the service that we want
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        // Good to set it to false on default
        boolean isAvailable = false;
        //checks if present and connected to the internet
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }


}

package com.example.howitsoutside;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.howitsoutside.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG=MainActivity.class.getSimpleName();
    private CurrentWeather currentWeather;
    private ImageView iconImageView;
    final double latitude=28.6517;
    final double longitude=77.2219;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getForcast(latitude,longitude);

    }

    private void getForcast(double latitude,double longitude ) {
        final ActivityMainBinding binding= DataBindingUtil.setContentView(MainActivity.this,R.layout.activity_main) ;

        iconImageView =findViewById(R.id.iconImageView);
        String apiKey=getAPIKey();

        String forcastURL="https://api.darksky.net/forecast/"+apiKey+"/"+latitude+","+longitude;
        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forcastURL)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            currentWeather=getCurrentDetails(jsonData);

                             final CurrentWeather displayWeather=new CurrentWeather(
                                    currentWeather.getLocationLabel(),
                                    currentWeather.getIcon(),
                                    currentWeather.getTime(),
                                    currentWeather.getTemperture(),
                                    currentWeather.getHumidity(),
                                    currentWeather.getPrecipChance(),
                                    currentWeather.getSummary(),
                                    currentWeather.getTimeZone()
                            );
                            binding.setWeather(displayWeather);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable=getResources().getDrawable(displayWeather.getIconId());
                                    iconImageView.setImageDrawable(drawable);
                                }
                            });

                        } else {
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception caught: ", e);

                    } catch(JSONException e){
                        Log.e(TAG,"JSON Exception caught",e);
                    }

                }
            });
        }
    }

    /*
    * an utility method that reads the file 'api_key' from the assets folder and fetches
    * the api key stored in that file, that way we don't have to hard code the api
    * in the main code
    * */
    private String getAPIKey() {

        try{
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(getAssets().open("api_key"))); // file that contains the api key
            String line;
            /*
            * the api key is only a single line string, thus
            * reading a single line once works pretty well
            * */
            line = bufferedReader.readLine();
            bufferedReader.close();
            return line;
        } catch (Exception e){
            e.printStackTrace();
        }

        return null;

    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast=new JSONObject(jsonData);
        String timezone=forecast.getString("timezone");
        JSONObject currently=forecast.getJSONObject("currently");
        Log.i(TAG,"from JSON "+timezone);
        CurrentWeather currentWeather=new CurrentWeather();
        currentWeather.setHumidity(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setLocationLabel("New Delhi ,India");
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperture(currently.getDouble("temperature"));
        currentWeather.setTimeZone(timezone);
        Log.d(TAG,currentWeather.getFormattedTime());
        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable =false;
        if(networkInfo != null && networkInfo.isConnected()){
            isAvailable =true;
        }
        else{
            Toast.makeText(this,R.string.network_unavailable_message,Toast.LENGTH_LONG).show();
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog=new AlertDialogFragment();
        dialog.show(getFragmentManager(),"error_dialog");
    }
    public void refreshOnClick(View view){
        getForcast(latitude,longitude);
        Toast.makeText(this,"Refreshing data",Toast.LENGTH_LONG).show();
    }
}

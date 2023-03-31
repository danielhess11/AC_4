package com.example.ac_4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WeatherFragment extends Fragment {


    private TextView tvLocation;
    private TextView tvTemperature;
    private TextView tvWeatherDescription;
    private TextView tvMinMaxTemperature;
    private TextView tvWindSpeed;
    private TextView tvHumidity;
    private TextView tvPressure;

    private double latitude;
    private double longitude;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        // Get the latitude and longitude from the MainActivity
        latitude = ((MainActivity) getActivity()).getLatitude();
        longitude = ((MainActivity) getActivity()).getLongitude();

        tvLocation = view.findViewById(R.id.tv_location);
        tvTemperature = view.findViewById(R.id.tv_temperature);
        tvWeatherDescription = view.findViewById(R.id.tv_weather_description);
        tvMinMaxTemperature = view.findViewById(R.id.tv_min_max_temperature);
        tvWindSpeed = view.findViewById(R.id.tv_wind_speed);
        tvHumidity = view.findViewById(R.id.tv_humidity);
        tvPressure = view.findViewById(R.id.tv_pressure);

        // Make network request to OpenWeatherMap API
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude +
                "&lon=" + longitude + "&appid=62b1d8a81ca45bfe049ed9efc5c463fb";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Get weather data from the API response
                        JSONObject main = response.getJSONObject("main");
                        JSONObject wind = response.getJSONObject("wind");
                        JSONArray weather = response.getJSONArray("weather");
                        JSONObject weatherObj = weather.getJSONObject(0);

                        // Set weather data to the views
                        tvLocation.setText(response.getString("name"));
                        tvTemperature.setText(main.getString("temp") + "°C");
                        tvWeatherDescription.setText(weatherObj.getString("description"));
                        tvMinMaxTemperature.setText("Min / Max Temperature: " + main.getString("temp_min")
                                + "°C / " + main.getString("temp_max") + "°C");
                        tvWindSpeed.setText("Wind Speed: " + wind.getString("speed") + "m/s");
                        tvHumidity.setText("Humidity: " + main.getString("humidity") + "%");
                        tvPressure.setText("Pressure: " + main.getString("pressure") + "hPa");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Error fetching weather data", Toast.LENGTH_SHORT).show();
                });

        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);

        return view;
    }

}
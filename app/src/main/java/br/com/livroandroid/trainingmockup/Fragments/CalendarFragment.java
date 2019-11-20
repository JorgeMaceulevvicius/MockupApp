package br.com.livroandroid.trainingmockup.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.livroandroid.trainingmockup.Connection.Weather;
import br.com.livroandroid.trainingmockup.R;


public class CalendarFragment extends Fragment {

    private String city = null;
    private TextView tvTemp, tvTime, tvUserLocation;

    public CalendarFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            city = getArguments().getString("cityLocation");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        tvUserLocation = view.findViewById(R.id.tvUserLocation);
        tvTemp = view.findViewById(R.id.tvTemperatura);
        tvTime = view.findViewById(R.id.tvTime);

        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date currentDate = cal.getTime();
        String hour = dateFormat_hora.format(currentDate);


        tvUserLocation.setText(city);
        tvTime.setText(hour);
        tvTemp.setText(" " + getTemperature(city) + "°C");

        return view;
    }

    private String getTemperature(String city) {

        String temperature = "";

        Weather weather = new Weather();

        try {
            String content = weather
                    .execute("https://openweathermap.org/data/2.5/weather?q=" + city + "&appid=b6907d289e10d714a6e88b30761fae22")
                    .get();

            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("main");

            JSONObject jsonObjectmain = new JSONObject(weatherData);
            temperature = jsonObjectmain.getString("temp");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return temperature;
    }


}

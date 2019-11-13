package br.com.livroandroid.trainingmockup.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.livroandroid.trainingmockup.Activities.HomeActivity;
import br.com.livroandroid.trainingmockup.R;


public class CalendarFragment extends Fragment {

    String temp = "";
    TextView tvTemp,tvTime;
    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        tvTemp = (TextView)v.findViewById(R.id.tvTemperatura);
        tvTime = (TextView)v.findViewById(R.id.tvTime);

        SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm");
        Date data = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        Date data_atual = cal.getTime();
        String hora_atual = dateFormat_hora.format(data_atual);
        Log.i("hora_atual",hora_atual);

        tvTime.setText(hora_atual);

        String content;
        CalendarFragment.Weather weather = new CalendarFragment.Weather();

        try {
            content = weather
                    .execute("https://openweathermap.org/data/2.5/weather?q=Curitiba&appid=b6907d289e10d714a6e88b30761fae22")
                    .get();

            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("main");

            JSONObject jsonObjectmain = new JSONObject(weatherData);
            String temp = jsonObjectmain.getString("temp");

            tvTemp.setText(temp+" °C");

        } catch (Exception e) {
            e.printStackTrace();
        }

                return v;
    }

    class Weather extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... address) {

            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1){
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                Log.i("Content",content);
                return content;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}

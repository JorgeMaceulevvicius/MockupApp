package br.com.livroandroid.trainingmockup.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import br.com.livroandroid.trainingmockup.Fragments.CalendarFragment;
import br.com.livroandroid.trainingmockup.Fragments.MapFragment;
import br.com.livroandroid.trainingmockup.Fragments.PhotoFragment;
import br.com.livroandroid.trainingmockup.R;

public class HomeActivity extends AppCompatActivity {

    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        search = (EditText) findViewById(R.id.edtSearch);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new PhotoFragment()).commit();
        search.setVisibility(View.INVISIBLE);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){

                    case R.id.photo:

                        selectedFragment = new PhotoFragment();
                        search.setVisibility(View.INVISIBLE);
                        break;

                    case R.id.calendar:

                        selectedFragment = new CalendarFragment();
                        search.setVisibility(View.INVISIBLE);
                        break;

                    case R.id.maps:

                        selectedFragment = new MapFragment();
                        search.setVisibility(View.VISIBLE);
                        search.setHint("Search Supermarkets");

                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
            return true;
            }
        });
    }


}

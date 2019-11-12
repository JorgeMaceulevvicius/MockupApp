package br.com.livroandroid.trainingmockup.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.fragment.app.Fragment;
import br.com.livroandroid.trainingmockup.Fragments.CalendarFragment;
import br.com.livroandroid.trainingmockup.Fragments.MapFragment;
import br.com.livroandroid.trainingmockup.Fragments.PhotoFragment;
import br.com.livroandroid.trainingmockup.R;

public class HomeActivity extends AppCompatActivity {

    EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

      search = (EditText) findViewById(R.id.edtSearch);


        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new PhotoFragment()).commit();
        search.setHint("Search the Photo");

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){

                    case R.id.photo:

                        selectedFragment = new PhotoFragment();
                        search.setHint("Search the Photo");
                        break;

                    case R.id.calendar:

                        selectedFragment = new CalendarFragment();
                        search.setHint("Search the Date");
                        break;

                    case R.id.maps:

                        selectedFragment = new MapFragment();
                        search.setHint("Search Location");
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
            return true;
            }
        });
    }


}

package br.com.livroandroid.trainingmockup.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import br.com.livroandroid.trainingmockup.Entities.Market;
import br.com.livroandroid.trainingmockup.Fragments.CalendarFragment;
import br.com.livroandroid.trainingmockup.Fragments.MapFragment;
import br.com.livroandroid.trainingmockup.Fragments.MarketFragment;
import br.com.livroandroid.trainingmockup.Fragments.PhotoFragment;
import br.com.livroandroid.trainingmockup.R;

public class HomeActivity extends AppCompatActivity {

    private EditText search;
    private Button btn_search;
    private String city;
    private DatabaseReference mDatabase;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        search = (EditText) findViewById(R.id.edtSearch);
        btn_search = findViewById(R.id.buttonSearch);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("markets");

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query q = mDatabase.orderByChild("title").equalTo(search.getText().toString());
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                Market mkt = snapshot.getValue(Market.class);

                                Bundle bd = new Bundle();
                                bd.putDouble("Latitude",mkt.getLatitude());
                                bd.putDouble("Longitude",mkt.getLongitude());
                                bd.putString("title",mkt.getTitle());
                                bd.putString("address", mkt.getAdress());
                                Fragment fragment = new MapFragment();
                                fragment.setArguments(bd);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, fragment).commit();
                            }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNav);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        city = cityLocation(location.getLatitude(),location.getLongitude());

        //Toast.makeText(this,city,Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putString("cityLocation",city);

        Fragment fragment = new PhotoFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
        search.setVisibility(View.INVISIBLE);
        btn_search.setVisibility(View.INVISIBLE);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                Fragment selectedFragment = null;
                switch (item.getItemId()){

                    case R.id.photo:

                        selectedFragment = new PhotoFragment();
                        selectedFragment.setArguments(bundle);
                        search.setVisibility(View.INVISIBLE);
                        btn_search.setVisibility(View.INVISIBLE);
                        break;

                    case R.id.calendar:

                        selectedFragment = new CalendarFragment();
                        selectedFragment.setArguments(bundle);
                        search.setVisibility(View.INVISIBLE);
                        btn_search.setVisibility(View.INVISIBLE);
                        break;

                    case R.id.maps:

                        selectedFragment = new MapFragment();
                        search.setVisibility(View.VISIBLE);
                        search.setHint("Search Supermarkets");
                        btn_search.setVisibility(View.VISIBLE);

                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
            return true;
            }
        });

    }

    private String cityLocation(double lat, double lon) {

        String cityName = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(lat,lon,10);
            if(addresses.size()>0){
                for (Address adr:addresses){
                    if (adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getLocality();
                        break;
                    }

                }

            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return cityName;
    }


}

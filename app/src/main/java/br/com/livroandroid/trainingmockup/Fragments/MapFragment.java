package br.com.livroandroid.trainingmockup.Fragments;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import br.com.livroandroid.trainingmockup.Entities.Card;
import br.com.livroandroid.trainingmockup.Entities.Market;
import br.com.livroandroid.trainingmockup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{


    private DatabaseReference mDatabase;
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    private List<Market> markets;
    private double latitude, longitude;
    private String title, address;
    CameraPosition startLocation;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            latitude = getArguments().getDouble("Latitude");
            longitude = getArguments().getDouble("Longitude");
            title = getArguments().getString("title");
            address = getArguments().getString("address");

           // Toast.makeText(getContext(),latitude+"  +  "+longitude,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("markets");

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.mapView);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if(latitude != 0 && longitude != 0){

            startLocation = CameraPosition.builder().target(new LatLng(latitude, longitude)).zoom(17).bearing(0).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(startLocation));
            LatLng location = new LatLng(latitude,longitude);
            mGoogleMap.addMarker(new MarkerOptions().position(location).title(title)).setSnippet(address);
        }else {
             startLocation = CameraPosition.builder().target(new LatLng(-25.431672, -49.278474)).zoom(3).bearing(0).build();
        }

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(startLocation));

        MapsInitializer.initialize(getContext());



        markets = new ArrayList<>();

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = googleMap.getCameraPosition();

                    mDatabase.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(cameraPosition.zoom > 13) {

                                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                                    Market m = postSnapshot.getValue(Market.class);
                                    markets.add(m);
                                    LatLng location = new LatLng(m.getLatitude(),m.getLongitude());
                                    mGoogleMap.addMarker(new MarkerOptions().position(location).title(m.getTitle())).setSnippet(m.getAdress());
                                }


                            }else {
                                mGoogleMap.clear();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            mGoogleMap.clear();
                        }
                    });

                }

        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // Toast.makeText(getContext(),"Clicked " + m.getTitle(),Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("Title",marker.getTitle());
                MarketFragment mf = new MarketFragment();
                mf.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,mf);
                fragmentTransaction.commit();


            }
        });


    }


}

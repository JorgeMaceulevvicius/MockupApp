package br.com.livroandroid.trainingmockup.Fragments;


import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import androidx.fragment.app.FragmentActivity;
import br.com.livroandroid.trainingmockup.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    private Marker currentLocationMarker;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_map, container, false);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView)mView.findViewById(R.id.mapView);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = googleMap.getCameraPosition();
                if(cameraPosition.zoom > 13) {
                    mGoogleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(-25.431672, -49.278474))
                            .title("Near My House")
                            .snippet("Location Test")
                            .visible(true)
                    );
                }
            }
        });


        CameraPosition SouthAmerica = CameraPosition.builder().target(new LatLng(-25.431672, -49.278474)).zoom(3).bearing(0).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(SouthAmerica));
    }


}

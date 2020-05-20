package com.example.restourantapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.restourantapp.Model.Restaurants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ReserveFragment extends Fragment implements OnMapReadyCallback {
    private final String API_KEY = "AIzaSyBnFQiwyy26b4_7hdlmhdrA5YwOUtQm3Oc";
    public MapView mapView;
    private TextView restaurantAddress, resPhoneNumber, working_time, paymentOptions, parking, dressCode, description;
    private FirebaseDatabase database;
    private DatabaseReference restaurant;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("RestaurantReserve");
        getDetailRestaurant();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reserve, container, false);
        mapView = view.findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);
        initFields(view);
        return view;
    }

    private void getDetailRestaurant() {
        restaurant.child("Vidovdan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restaurants restaurants = dataSnapshot.getValue(Restaurants.class);
                restaurantAddress.setText(restaurants.getAddress());
                resPhoneNumber.setText(restaurants.getPhone());
                parking.setText(restaurants.getParking());
                description.setText(restaurants.getDescription());
                dressCode.setText(restaurants.getDressCode());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initFields(View v) {
        restaurantAddress = v.findViewById(R.id.restaurantAddress);
        resPhoneNumber = v.findViewById(R.id.restaurantPhone);
        working_time = v.findViewById(R.id.working_time);
        paymentOptions = v.findViewById(R.id.payment_options);
        parking = v.findViewById(R.id.parking);
        dressCode = v.findViewById(R.id.dressCode);
        description = v.findViewById(R.id.description);


    }

    public void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(API_KEY);

        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);


    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(API_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(API_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}

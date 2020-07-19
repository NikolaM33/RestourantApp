package com.example.restourantapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.restourantapp.Model.Reservations;
import com.example.restourantapp.Model.Restaurants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ReserveFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "RESERVE FRAGMENT";
    private final String API_KEY = "google key";
    public MapView mapView;
    private TextView restaurantAddress, resPhoneNumber, working_time, paymentOptions, parking, dressCode, description;
    private FirebaseDatabase database;
    private DatabaseReference restaurant, reservations;
    private FirebaseAuth mAuth;

    private Button btnReserve, btnConfirm;
    private TextInputLayout reserveInputLayout;
    private String date, time;

    private int dateDay, timeHour;
    private String numberOfPerson;
    private String name, image, resName;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("RestaurantReserve");
        reservations = database.getReference("Reservations");
        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reserve, container, false);
        mapView = view.findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);
        initFields(view);
        RestaurantDetails activity = (RestaurantDetails) getActivity();
        Bundle results = activity.getMyData();
        resName = results.getString("name");
        Log.i(TAG, "onCreateView: " + results.getString("name"));
        final String restaurantName = resName;
        getDetailRestaurant(resName);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    if ((dateDay == Calendar.DAY_OF_MONTH && timeHour <= Calendar.HOUR_OF_DAY) || timeHour < 12 || timeHour > 21) {
                        Toast.makeText(getActivity(), "Error wrong date or time", Toast.LENGTH_LONG).show();
                    } else {
                        String reserveID = String.valueOf(System.currentTimeMillis());
                        DateFormat df = new SimpleDateFormat(" dd MMM yyyy, HH:mm");
                        String dateAndTime = df.format(Calendar.getInstance().getTime());

                        Reservations reservation = new Reservations(dateAndTime, restaurantName, mAuth.getCurrentUser().getUid(),
                                date, time, numberOfPerson, reserveID);

                        reservations.child(reserveID).setValue(reservation);
                        Toast.makeText(getActivity(), "Reservation was placed successfully", Toast.LENGTH_LONG).show();
                        reserveInputLayout.setVisibility(View.GONE);
                        btnConfirm.setVisibility(View.GONE);
                    }
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                }
            }
        });


        btnReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateAndTimePicker();

            }
        });
        Bundle bun = new Bundle();
        resName = bun.getString("resName");

        return view;

    }

    public void dateAndTimePicker() {
        reserveInputLayout.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.VISIBLE);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View layout_dateAndTime = inflater.inflate(R.layout.date_time_picker_layout, null);
        Button btnSetReservation = layout_dateAndTime.findViewById(R.id.btnSetReservation);
        TextView txtSetPerson = layout_dateAndTime.findViewById(R.id.txtPerson);
        final DatePicker datePicker = layout_dateAndTime.findViewById(R.id.datePicker);
        final TimePicker timePicker = layout_dateAndTime.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        final ElegantNumberButton numberButton = layout_dateAndTime.findViewById(R.id.elegantNumberButton);
        datePicker.setMinDate(System.currentTimeMillis() - 100000);
        Calendar c = Calendar.getInstance();


        c.add(Calendar.MONTH, +1);
        long oneMonthAhead = c.getTimeInMillis();
        datePicker.setMaxDate(oneMonthAhead);

        alertDialog.setView(layout_dateAndTime);

        // final int d=c.get(Calendar.DAY_OF_MONTH);
        final AlertDialog show = alertDialog.show();


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay > 21) {
                    timePicker.setHour(21);
                } else if (hourOfDay < 12) {
                    timePicker.setHour(12);
                }
            }
        });
        btnSetReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberOfPerson = numberButton.getNumber();
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth() + 1;
                int year = datePicker.getYear();
                dateDay = day;
                date = day + "." + month + "." + year;
                int i = Calendar.getInstance().get(Calendar.HOUR);
                if (timePicker.getHour() > 21 && timePicker.getMinute() > 0) {
                    time = 21 + ":" + 0 + 0;
                    reserveInputLayout.setError("Cannot reserve table after 21h");
                } else if (timePicker.getHour() < 12 && timePicker.getMinute() > 0) {
                    time = 12 + ":" + 0 + 0;
                    reserveInputLayout.setError("Cannot reserve table before 12h");
                } else {
                    time = timePicker.getHour() + ":" + timePicker.getMinute();
                    reserveInputLayout.setErrorEnabled(false);
                }
                timeHour = timePicker.getHour();

                numberOfPerson = numberButton.getNumber();
                Log.i(TAG, "onClick: " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + Calendar.getInstance().get(Calendar.HOUR));
                reserveInputLayout.getEditText().setText("Table for " + numberButton.getNumber() + " on " + date + " at " + time);
                show.dismiss();

            }
        });
    }

    private void getDetailRestaurant(String resName) {
        restaurant.child(resName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Restaurants restaurants = dataSnapshot.getValue(Restaurants.class);
                restaurantAddress.setText(restaurants.getAddress());
                resPhoneNumber.setText(restaurants.getPhone());
                parking.setText("    " + restaurants.getParking());
                description.setText("    " + restaurants.getDescription());
                dressCode.setText(restaurants.getDressCode());
                image = restaurants.getImage();
                name = restaurants.getName();
                paymentOptions.setText("    " + restaurants.getPaymentOptions());
                working_time.setText("    " + restaurants.getWorking_time());

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
        btnReserve = v.findViewById(R.id.btnReserve);
        reserveInputLayout = v.findViewById(R.id.layoutReservation_time);
        btnConfirm = v.findViewById(R.id.btnConfirm);



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
        googleMap.addMarker(new MarkerOptions().position(new LatLng(42.896802, 20.867614)).title("Duo Grill"));

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

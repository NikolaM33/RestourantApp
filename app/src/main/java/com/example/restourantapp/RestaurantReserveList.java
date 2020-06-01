package com.example.restourantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restourantapp.Interface.ItemClickListener;
import com.example.restourantapp.Model.Restaurants;
import com.example.restourantapp.view.RestaurantReserveViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RestaurantReserveList extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference restaurantReserve;
    private RecyclerView recycler_restaurantsReserve;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<Restaurants, RestaurantReserveViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_reserve_list);

        //init Firebase
        database = FirebaseDatabase.getInstance();
        restaurantReserve = database.getReference("RestaurantReserve");

        //Load restaurants
        recycler_restaurantsReserve = findViewById(R.id.recyclerReserve);
        recycler_restaurantsReserve.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_restaurantsReserve.setLayoutManager(layoutManager);
        loadRestaurantReserve();
    }

    private void loadRestaurantReserve() {
        adapter = new FirebaseRecyclerAdapter<Restaurants, RestaurantReserveViewHolder>(Restaurants.class, R.layout.restaurant_reserve_item, RestaurantReserveViewHolder.class, restaurantReserve) {
            @Override
            protected void populateViewHolder(RestaurantReserveViewHolder restaurantReserveViewHolder, Restaurants restaurants, int i) {

                restaurantReserveViewHolder.resReserveName.setText(restaurants.getName());
                Picasso.with(getBaseContext()).load(restaurants.getImage()).into(restaurantReserveViewHolder.resReserveImage);
                restaurantReserveViewHolder.resReserveDescription.setText(restaurants.getDescription());
                restaurantReserveViewHolder.resReserveAddress.setText(restaurants.getAddress());
                final Restaurants clickItem = restaurants;
                restaurantReserveViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(RestaurantReserveList.this, "" + clickItem.getName(), Toast.LENGTH_LONG).show();
                        Intent toRestaurantDetail = new Intent(RestaurantReserveList.this, RestaurantDetails.class);
                        toRestaurantDetail.putExtra("Name", clickItem.getName());
                        toRestaurantDetail.putExtra("image", clickItem.getImage());

                        startActivity(toRestaurantDetail);
                    }
                });
            }


        };
        recycler_restaurantsReserve.setAdapter(adapter);
    }
}

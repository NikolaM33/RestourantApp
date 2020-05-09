package com.example.restourantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.restourantapp.Interface.ItemClickListener;
import com.example.restourantapp.Model.RestaurantsOrdering;
import com.example.restourantapp.view.RestaurantViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RestaurantsList extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference restaurants;
    private RecyclerView recycler_restaurants;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseRecyclerAdapter<RestaurantsOrdering, RestaurantViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restourant_list);

        //init Firebase
        database = FirebaseDatabase.getInstance();
        restaurants = database.getReference("Restourant");

        //Load restaurants
        recycler_restaurants = findViewById(R.id.recycler_restaurants);
        recycler_restaurants.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_restaurants.setLayoutManager(layoutManager);
        loadRestaurants();

    }

    private void loadRestaurants() {
        adapter = new FirebaseRecyclerAdapter<RestaurantsOrdering, RestaurantViewHolder>(RestaurantsOrdering.class, R.layout.restaurant_item, RestaurantViewHolder.class, restaurants) {
            @Override
            protected void populateViewHolder(RestaurantViewHolder restaurantViewHolder, RestaurantsOrdering restaurantsOrdering, int i) {

                restaurantViewHolder.restaurantName.setText(restaurantsOrdering.getName());
                Picasso.with(getBaseContext()).load(restaurantsOrdering.getImage()).into(restaurantViewHolder.restaurantImage);
                final RestaurantsOrdering clickItem = restaurantsOrdering;
                restaurantViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(RestaurantsList.this, "" + clickItem.getName(), Toast.LENGTH_LONG).show();
                        Intent foodList = new Intent(RestaurantsList.this, FoodList.class);
                        foodList.putExtra("RestaurantName", clickItem.getName());
                        startActivity(foodList);
                    }
                });
            }
        };
        recycler_restaurants.setAdapter(adapter);

    }


}

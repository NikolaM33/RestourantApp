package com.example.restourantapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.restourantapp.Interface.ItemClickListener;
import com.example.restourantapp.Model.Food;
import com.example.restourantapp.view.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {
    private static final String TAG = "FOODlIST";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference foodList;
    private String restaurantName;
    private FloatingActionButton btnOpenCart;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Where to eat?");

        //firebase

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Restourant");
        mAuth = FirebaseAuth.getInstance();

        swipeRefreshLayout = findViewById(R.id.swipeFood_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //get Intent here
                if (getIntent() != null) {
                    restaurantName = getIntent().getStringExtra("RestaurantName");
                    if (!restaurantName.isEmpty() && restaurantName != null) {
                        Log.i(TAG, "onCreate: " + restaurantName);
                        loadListFood(restaurantName);
                    }

                }
            }
        });
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //get Intent here
                if (getIntent() != null) {
                    restaurantName = getIntent().getStringExtra("RestaurantName");
                    if (!restaurantName.isEmpty() && restaurantName != null) {
                        Log.i(TAG, "onCreate: " + restaurantName);
                        loadListFood(restaurantName);
                    }

                }
            }
        });

        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        // layoutManager = new LinearLayoutManager(this);
        // recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        btnOpenCart = findViewById(R.id.btnOpenCart);
        //get Intent here
        if (getIntent() != null) {
            restaurantName = getIntent().getStringExtra("RestaurantName");
            if (!restaurantName.isEmpty() && restaurantName != null) {
                Log.i(TAG, "onCreate: " + restaurantName);
                loadListFood(restaurantName);
            }

        }

        btnOpenCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(FoodList.this, Cart.class);
                startActivity(cartIntent);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.standard_menu, menu);
        MenuItem btnUser = menu.findItem(R.id.btnUserProfile);
        btnUser.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseUser user = mAuth.getCurrentUser();
                Log.i(TAG, "onMenuItemClick: " + user);
                if (user != null) {
                    startActivity(new Intent(FoodList.this, UserProfile.class));
                    return false;
                } else {
                    startActivity(new Intent(FoodList.this, LoginActivity.class));
                    return false;
                }
            }
        });

        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                startActivity(new Intent(FoodList.this, RestaurantsList.class));
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void loadListFood(final String restaurantName) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, foodList.child(restaurantName).child("Food")) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.foodName.setText(food.getName());
                Log.i(TAG, "populateViewHolder: " + food.getName());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolder.foodImage);
                final Food local = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(FoodList.this, "" + local.getName(), Toast.LENGTH_SHORT).show();
                        Intent foodDetails = new Intent(getApplicationContext(), FoodDetails.class);
                        foodDetails.putExtra("FoodName", local.getName());
                        foodDetails.putExtra("RestaurantID", restaurantName);
                        Log.i(TAG, "onClick: " + local.getName());
                        startActivity(foodDetails);

                    }
                });

            }
        };
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);


    }
}

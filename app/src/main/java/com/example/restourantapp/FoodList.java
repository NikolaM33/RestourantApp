package com.example.restourantapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.restourantapp.Databases.Database;
import com.example.restourantapp.Interface.ItemClickListener;
import com.example.restourantapp.Model.Food;
import com.example.restourantapp.view.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {
    private static final String TAG = "FOODlIST";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference foodList;
    private String restaurantName;
    private CounterFab btnOpenCart;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //firebase

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Restourant");

        recyclerView = findViewById(R.id.recycler_food);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        btnOpenCart =  findViewById(R.id.btnOpenCart);
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
        btnOpenCart.setCount(new Database(this).getCountCart);






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

    }
    protected void onResume() {

        super.onResume();
        btnOpenCart.setCount(new Database(this).getCountCart);

    }


}

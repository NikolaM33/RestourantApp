package com.example.restourantapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.restourantapp.Databases.Database;
import com.example.restourantapp.Model.Food;
import com.example.restourantapp.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetails extends AppCompatActivity {

    private TextView foodName, foodPrice, foodDescription;
    private ImageView food_Image;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton btnCart;
    private ElegantNumberButton numberButton;
    private String foodNameID, restaurantID;

    private FirebaseDatabase database;
    private DatabaseReference foods;
    private Food currentFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Restourant");

        numberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);

        foodName = findViewById(R.id.food_name);
        foodPrice = findViewById(R.id.food_price);
        foodDescription = findViewById(R.id.food_description);
        food_Image = findViewById(R.id.img_food);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingAppbar);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        foodNameID,
                        numberButton.getNumber(),
                        currentFood.getPrice()


                ));
                Toast.makeText(FoodDetails.this, "Added to cart", Toast.LENGTH_LONG).show();
            }
        });

        //Get food name from Intent
        if (getIntent() != null) {
            foodNameID = getIntent().getStringExtra("FoodName");
            restaurantID = getIntent().getStringExtra("RestaurantID");


        }
        if (!foodNameID.isEmpty() && foodNameID != null && !restaurantID.isEmpty() && restaurantID != null) {
            String TAG = "FOODDETAILS";
            Log.i(TAG, "onCreate: " + foodNameID);
            getDetailFood(foodNameID, restaurantID);


        }


    }

    private void getDetailFood(String foodNameID, String restaurantID) {
        foods.child(restaurantID).child("Food").child(foodNameID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(food_Image);
                foodName.setText(currentFood.getName());

                foodPrice.setText(currentFood.getPrice());

                foodDescription.setText(currentFood.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(FoodDetails.this, "" + databaseError.getMessage(), Toast.LENGTH_LONG);


            }
        });

    }
}

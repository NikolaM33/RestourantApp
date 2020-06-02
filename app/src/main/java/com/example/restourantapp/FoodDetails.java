package com.example.restourantapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.restourantapp.Databases.Database;
import com.example.restourantapp.Model.Addon;
import com.example.restourantapp.Model.Food;
import com.example.restourantapp.Model.Order;
import com.example.restourantapp.Model.RatingFood;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class FoodDetails extends AppCompatActivity implements RatingDialogListener {
    private final static String TAG = "FOODDETAILS";
    private TextView foodName, foodPrice, foodDescription;
    RatingFood re;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView food_Image, imgAddon;
    private ElegantNumberButton numberButton;
    private FloatingActionButton btnCart, btnRating;
    private String foodNameID, restaurantID, restaurantIDFoodID;
    private RatingBar ratingBar;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference foods, rating;
    private Food currentFood;
    private ChipGroup chipGroup, chipGroup_addon;
    private EditText edtSearch;
    private BottomSheetDialog addonBottomSheetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        getSupportActionBar().setTitle("Where to eat?");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Restourant");
        mAuth = FirebaseAuth.getInstance();
        rating = database.getReference("RatingFood");


        numberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);
        btnRating = findViewById(R.id.btnRating);
        ratingBar = findViewById(R.id.ratingBar);
        foodName = findViewById(R.id.food_name);
        foodPrice = findViewById(R.id.food_price);
        foodDescription = findViewById(R.id.food_description);
        food_Image = findViewById(R.id.img_food);
        imgAddon = findViewById(R.id.img_add_addon);
        chipGroup = findViewById(R.id.chip_group);
        initView();
        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingAppbar);
        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });
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
            getRatingFood(foodNameID, restaurantID);


        }
        //  imgAddon.setOnClickListener(new View.OnClickListener() {

        //    @Override
        //   public void onClick(View v) {
        //         displayAddonList();
        //    addonBottomSheetDialog.show();
        //   if (currentFood.getAddon()!=null){
        //    displayAddonList();
        //      addonBottomSheetDialog.show();
        // }
        // }
        //  });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.standard_menu, menu);
        MenuItem btnUser = menu.findItem(R.id.btnUserProfile);
        btnUser.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseUser user = mAuth.getCurrentUser();
                //   Log.i(TAG, "onMenuItemClick: "+user);
                if (user != null) {
                    startActivity(new Intent(FoodDetails.this, UserProfile.class));
                    return false;
                } else {
                    startActivity(new Intent(FoodDetails.this, LoginActivity.class));
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
                Intent intent = new Intent(FoodDetails.this, FoodList.class);
                intent.putExtra("RestaurantName", restaurantID);
                startActivity(intent);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void displayAddonList() {
        if (currentFood.getAddon().size() > 0) {
            chipGroup_addon.clearCheck();
            chipGroup_addon.removeAllViews();
            for (Addon addonModel : currentFood.getAddon()) {

                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.layout_addon_item, null);
                chip.setText(new StringBuilder(addonModel.getName()).append("(+$")
                        .append(addonModel.getPrice()).append(")"));
                chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            currentFood.setAddon(new ArrayList<Addon>());

                        }
                    }
                });
                chipGroup_addon.addView(chip);

            }

            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    chipGroup_addon.clearCheck();
                    chipGroup_addon.removeAllViews();
                    for (Addon addonModel : currentFood.getAddon()) {
                        if (addonModel.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.layout_addon_item, null);
                            chip.setText(new StringBuilder(addonModel.getName()).append("(+$")
                                    .append(addonModel.getPrice()).append(")"));
                            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (isChecked) {
                                        currentFood.setAddon(new ArrayList<Addon>());

                                    }
                                }
                            });
                            chipGroup_addon.addView(chip);
                        }
                    }


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    private void getRatingFood(String foodNameID, String restaurantID) {
        String id = restaurantID + "_" + foodNameID;
        Query foodRating = rating.orderByChild("restaurantIDFoodID").equalTo(id);

        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RatingFood item = postSnapshot.getValue(RatingFood.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;

                }
                if (count != 0) {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder().setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite Ok", "Very Good", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this Food")
                .setDescription("Please select some starts and give your feedback")
                .setTitleTextColor(R.color.color2)
                .setDescriptionTextColor(R.color.color2)
                .setHint("Please write your comment here...")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetails.this)
                .show();

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

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        //get rating and upload to Firebase
        final String ratingID = String.valueOf(System.currentTimeMillis());
        restaurantIDFoodID = restaurantID + "_" + foodNameID;
        final String userID = mAuth.getCurrentUser().getUid();
        String userResName = userID + "_" + restaurantIDFoodID;
        //  checkIsRatingExists(userID,foodNameID,restaurantID);
        final RatingFood ratingFood = new RatingFood(userID, ratingID, String.valueOf(i), s, restaurantIDFoodID, userResName);
        rating.child(ratingID).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //re=dataSnapshot.getValue(RatingFood.class);
                String rati = re.getRatingID();
                String value = re.getRateValue();
                Log.i(TAG, "onDataChange: " + rati + value);
                if ((dataSnapshot.child(ratingID).exists())) {
                    //Remove old value
                    rating.child(ratingID).removeValue();
                    //Update new value
                    rating.child(ratingID).setValue(ratingFood);
                } else {
                    rating.child(ratingID).setValue(ratingFood);

                }
                Toast.makeText(FoodDetails.this, "Thank you for submit rating !!!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initView() {
        addonBottomSheetDialog = new BottomSheetDialog(getBaseContext(), R.style.DialogStyle);
        View layout_addon_display = getLayoutInflater().inflate(R.layout.layout_addon_displey, null);
        chipGroup_addon = layout_addon_display.findViewById(R.id.chip_group_addon);
        edtSearch = layout_addon_display.findViewById(R.id.edt_search);
        addonBottomSheetDialog.setContentView(layout_addon_display);

        addonBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //displayUserSelectedAddon ();


            }
        });

    }

    private void displayUserSelectedAddon() {
        chipGroup.removeAllViews();
        for (final Addon addonModel : currentFood.getAddon()) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.layout_chip_with_delete_icon, null);
            chip.setText(new StringBuilder(addonModel.getName()).append("(+$")
                    .append(addonModel.getPrice()).append(")"));
            chip.setClickable(false);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Remove when user select delete
                    chipGroup.removeView(v);
                    currentFood.getAddon().remove(addonModel);
                }
            });
            chipGroup.addView(chip);
        }

    }

    private void checkIsRatingExists(String userID, String foodName, String restaurantName) {
        String check = userID + "_" + restaurantName + "_" + foodName;

        Query checkRating = rating.orderByChild("userResFood").equalTo(check);


    }


}

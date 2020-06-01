package com.example.restourantapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.restourantapp.view.ViewPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class RestaurantDetails extends AppCompatActivity {
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageButton userProfileButton;
    private TextView restaurantName;
    private ImageView restaurantImage;
    private FirebaseDatabase database;
    private DatabaseReference restaurant;
    private String name, image;
    private FirebaseAuth mAuth;

    public String getName() {
        return name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Where to eat?");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_restaurant_details);
        tabLayout = findViewById(R.id.tabLayout);
        appBarLayout = findViewById(R.id.appbar);
        viewPager = findViewById(R.id.ViewPager);
        restaurantName = findViewById(R.id.restaurantDetailName);
        restaurantImage = findViewById(R.id.app_bar_image);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingAppbar);

        database = FirebaseDatabase.getInstance();
        restaurant = database.getReference("RestaurantReserve");
        mAuth = FirebaseAuth.getInstance();






        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //ADDING FRAGMENTS
        adapter.addFragment(new ReserveFragment(), "RESERVE");
        adapter.addFragment(new MenuFragment(), "MENU");
        adapter.addFragment(new ReviewsFragment(), "REVIEWS");

        //ADAPTER SETUP
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        if (getIntent() != null) {
            name = getIntent().getStringExtra("Name");
            image = getIntent().getStringExtra("image");

        }
        if (!name.isEmpty() && name != null && !image.isEmpty() && image != null) {


            getNameAndImage(name, image);
        }











    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.standard_menu, menu);
        MenuItem btnUser = menu.findItem(R.id.btnUserProfile);
        btnUser.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                FirebaseUser user = mAuth.getCurrentUser();
                //  Log.i(TAG, "onMenuItemClick: "+user);
                if (user != null) {
                    startActivity(new Intent(RestaurantDetails.this, UserProfile.class));
                    return false;
                } else {
                    startActivity(new Intent(RestaurantDetails.this, LoginActivity.class));
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
                startActivity(new Intent(RestaurantDetails.this, RestaurantReserveList.class));
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public Bundle getMyData() {
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        return bundle;


    }

    private void getNameAndImage(String name, String image) {


        Picasso.with(getBaseContext()).load(image).into(restaurantImage);
        restaurantName.setText(name);



    }
}

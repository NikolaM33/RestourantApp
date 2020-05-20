package com.example.restourantapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.example.restourantapp.view.ViewPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;

public class RestaurantDetails extends AppCompatActivity {
    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageButton userProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        tabLayout = findViewById(R.id.tabLayout);
        appBarLayout = findViewById(R.id.appbar);
        viewPager = findViewById(R.id.ViewPager);
        collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingAppbar);
        userProfileButton = findViewById(R.id.userButton);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        //ADDING FRAGMENTS
        adapter.addFragment(new ReserveFragment(), "RESERVE");
        adapter.addFragment(new MenuFragment(), "MENU");
        adapter.addFragment(new ReviewsFragment(), "REVIEWS");

        //ADAPTER SETUP
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        userProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateAndTimePicker();
            }
        });


    }

    public void dateAndTimePicker() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RestaurantDetails.this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_dateAndTime = inflater.inflate(R.layout.date_time_picker_layout, null);
        alertDialog.setView(layout_dateAndTime);

        alertDialog.show();


    }
}

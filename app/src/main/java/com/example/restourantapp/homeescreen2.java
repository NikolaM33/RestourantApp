package com.example.restourantapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class homeescreen2 extends AppCompatActivity {

    private  Button btn_order , btn_reserve;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeescreen2);

        btn_order=findViewById(R.id.btnOrder);
        btn_reserve=findViewById(R.id.btnReserve);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeescreen2.this,RestaurantsList.class);
                startActivity(intent);
            }
        });

        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(homeescreen2.this,RestaurantReserveList.class);
                startActivity(intent);
            }
        });





    }




}

package com.example.restourantapp.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restourantapp.Interface.ItemClickListener;
import com.example.restourantapp.R;

public class RestaurantReserveViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView resReserveName, resReserveDescription, resReserveAddress;
    public ImageView resReserveImage;
    private ItemClickListener itemClickListener;


    public RestaurantReserveViewHolder(@NonNull View itemView) {
        super(itemView);
        resReserveName = itemView.findViewById(R.id.reserveRestaurantName);
        resReserveDescription = itemView.findViewById(R.id.reserveRestaurantDescription);
        resReserveAddress = itemView.findViewById(R.id.reserveRestaurantAddress);
        resReserveImage = itemView.findViewById(R.id.restaurantReserveImage);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);


    }
}

package com.example.restourantapp.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restourantapp.Interface.ItemClickListener;
import com.example.restourantapp.R;

public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView restaurantName;
    public ImageView restaurantImage;
    private ItemClickListener itemClickListener;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        restaurantName = itemView.findViewById(R.id.restaurant_name);
        restaurantImage = itemView.findViewById(R.id.restaurant_image);

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

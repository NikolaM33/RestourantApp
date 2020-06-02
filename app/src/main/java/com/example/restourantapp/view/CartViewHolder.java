package com.example.restourantapp.view;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restourantapp.Interface.ItemClickListener;
import com.example.restourantapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnCreateContextMenuListener{

        public TextView txt_cart_name, txt_price;
        public ImageView img_cart_count;
        private ItemClickListener itemClickListener;
        public RelativeLayout view_background;
        public LinearLayout view_foreground;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_cart_name = itemView.findViewById(R.id.cart_item_name);
            txt_price = itemView.findViewById(R.id.cart_item_Price);
            img_cart_count = itemView.findViewById(R.id.cart_item_count);

            view_background=(RelativeLayout)itemView.findViewById(R.id.view_bacground);
            view_foreground=(LinearLayout)itemView.findViewById(R.id.view_foreground);

            itemView.setOnCreateContextMenuListener(this);

        }

        public void setTxt_cart_name(TextView txt_cart_name) {
            this.txt_cart_name = txt_cart_name;
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select action");
        }
    }



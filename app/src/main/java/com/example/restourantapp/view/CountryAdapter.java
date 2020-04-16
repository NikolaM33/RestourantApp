package com.example.restourantapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restourantapp.R;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<CountrySpinner> {

    public CountryAdapter(Context context, ArrayList<CountrySpinner> countryList) {
        super(context, 0, countryList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_layout, parent, false

            );
        }
        ImageView imageViewFlag = convertView.findViewById(R.id.image_flag);
        TextView textViewNumber = convertView.findViewById(R.id.phone_code);

        CountrySpinner currentItem = getItem(position);
        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getFlagImage());
            textViewNumber.setText(currentItem.getCountryCode());
        }
        return convertView;
    }
}

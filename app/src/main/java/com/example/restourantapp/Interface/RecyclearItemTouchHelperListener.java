package com.example.restourantapp.Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclearItemTouchHelperListener {

    void  onSwiped(RecyclerView.ViewHolder viewHolder,int direction, int position);
}

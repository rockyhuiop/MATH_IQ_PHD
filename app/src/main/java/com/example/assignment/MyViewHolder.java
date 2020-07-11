package com.example.assignment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    protected TextView [] txtItem;
    protected LinearLayout container;
    public MyViewHolder(@NonNull View itemView){
        super(itemView);
        container = itemView.findViewById(R.id.itemContainer);
        txtItem = new TextView[4];
        txtItem[0] = itemView.findViewById(R.id.ItemPlayerName);
        txtItem[1] = itemView.findViewById(R.id.ItemDate);
        txtItem[2] = itemView.findViewById(R.id.ItemDuration);
        txtItem[3] = itemView.findViewById(R.id.ItemCorrectCount);

    }

}
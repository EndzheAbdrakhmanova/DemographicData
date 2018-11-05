package com.example.endzhe.rertofit.recyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.endzhe.rertofit.R;

class RoomViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewDob, textViewDate, textViewSex, textViewCountry, textViewRank;
    public ImageView imageViewDelete;

    public RoomViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewCountry = (TextView) itemView.findViewById(R.id.textViewCountry);
        textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
        textViewSex = (TextView) itemView.findViewById(R.id.textViewSex);
        textViewDob = (TextView) itemView.findViewById(R.id.textViewDob);
        textViewRank = (TextView) itemView.findViewById(R.id.textViewRank);
        imageViewDelete = (ImageView) itemView.findViewById(R.id.imageViewDelete);

    }
}
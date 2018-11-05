package com.example.endzhe.rertofit.recyclerView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.endzhe.rertofit.R;
import com.example.endzhe.rertofit.database.AppDatabase;
import com.example.endzhe.rertofit.database.DataDao;
import com.example.endzhe.rertofit.database.DataModel;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomViewHolder> {
    private List<DataModel> dataModelList;
    private Context context;
    AppDatabase db;

    public RoomAdapter(List<DataModel> dataModelList, Context context) {
        this.dataModelList = dataModelList;
        this.context = context;
    }


    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, viewGroup, false);
        RoomViewHolder viewHolder = new RoomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder roomViewHolder, int position) {
        DataModel dataModel = dataModelList.get(position);

        roomViewHolder.textViewDob.setText(dataModel.getDob());
        roomViewHolder.textViewSex.setText(dataModel.getSex());
        roomViewHolder.textViewCountry.setText(dataModel.getCountry());
        roomViewHolder.textViewDate.setText(dataModel.getDate());

        roomViewHolder.textViewRank.setText(dataModel.getRank());

        roomViewHolder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete a message with the following id
                DataDao messageDao = (DataDao) AppDatabase.getInstance(context).dataDao();
                messageDao.deleteDataModel(dataModel);
                dataModelList.remove(dataModel);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

}

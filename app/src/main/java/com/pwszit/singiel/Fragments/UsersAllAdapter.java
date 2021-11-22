package com.pwszit.singiel.Fragments;


/**
 * Created by ABC on 02-09-2016.
 */


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pwszit.singiel.ChatRoomActivity;
import com.pwszit.singiel.R;

import java.util.ArrayList;

public class UsersAllAdapter extends RecyclerView.Adapter<UsersAllAdapter.MyViewHolder> {

    ImageLoader imageLoader;
    private ArrayList<UsersAllObject> dataSet;
    Activity mActivity;

    Boolean check=false;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        NetworkImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.name=(TextView) itemView.findViewById(R.id.name);
            imageView =(NetworkImageView)itemView.findViewById(R.id.photo);

        }
    }

    public UsersAllAdapter(ArrayList<UsersAllObject> data,Activity activity) {
        this.dataSet = data;
        this.mActivity = activity;

     //   imageLoader = CustomVolleyRequest.getInstance(mActivity.getApplicationContext())
      //          .getImageLoader();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_all_users, parent, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ChatRoomActivity.class);
                intent.putExtra("to_id",dataSet.get(myViewHolder.getAdapterPosition()).getId());
                mActivity.startActivity(intent);
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView name = holder.name;
      //  NetworkImageView imageView = holder.imageView;
      //  imageLoader.get(dataSet.get(listPosition).getPhoto(), ImageLoader.getImageListener(imageView
      //          ,0,android.R.drawable
       //                 .ic_dialog_alert));
       // imageView.setImageUrl(dataSet.get(listPosition).getPhoto(), imageLoader);
        name.setText(dataSet.get(listPosition).getName());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
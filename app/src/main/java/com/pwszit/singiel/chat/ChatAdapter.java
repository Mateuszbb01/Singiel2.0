package com.pwszit.singiel.chat;

import android.content.Context;
import android.graphics.Color;

import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.pwszit.singiel.R;

import java.util.List;

import static com.google.android.material.color.MaterialColors.getColor;


public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder>{
    private List<ChatObject> chatList;
    private Context context;


    public ChatAdapter(List<ChatObject> matchesList, Context context){
        this.chatList = matchesList;
        this.context = context;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recievierchatlayout, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);
        ChatViewHolder rcv = new ChatViewHolder(layoutView);

        return rcv;
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        holder.mMessage.setText(chatList.get(position).getMessage());

        if(chatList.get(position).getCurrentUser()){

            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius(20);
            shape.setCornerRadii(new float[] { 25, 25, 3, 25, 25, 25, 25, 25 });
            shape.setColor(Color.parseColor("#ffc1e3"));
            holder.mContainer.setGravity(Gravity.END);
            holder.mMessage.setBackground(shape);
            holder.mMessage.setTextColor(Color.parseColor("#1B1A1A"));
            //holder.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));
//#5fc9f8

        }else{
            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius(20);
            shape.setColor(Color.parseColor("#ebf5f0"));
            shape.setCornerRadii(new float[] { 25, 3, 25, 25, 25, 25, 25, 25 });
            holder.mMessage.setBackground(shape);
            holder.mContainer.setGravity(Gravity.START);
            holder.mMessage.setTextColor(Color.parseColor("#1B1A1A"));
            //holder.mContainer.setBackgroundColor(Color.parseColor("#F4F4F4"));

            //#53d769
        }

    }

    @Override
    public int getItemCount() {
        return this.chatList.size();
    }
}
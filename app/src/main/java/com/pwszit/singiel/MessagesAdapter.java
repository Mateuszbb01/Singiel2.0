package com.pwszit.singiel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;

    int ITEM_SEND = 1;
    int ITEM_RECIEVE = 2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewmessage;
        TextView timeofmessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewmessage = itemView.findViewById(R.id.sendermessage);
            timeofmessage = itemView.findViewById(R.id.timeofmessage);
        }

        class RecieverViewHolder extends RecyclerView.ViewHolder {

            TextView textViewmessage;
            TextView timeofmessage;

            public RecieverViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewmessage = itemView.findViewById(R.id.sendermessage);
                timeofmessage = itemView.findViewById(R.id.timeofmessage);
            }
        }


    }

}

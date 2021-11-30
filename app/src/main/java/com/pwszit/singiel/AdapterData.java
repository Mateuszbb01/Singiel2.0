package com.pwszit.singiel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {

   List<DataModel> listData;
   LayoutInflater inflater;
    Activity activity;
    private Context context;


    public AdapterData(Context context, List<DataModel> listData) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
        this.context = context;

    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.card_view_all_users, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterData.HolderData holder, int position) {
        holder.name.setText(listData.get(position).getName());
        Picasso.get()
                .load(Constant.URL+"storage/photo/"+ listData.get(position).getPhoto())
                .fit()
                .centerCrop()
                .into(holder.photo);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatMessagingActivity.class);
                intent.putExtra("name", listData.get(position).getName());
                intent.putExtra("id", listData.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();

    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView name;
        ImageView photo;
        LinearLayout mainLayout;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            //photo = itemView.findViewById(R.id.photo);

            mainLayout = itemView.findViewById(R.id.mainLayout);
            name = itemView.findViewById(R.id.name);
            photo = itemView.findViewById(R.id.photo);
        }
    }

}

package com.pwszit.singiel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pwszit.singiel.DataModel;
import com.pwszit.singiel.R;

import java.util.List;

public class AdapterData extends RecyclerView.Adapter<AdapterData.HolderData> {

   List<DataModel> listData;
   LayoutInflater inflater;

    public AdapterData(Context context, List<DataModel> listData) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
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
        holder.photo.setText(listData.get(position).getPhoto());
    }

    @Override
    public int getItemCount() {
        return listData.size();

    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView name;
        TextView photo;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photo);


            name = itemView.findViewById(R.id.name);
        }
    }
}

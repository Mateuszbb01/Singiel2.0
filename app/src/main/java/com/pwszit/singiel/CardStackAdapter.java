package com.pwszit.singiel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private Context context;
    private List<ItemModel> items;

    public CardStackAdapter(List<ItemModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, age, city, userlikedid, comment;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            name = itemView.findViewById(R.id.item_name);
           // age = itemView.findViewById(R.id.item_age);
            city = itemView.findViewById(R.id.item_city);
            userlikedid = itemView.findViewById(R.id.userlikedid);
            comment = itemView.findViewById(R.id.item_comment);
        }

        void setData(ItemModel data) {
            Picasso.get()
                    .load(Constant.URL+"storage/photo/"+ data.getImage())
                    .fit()
                    .centerCrop()
                    .into(image);
            name.setText((data.getName()+", "+(data.getAge())));
            //age.setText(data.getAge());
            city.setText(data.getCity());
            userlikedid.setText(data.getUserlikedid());
            comment.setText(data.getComment());
        }
    }

    public List<ItemModel> getItems() {
        return items;
    }

    public void setItems(List<ItemModel> items) {
        this.items = items;
    }
}
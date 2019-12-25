package com.example.notification.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.DataDrink;
import com.example.notification.R;

import java.util.List;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.ViewHolder> {
    List<DataDrink> drinkList;
    Context context;

    public AdapterNotification(List<DataDrink> drinkList, Context context) {
        this.drinkList = drinkList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_student,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.foodname.setText(drinkList.get(i).getdrinkName());
        viewHolder.price.setText("Rp."+drinkList.get(i).getPrice());
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView foodname,price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodname    = itemView.findViewById(R.id.tvFullName);
            price       = itemView.findViewById(R.id.tvRegNo);
        }
    }
}

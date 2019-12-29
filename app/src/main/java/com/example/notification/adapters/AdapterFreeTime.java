package com.example.notification.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.R;
import com.example.notification.models.ModelFreeTime;

import java.util.List;

public class AdapterFreeTime extends RecyclerView.Adapter<AdapterFreeTime.FreeTimeviewHolder> {

    private Context context;

    private List<ModelFreeTime> freeTimeList;

    public AdapterFreeTime(List<ModelFreeTime>freeTimeList, Context context){
        this.freeTimeList = freeTimeList;
        this.context = context;
    }

    @NonNull
    @Override
    public FreeTimeviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_free_time, parent, false);

        return new FreeTimeviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreeTimeviewHolder holder, int position) {

        String freeDate = freeTimeList.get(position).getFreeDate();
        String startTime = freeTimeList.get(position).getStarttime();
        String endTime = freeTimeList.get(position).getEndTime();

        holder.date.setText(freeDate);
        holder.startTime.setText(startTime);
        holder.endTime.setText(endTime);

    }

    @Override
    public int getItemCount() {
        return freeTimeList.size();
    }

    public class FreeTimeviewHolder extends RecyclerView.ViewHolder {

        TextView date, startTime, endTime;

        public FreeTimeviewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.freeDateTv);
            startTime = itemView.findViewById(R.id.freeStartTime);
            endTime = itemView.findViewById(R.id.freeEndTime);

        }
    }
}

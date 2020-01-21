package com.example.notification.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.models.ModelNotification;
import com.example.notification.models.ModelRequest;
import com.example.notification.models.ModelStudent;
import com.example.notification.R;
import com.example.notification.activities.StudentDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class AdapterNotificationList extends RecyclerView.Adapter<AdapterNotificationList.ViewHolder> {
    private List<ModelNotification> modelNotifications;
    public Context context;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public AdapterNotificationList(List<ModelNotification> modelNotifications, Context context) {
        this.modelNotifications = modelNotifications;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_notification,viewGroup,false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String dateTime = modelNotifications.get(i).getSendDateTime();

        String sender =  "<b>"+ modelNotifications.get(i).getNotifierName()+"</b>" + " added a schedule on "+ "<b>"+ dateTime+"</b>";

        viewHolder.notifier.setText(Html.fromHtml(sender));
        viewHolder.nDate.setText(Html.fromHtml(dateTime));


    }

    @Override
    public int getItemCount() {
        return modelNotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nDate, notifier;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nDate = itemView.findViewById(R.id.notify_date);
            notifier = itemView.findViewById(R.id.notyfier);
        }
    }
}

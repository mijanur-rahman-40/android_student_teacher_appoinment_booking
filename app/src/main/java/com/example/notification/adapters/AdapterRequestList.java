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

import com.example.notification.models.ModelRequest;
import com.example.notification.models.ModelStudent;
import com.example.notification.R;
import com.example.notification.activities.StudentDetailsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class AdapterRequestList extends RecyclerView.Adapter<AdapterRequestList.ViewHolder> {
    private List<ModelRequest> modelRequests;
    public Context context;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public AdapterRequestList(List<ModelRequest> modelRequests, Context context) {
        this.modelRequests = modelRequests;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_request,viewGroup,false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String sender = modelRequests.get(i).getSenderId().equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())?
                "You ": modelRequests.get(i).getRequesterName();

        String receiver = modelRequests.get(i).getReceiverId().equals(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid())?
                "You ": modelRequests.get(i).getReceiverName();

        String reqTo = "<b>"+ sender+"</b>" + " requested for an appointment to "+ "<b>"+ receiver+"</b>";
        String dateTime = "<b>"+ modelRequests.get(i).getAptdate()+"</b>" + " between "+ "<b>"+ modelRequests.get(i).getStartTime()
                +"</b>" + " and "+ "<b>"+ modelRequests.get(i).getEndTime()+"</b>";

        viewHolder.requesterTo.setText(Html.fromHtml(reqTo));
        viewHolder.timeRange.setText(Html.fromHtml(dateTime));


    }

    @Override
    public int getItemCount() {
        return modelRequests.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timeRange, requesterTo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeRange = itemView.findViewById(R.id.timeRange);
            requesterTo = itemView.findViewById(R.id.requester);
        }
    }
}

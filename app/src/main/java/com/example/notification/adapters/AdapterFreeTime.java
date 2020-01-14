package com.example.notification.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.R;
import com.example.notification.activities.PendingRequestActivity;
import com.example.notification.models.ModelFreeTime;
import com.example.notification.models.ModelRequest;
import com.example.notification.models.ModelStudent;
import com.example.notification.models.ModelTeacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AdapterFreeTime extends RecyclerView.Adapter<AdapterFreeTime.FreeTimeviewHolder> {

    private Context context;

    private ModelTeacher modelTeacher, tRequester;
    private ModelStudent stRequester;
    private List<ModelFreeTime> freeTimeList;
    private List<ModelRequest> pendingrequests, acceptedRequests;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbRef;

    private Button applyBtn, cancelBtn, deleteBtn;
    private TextView dateTv, freeTimeTv, maxNoTv, aptDetails, reqApplyValue, accApplyValue;


    public AdapterFreeTime(List<ModelFreeTime>freeTimeList, Context context, ModelTeacher modelTeacher, ModelStudent stRequester, ModelTeacher tRequester){
        this.freeTimeList = freeTimeList;
        this.context = context;
        this.modelTeacher = modelTeacher;
        this.stRequester = stRequester;
        this.tRequester = tRequester;
    }

    public AdapterFreeTime(List<ModelFreeTime> freeTimeList, Context context, ModelTeacher modelTeacher) {
        this.freeTimeList = freeTimeList;
        this.context = context;
        this.modelTeacher = modelTeacher;
    }

    @NonNull
    @Override
    public FreeTimeviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_free_time, parent, false);

        firebaseAuth = FirebaseAuth.getInstance();

        return new FreeTimeviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreeTimeviewHolder holder, final int position) {


        final String freeDate = freeTimeList.get(position).getFreeDate();
        final String startTime = freeTimeList.get(position).getStartTime();
        final String endTime = freeTimeList.get(position).getEndTime();
        final String maxNo = freeTimeList.get(position).getFreeSlot();
        final String owner = freeTimeList.get(position).getOwner();
        final String scheduleId = freeTimeList.get(position).getScheduleID();

        holder.date.setText(freeDate);
        holder.startTime.setText(startTime);
        holder.endTime.setText(endTime);

        holder.model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid().equals(owner)){

                    seeApointmentDeatails(scheduleId,freeDate, startTime, endTime, maxNo);
                } else {
                    applyForAppt(freeDate, startTime, endTime, scheduleId);
                }

            }
        });



    }

    private void retrieveRequests(final String scheduleId, final TextView reqApplyValue, final TextView accApplyValue) {

        pendingrequests = new ArrayList<>();
        acceptedRequests = new ArrayList<>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("requests");



        dbRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                acceptedRequests.clear();
                pendingrequests.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    ModelRequest modelRequest = ds.getValue(ModelRequest.class);

                    assert modelRequest != null;
                    if (Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid().equals(modelRequest.getReceiverId())
                            && scheduleId.equals(modelRequest.getScheduleId())){
                        if (modelRequest.isAccepted())
                        {
                            acceptedRequests.add(modelRequest);
                        } else {
                            pendingrequests.add(modelRequest);
                        }

                    }
                }

                String pendingSize = String.valueOf(pendingrequests.size());
                String accSize = String.valueOf(acceptedRequests.size());

                reqApplyValue.setText(pendingSize);
                accApplyValue.setText(accSize);


                Log.i("CHECK", String.valueOf(pendingrequests.size()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void applyForAppt(final String freeDate, final String startTime, final String endTime, final String scheduleId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            assert inflater != null;
//            View theView = inflater.inflate(R.layout.appointment_details_dialog, null);

        @SuppressLint("InflateParams") View theView = LayoutInflater.from(context).inflate(R.layout.apply_for_apt_dialog, null);

        builder.setView(theView);

        applyBtn = theView.findViewById(R.id.stApply);
        cancelBtn = theView.findViewById(R.id.stCancelBtn);

        aptDetails = theView.findViewById(R.id.aptdetails);

        String details = "<b>"+ modelTeacher.getFullName()+"</b>" + " is free on "+ "<b>"+ freeDate
                +"</b>"+ ", from "+ "<b>"+ startTime+"</b>"+ " to "+ "<b>"+ endTime+"</b>" ;

        aptDetails.setText(Html.fromHtml(details));

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                createRequest(dialog,Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), scheduleId, freeDate, startTime, endTime);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    private void createRequest(final AlertDialog dialog, String curUid, String scheduleId, String aptdate, String startTime, String endTime) {

        String requesterName = null, receiverName;
        if (stRequester!=null){
            requesterName = stRequester.getFullName();

        }

        if (tRequester!=null){
            requesterName = tRequester.getFullName();
        }

        receiverName = modelTeacher.getFullName();

        dbRef = FirebaseDatabase.getInstance().getReference();

        String reqId = dbRef.child("requests").push().getKey();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd  |  hh:mm a");

        Date date = new Date();

        String dateTime = formatter.format(date);


        ModelRequest modelRequest = new ModelRequest(curUid, modelTeacher.getToken(), dateTime, reqId, scheduleId, requesterName, receiverName, aptdate, startTime, endTime, false);

        assert reqId != null;
        dbRef.child("requests").child(reqId).setValue(modelRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        dialog.dismiss();
                        Toast.makeText(context, "You have applied for appointment", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Application Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void seeApointmentDeatails(String scheduleId, String freeDate, String startTime, String endTime, String maxNo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            assert inflater != null;
//            View theView = inflater.inflate(R.layout.appointment_details_dialog, null);

        View theView = LayoutInflater.from(context).inflate(R.layout.appointment_details_dialog, null);

        builder.setView(theView);

        deleteBtn = theView.findViewById(R.id.deleteBtn);
        cancelBtn = theView.findViewById(R.id.apCancelBtn);

        dateTv = theView.findViewById(R.id.apDateValue);
        freeTimeTv = theView.findViewById(R.id.apTimeRangeValue);
        maxNoTv = theView.findViewById(R.id.maxApplyValue);

        reqApplyValue = theView.findViewById(R.id.reqApplyValue);
        accApplyValue = theView.findViewById(R.id.accApplyValue);



        retrieveRequests(scheduleId, reqApplyValue, accApplyValue);


        reqApplyValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeRequestList();
            }
        });
        accApplyValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeAcceptedList();
            }
        });



        dateTv.setText(freeDate);
        freeTimeTv.setText(startTime + " \nto " + endTime);
        maxNoTv.setText(maxNo);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    private void seeAcceptedList() {
        Intent intent = new Intent(context, PendingRequestActivity.class);

        intent.putParcelableArrayListExtra("requestList", (ArrayList) acceptedRequests);

        context.startActivity(intent);
    }

    private void seeRequestList() {

        Intent intent = new Intent(context, PendingRequestActivity.class);

        intent.putParcelableArrayListExtra("requestList", (ArrayList) pendingrequests);

        context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return freeTimeList.size();
    }

    public class FreeTimeviewHolder extends RecyclerView.ViewHolder {

        TextView date, startTime, endTime;
        RelativeLayout model;

        public FreeTimeviewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.freeDateTv);
            startTime = itemView.findViewById(R.id.freeStartTime);
            endTime = itemView.findViewById(R.id.freeEndTime);
            model = itemView.findViewById(R.id.freeTimeModel);
        }


    }
}

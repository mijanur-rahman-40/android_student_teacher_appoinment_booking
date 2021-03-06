package com.example.notification.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.notification.R;
import com.example.notification.activities.ProfileActivity;
import com.example.notification.adapters.AdapterFreeTime;
import com.example.notification.models.ModelFreeTime;
import com.example.notification.models.ModelNotification;
import com.example.notification.models.ModelTeacher;
import com.example.notification.notifications.MySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TeacherOwnSceduleFragment extends Fragment {


    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    NumberPicker numberPicker;

    Calendar cldr;
    int day, month, year, hour, minute;

    private TextView datePick, startTimePick, endTimePick, freeSlot;
    private Button submitBtn;



    private List<ModelFreeTime> freeTimeList;
    private AdapterFreeTime adapterFreeTime;
    private RecyclerView freeTimeRv;
    private ModelTeacher modelTeacher;

    private RelativeLayout rlShow, rlAdd;
    private ImageButton btnAdd, btncancel;

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;


    public TeacherOwnSceduleFragment(){

    }

    public TeacherOwnSceduleFragment(ModelTeacher modelTeacher){
        this.modelTeacher = modelTeacher;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View itemView = inflater.inflate(R.layout.fragmet_owned_teacher, container, false);
        setUpView(itemView);


        setUpView(itemView);

        retrieveMyAppointment();

        setActionOnLayout();

        return itemView;
    }


    private void setUpView(View itemView) {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        freeTimeRv = itemView.findViewById(R.id.freeTimeRv);
        rlAdd = itemView.findViewById(R.id.addRelativeLayout);
        rlShow = itemView.findViewById(R.id.showRelativeLayout);
        btnAdd = itemView.findViewById(R.id.btnAddTime);
        btncancel = itemView.findViewById(R.id.btnMinimizeTime);

        datePick = itemView.findViewById(R.id.dateFreePick);
        startTimePick = itemView.findViewById(R.id.startTimePick);
        endTimePick = itemView.findViewById(R.id.endTimePick);
        freeSlot = itemView.findViewById(R.id.noOfSlot);
        submitBtn = itemView.findViewById(R.id.addBtn);

        cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        hour = cldr.get(Calendar.HOUR_OF_DAY);
        minute = cldr.get(Calendar.MINUTE);



    }

    private void setActionOnLayout() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlAdd.setVisibility(View.VISIBLE);
                rlShow.setVisibility(View.GONE);
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePick.setText("");
                startTimePick.setText("");
                endTimePick.setText("");
                freeSlot.setText("");
                rlAdd.setVisibility(View.GONE);
                rlShow.setVisibility(View.VISIBLE);
            }
        });

        freeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickNumber();
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = datePick.getText().toString();
                String startTime = startTimePick.getText().toString();
                String endTime = endTimePick.getText().toString();
                String slot = freeSlot.getText().toString();
                String owner = user.getUid();

                Log.i("ERROR", date + startTime+ owner);

                if (!date.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()){
                    storeFreeTimeToDatabase(date, startTime, endTime, owner, slot);

                } else {
                    Toast.makeText(getContext(), "These Field Are Required", Toast.LENGTH_SHORT).show();
                }
            }
        });

                datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppointmentDate(datePick);
            }
        });

        startTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppointmentTime(startTimePick);
            }
        });
        endTimePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppointmentTime(endTimePick);
            }
        });


    }


    private void storeFreeTimeToDatabase(final String date, String startTime, String endTime, final String owner, String slot) {

        final String scheduleID = databaseReference.child("freeTimes").push().getKey();

        ModelFreeTime modelFreeTime = new ModelFreeTime(date, startTime, endTime, owner, slot, scheduleID);

        assert scheduleID != null;
        databaseReference.child("freeTimes").child(scheduleID).setValue(modelFreeTime)
                .addOnCompleteListener(new OnCompleteListener<Void>() {


                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        datePick.setText("");
                        startTimePick.setText("");
                        endTimePick.setText("");
                        freeSlot.setText("");
                        rlAdd.setVisibility(View.GONE);
                        rlShow.setVisibility(View.VISIBLE);

                        createtNotification(owner, date, scheduleID, modelTeacher.getFullName());

                        Toast.makeText(getContext(), "Free Time Added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Data Sent Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createtNotification(String owner, String date, String scheduleId, String notifierName) {
        ModelNotification modelNotification = new ModelNotification(owner, date, scheduleId, notifierName);

        String noticationId = databaseReference.child("notifications").push().getKey();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("notifications");

        assert noticationId != null;
        dbRef.child(noticationId).setValue(modelNotification);

        String sender =  "<b>"+ notifierName+"</b>" + " added a schedule";


        String NOTIFICATION_TOPIC = "/topics/" + "SCHEDULE";
        String NOTIFICATION_TITLE = "New Notification";
        String NOTIFICATION_TYPE = "scheduleNotification";


        JSONObject notificationJo = new JSONObject();

        JSONObject notificationBodyJo = new JSONObject();


        try {
            notificationBodyJo.put("notificationType", NOTIFICATION_TYPE);
            notificationBodyJo.put("sender", owner);
            notificationBodyJo.put("nId", noticationId);
            notificationBodyJo.put("nTitle", NOTIFICATION_TITLE);
            notificationBodyJo.put("nDescription", Html.fromHtml(sender));

            notificationJo.put("to", NOTIFICATION_TOPIC);

            notificationJo.put("data", notificationBodyJo);



        } catch (Exception e){
            e.printStackTrace();
        }

        sendScheduleNotification(notificationJo);


    }

    private void sendScheduleNotification(JSONObject notificationJo) {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", notificationJo, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("JSON_RESPONSE", "onResponse: " + response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("JSON_ERROR", "onResponse: " + error.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "key=AAAAU0EpuEU:APA91bHE5EK3qdTJWegs9JKYCAlc8gZvbEvp41wQGOUlhGo-mtPDm6gRkOkM2JcY7tyz5iHLB0rGYFGvgoQ8FMdJfRcP0JbH9nkjcHsZakeK0Hfa29oAehUt0y0cAzCqEvUksbIkUpmq");
                return headers;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void pickNumber() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") View theView = inflater.inflate(R.layout.number_picker_dialog, null);
        numberPicker = theView.findViewById(R.id.number_picker);

        builder.setView(theView)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        freeSlot.setText(String.valueOf(numberPicker.getValue()));
                        Log.d("DBG","Price is: "+numberPicker.getValue());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(20);

        builder.create();
        builder.show();

    }

    private void setAppointmentDate(final TextView datePick) {
        datePickerDialog = new DatePickerDialog(Objects.requireNonNull(getContext()),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy");

                        Calendar cal = Calendar.getInstance();

                        cal.setTimeInMillis(0);
                        cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                        Date date = cal.getTime();
                        String dateTime = formatter.format(date);
                        datePick.setText(dateTime);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void setAppointmentTime(final TextView timePick) {
        timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");

                        Calendar cal = Calendar.getInstance();

                        cal.setTimeInMillis(0);
                        cal.set(0, 0, 0, hourOfDay, minute, 0);
                        Date date = cal.getTime();
                        String dateTime = formatter.format(date);
                        timePick.setText(dateTime);

                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }



    private void retrieveMyAppointment() {
        freeTimeList = new ArrayList<>();

        try {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("freeTimes");

            Query query = dbRef.orderByChild("freeDate");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    freeTimeList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){

                        ModelFreeTime modelFreeTime = ds.getValue(ModelFreeTime.class);

                        Log.i("Retrieve", ds.toString());

                        assert modelFreeTime != null;
                        if (modelFreeTime.getOwner().equals(user.getUid())){
                            freeTimeList.add(modelFreeTime);
                        }

                    }


                    adapterFreeTime = new AdapterFreeTime(freeTimeList, getContext(), modelTeacher);

                    LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());

                    freeTimeRv.setLayoutManager(linearLayout);

                    freeTimeRv.setAdapter(adapterFreeTime);

                    Log.i("LIST", (String.valueOf(freeTimeList.size())));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }



}

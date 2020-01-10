package com.example.notification.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.R;
import com.example.notification.adapters.AdapterFreeTime;
import com.example.notification.models.ModelFreeTime;
import com.example.notification.models.ModelTeacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TeacherDetailsActivity extends AppCompatActivity {

    TextView name, dept, designation,email;
    Button sendBtn;
    ImageView backBtn, tDImg;
    ModelTeacher modelTeacher;
    AdapterFreeTime adapterFreeTime;
    DatabaseReference dbRef;
    RecyclerView freeTimeRv;
    List<ModelFreeTime> freeTimeList;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_teacher_details);


        setUpViews();

        modelTeacher =  (ModelTeacher) getIntent().getSerializableExtra("modelTeacher");

        if (modelTeacher != null) {
            setToTheViews(modelTeacher);
            retrieveAppointmentList();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity();
            }
        });

    }

    private void retrieveAppointmentList() {
        freeTimeList = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference();

        dbRef.child("freeTimes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                freeTimeList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ModelFreeTime modelFreeTime = ds.getValue(ModelFreeTime.class);

                    assert modelFreeTime != null;
                    if (modelFreeTime.getOwner().equals(modelTeacher.getToken())){
                        freeTimeList.add(modelFreeTime);
                    }
                }
                adapterFreeTime = new AdapterFreeTime(freeTimeList, TeacherDetailsActivity.this, modelTeacher);

                LinearLayoutManager linearLayout = new LinearLayoutManager(TeacherDetailsActivity.this);

                freeTimeRv.setLayoutManager(linearLayout);

                freeTimeRv.setAdapter(adapterFreeTime);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void goToChatActivity() {
        Intent intent = new Intent(this, ChatActivity.class);

        intent.putExtra("modelTeacher", modelTeacher);
        startActivity(intent);
    }



    private void setUpViews() {
        name = findViewById(R.id.tvTName);
        dept = findViewById(R.id.tDDept);
        designation = findViewById(R.id.tDesig);
        email = findViewById(R.id.tDEmail);
        sendBtn = findViewById(R.id.btnSendMsg);
        backBtn = findViewById(R.id.back_btn);
        freeTimeRv = findViewById(R.id.freeTimeRv);
        tDImg = findViewById(R.id.td_img);
        firebaseAuth = FirebaseAuth.getInstance();
    }


    @SuppressLint("SetTextI18n")
    private void setToTheViews(ModelTeacher modelTeacher) {
        name.setText(modelTeacher.getName());
        dept.setText("Department: " + modelTeacher.getDept());
        email.setText("Email: " + modelTeacher.getEmail());
        designation.setText("Designation: " + modelTeacher.getDesignation());try {
            Picasso.get().load(modelTeacher.getImageLink()).into(tDImg);
        } catch (Exception e){
            Picasso.get().load(R.drawable.avatar).into(tDImg);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, activityOptions.toBundle());
        }

    }



}

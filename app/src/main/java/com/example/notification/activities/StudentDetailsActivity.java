package com.example.notification.activities;

import androidx.appcompat.app.AppCompatActivity;

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

import com.example.notification.models.ModelStudent;
import com.example.notification.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class StudentDetailsActivity extends AppCompatActivity {

    TextView name, dept, session, semester, regNo, email;
    Button sendBtn;
    ImageView backBtn, stDImg;
    ModelStudent modelStudent;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_student_details);


        setUpViews();

        modelStudent =  (ModelStudent) getIntent().getSerializableExtra("modelStudent");

        if (modelStudent != null) {
            setToTheViews(modelStudent);
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

    private void goToChatActivity() {
        Intent intent = new Intent(this, ChatActivity.class);

        intent.putExtra("modelStudent", modelStudent);
        startActivity(intent);
    }


    private void setUpViews() {
        name = findViewById(R.id.tvName);
        dept = findViewById(R.id.stDDept);
        session = findViewById(R.id.stDSes);
        semester = findViewById(R.id.stDSem);
        regNo = findViewById(R.id.stDReg);
        email = findViewById(R.id.stDEmail);
        sendBtn = findViewById(R.id.btnSendMsg);
        backBtn = findViewById(R.id.back_btn);
        stDImg = findViewById(R.id.std_img);
        firebaseAuth = FirebaseAuth.getInstance();
    }


    @SuppressLint("SetTextI18n")
    private void setToTheViews(ModelStudent modelStudent) {
        name.setText(modelStudent.getFullName());
        dept.setText("Department: " + modelStudent.getDepartment());
        session.setText("Session: " + modelStudent.getSession());
        semester.setText("Semester: " + modelStudent.getSemester());
        regNo.setText("Reg No: " + modelStudent.getRegNo());
        email.setText("Email: " + modelStudent.getEmail());
        try {
            Picasso.get().load(modelStudent.getImageLink()).into(stDImg);
        } catch (Exception e){
            Picasso.get().load(R.drawable.avatar).into(stDImg);
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

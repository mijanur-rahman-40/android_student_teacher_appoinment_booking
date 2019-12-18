package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentDetailsActivity extends AppCompatActivity {

    TextView name, dept, session, semester, regNo, email;
    Button sendBtn;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_student_details);


        setUpViews();

        UserStudent userStudent =  (UserStudent) getIntent().getSerializableExtra("userStudent");

        if (userStudent != null) {
            setToTheViews(userStudent);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
    }


    @SuppressLint("SetTextI18n")
    private void setToTheViews(UserStudent userStudent) {
        name.setText(userStudent.getFullName());
        dept.setText("Department: " + userStudent.getDepartment());
        session.setText("Session: " + userStudent.getSession());
        semester.setText("Semester: " + userStudent.getSemester());
        regNo.setText("Reg No: " + userStudent.getRegNo());
        email.setText("Email: " + userStudent.getEmail());
    }



}

package com.example.notification.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.notification.models.ModelStudent;
import com.example.notification.R;

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

        ModelStudent modelStudent =  (ModelStudent) getIntent().getSerializableExtra("modelStudent");

        if (modelStudent != null) {
            setToTheViews(modelStudent);
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
    private void setToTheViews(ModelStudent modelStudent) {
        name.setText(modelStudent.getFullName());
        dept.setText("Department: " + modelStudent.getDepartment());
        session.setText("Session: " + modelStudent.getSession());
        semester.setText("Semester: " + modelStudent.getSemester());
        regNo.setText("Reg No: " + modelStudent.getRegNo());
        email.setText("Email: " + modelStudent.getEmail());
    }



}
